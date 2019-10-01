package com.yabonza.APITest.aws;

/**
 * An interface for a AWS S3 bucket service to add and remove files
 */
public interface AWSS3Service {
    /**
     * Download the dog image from the supplied URL and upload it to a supplied AWS S3 bucket
     *
     * @param url the url of the dog image
     * @return the image key name of the location in the S3 bucket
     */
    String uploadFileToS3Bucket(String url);

    /**
     * Delete the file from the S3 bucket that has the supplied file name key
     *
     * @param fileName the file name key of the file in AWS S3 bucket
     */
    void deleteFileFromS3Bucket(String fileName);
}
