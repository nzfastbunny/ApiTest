package com.yabonza.APITest.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class AWSS3ServiceImpl implements AWSS3Service {
    /**
     * The AWS S3 bucket name
     */
    private String awsS3Bucket;
    /**
     * The AWS S3 connection object
     */
    private AmazonS3 amazonS3;

    @Autowired
    public AWSS3ServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3Bucket) {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion.getName()).build();
        this.awsS3Bucket = awsS3Bucket;
    }

    @Async
    public String uploadFileToS3Bucket(String url) {
        try {
            //creating the file in the server (temporarily)
            File file = new File(url);
            String name = file.getName();

            InputStream inputStream = new URL(url).openStream();
            Files.copy(inputStream, Paths.get(name), StandardCopyOption.REPLACE_EXISTING);
            file = new File(name); // updating the file to be the downloaded version not the URL

            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3Bucket, name, file);
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            this.amazonS3.putObject(putObjectRequest);

            //removing the file created in the server
            file.delete();

            return name;
        } catch (IOException | AmazonServiceException ex) {
            System.err.println("error [" + ex.getMessage() + "] occurred while uploading [" + url + "] ");
            return "";
        }
    }

    @Async
    public void deleteFileFromS3Bucket(String fileName) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(awsS3Bucket, fileName));
        } catch (AmazonServiceException ex) {
            System.err.println("error [" + ex.getMessage() + "] occurred while removing [" + fileName + "] ");
        }
    }
}
