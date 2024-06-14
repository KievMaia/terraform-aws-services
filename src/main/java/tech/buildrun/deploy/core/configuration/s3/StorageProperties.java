package tech.buildrun.deploy.core.configuration.s3;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageProperties {

    @Autowired
    private AmazonS3 amazonS3;
}
