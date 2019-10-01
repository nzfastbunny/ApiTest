package com.yabonza.APITest.mongodb;

import com.yabonza.APITest.model.DogBreed;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Interface for the non-simple MongoDB queries
 */
public interface DogBreedRepository extends MongoRepository<DogBreed, String> {
    /**
     * Find all the records in the database that have the supplied breed name
     *
     * @param breedName the supplied breed name
     * @return a list of the db records with the supplied dog breed or an empty list
     */
    public List<DogBreed> findByBreedName(String breedName);
}
