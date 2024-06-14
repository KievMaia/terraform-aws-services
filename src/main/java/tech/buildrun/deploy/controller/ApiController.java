package tech.buildrun.deploy.controller;

import io.awspring.cloud.sns.core.SnsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.buildrun.deploy.model.TimeStampResponse;
import tech.buildrun.deploy.service.S3Service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "public-api")
public class ApiController {
    @Autowired
    private S3Service s3Service;

    @Autowired
    ResourceLoader resourceLoader;

    public ApiController(final SnsTemplate snsTemplate) {
        this.snsTemplate = snsTemplate;
    }

    private final SnsTemplate snsTemplate;

    @GetMapping
    public ResponseEntity<TimeStampResponse> helloAws() {
        final var now = new TimeStampResponse(Instant.now());
        snsTemplate.convertAndSend("public-api-sns", now);
        return ResponseEntity.ok(now);
    }

    @PutMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        final File file = s3Service.convertMultipartFileToFile(multipartFile);
        s3Service.uploadFile("public-api-s3", multipartFile.getOriginalFilename(), file.getAbsoluteFile());
        return "File uploaded successfully.";
    }

    @GetMapping(value = "/getfiles")
    public List<String> getFiles() {
        return s3Service.getFiles("public-api-s3");
    }

    @DeleteMapping(value = "/delete")
    public String deleteFile(@RequestParam("key") String key) {
        s3Service.deleteFile("public-api-s3", key);
        return "File deleted successfully.";
    }
}