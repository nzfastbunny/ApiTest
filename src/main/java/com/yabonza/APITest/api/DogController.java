package com.yabonza.APITest.api;

import com.yabonza.APITest.aws.AWSS3Service;
import com.yabonza.APITest.model.DogBreed;
import com.yabonza.APITest.mongodb.DogBreedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class DogController {
    @Autowired
    private DogBreedRepository repository;
    @Autowired
    private AWSS3Service amazonS3ClientService;
    private static final String URL_PREFIX = "https://images.dog.ceo/breeds/";
    private static final String URLADDRESS = "https://dog.ceo/api/breeds/image/random";

    @RequestMapping(value = "/AddDogBreed", method = RequestMethod.POST)
    public ActionDogResponse addDogBreed() {
        RestTemplate restTemplate = new RestTemplate();
        DogResponse response = restTemplate.getForObject(URLADDRESS, DogResponse.class);

        String imageURL = response.getMessage();
        String breed = getBreed(response);

        String imagePath = amazonS3ClientService.uploadFileToS3Bucket(imageURL);

        DogBreed breedObj = new DogBreed(breed, new Date(), imagePath);
        DogBreed result = repository.insert(breedObj);

        return new ActionDogResponse(result.getId(), result.toString());
    }

    private String getBreed(DogResponse response) {
        String[] breeds = response.getMessage().replace(URL_PREFIX, "").split("/");
        return breeds[0];
    }

    @RequestMapping(value = "/GetDogBreedById", method = RequestMethod.GET)
    public ActionDogResponse getDogBreedById(@RequestParam("id") String id) {
        ActionDogResponse response = new ActionDogResponse(id, "Dog Breed Could Not Be Found!");

        Optional<DogBreed> result = repository.findById(id);
        if (result.isPresent()) {
            response = new ActionDogResponse(id, result.get().toString());
        }

        return response;
    }

    @RequestMapping(value = "/RemoveDogBreedById", method = RequestMethod.DELETE)
    public ActionDogResponse removeDogBreedById(@RequestParam("id") String id) {
        String content = String.format("The dog breed record with id=%s does not exist and could not be removed", id);

        Optional<DogBreed> result = repository.findById(id);
        if (result.isPresent()) {
            amazonS3ClientService.deleteFileFromS3Bucket(result.get().getS3Location());
            repository.deleteById(id);
            content = "Dog Breed Removed";
        }

        return new ActionDogResponse(id, content);
    }

    @RequestMapping(value = "/GetDogBreed", method = RequestMethod.GET)
    public ActionDogResponse searchDogBreed(@RequestParam("breedName") String breedName) {
        List<DogBreed> results = repository.findByBreedName(breedName);

        String content = "";
        if (results.isEmpty()) {
            content = "No records found for " + breedName;
        } else {
            content = results.toString();
        }

        return new ActionDogResponse(breedName, content);
    }

    @RequestMapping(value = "/AllBreeds", method = RequestMethod.GET)
    public ActionDogResponse getAllDogBreeds() {
        List<DogBreed> results = repository.findAll();

        List<String> breeds = new ArrayList<>();
        for (DogBreed result : results) {
            if (!breeds.contains(result.getBreedName())) {
                breeds.add(result.getBreedName());
            }
        }

        return new ActionDogResponse("", breeds.toString());
    }
}
