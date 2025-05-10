package com.nando.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.file.Paths;

@ApplicationScoped
public class S3Service {

    private String bucketName;

    private String bucketRegion;

    private String fileName;

    public S3Service(@ConfigProperty(name = "s3.bucket.name") String bucketName,
                     @ConfigProperty(name = "s3.bucket.region") String bucketRegion,
                     @ConfigProperty(name = "s3.name-file-to-save") String fileName)
    {
        this.bucketName = bucketName;
        this.bucketRegion = bucketRegion;
        this.fileName = fileName;
    }

    public void sendFileBucket(File file) {
        S3Client s3Client = S3Client.builder()
                .region(Region.of(bucketRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build(),
                Paths.get(file.getPath()));
    }

}
