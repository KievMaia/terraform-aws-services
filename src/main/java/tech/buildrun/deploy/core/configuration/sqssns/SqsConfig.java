package tech.buildrun.deploy.core.configuration.sqssns;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.config.SqsListenerConfigurer;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
public class SqsConfig {

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(
            final SqsAsyncClient sqsAsyncClient) {
        return SqsMessageListenerContainerFactory
                .builder()
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        final var credentials = AwsBasicCredentials.create("test", "test");
        return SqsAsyncClient.builder()
                .endpointOverride(URI.create(
                        "http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/"))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public SqsListenerConfigurer configurer(final ObjectMapper objectMapper) {
        return registrar -> registrar.setObjectMapper(objectMapper);
    }
}