package tech.buildrun.deploy.core.configuration.sqssns;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.net.URI;

@Configuration
public class SnsConfig {

    @Bean
    public SnsClient snsClient() {
            final var credentials = AwsBasicCredentials.create("test", "test");
            return SnsClient.builder()
                    .endpointOverride(URI.create("http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/"))
                    .region(Region.US_EAST_1)
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();
    }
}