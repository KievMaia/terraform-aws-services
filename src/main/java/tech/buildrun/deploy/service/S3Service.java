package tech.buildrun.deploy.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class S3Service {
    @Autowired
    private AmazonS3 amazonS3;

    public void uploadFile(String bucketName, String key, File file) {
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file));
    }

    public List<String> getFiles(String bucketName) {
        final var result = amazonS3.listObjectsV2(bucketName);
        final var objects = result.getObjectSummaries();
        return objects.stream().map(S3ObjectSummary::getKey).toList();
    }

    public void deleteFile(String bucketName, String key) {
        amazonS3.deleteObject(bucketName, key);
    }

    public File convertMultipartFileToFile(MultipartFile file) {
        try {
            File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            file.transferTo(convFile);
            return convFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}