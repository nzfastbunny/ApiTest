package com.yabonza.APITest.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Model class for dog breeds to be put into the MongoDB collection
 */
public class DogBreed {
    /**
     * The database identifier
     */
    @Id
    public String id;
    /**
     * The name of the breed of dog
     */
    public String breedName;
    /**
     * The date the file was added to AWS S3
     */
    public Date dateAdded;
    /**
     * The file key in AWS S3
     */
    public String s3Location;

    public DogBreed() {
    }

    public DogBreed(String breedName, Date dateAdded, String s3Location) {
        this.breedName = breedName;
        this.dateAdded = dateAdded;
        this.s3Location = s3Location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getS3Location() {
        return s3Location;
    }

    public void setS3Location(String s3Location) {
        this.s3Location = s3Location;
    }

    @Override
    public String toString() {
        return String.format("DogBreed[id=%s, breedName='%s', dateAdded='%s, s3Location='%s']",
                id, breedName, dateAdded, s3Location);
    }
}
