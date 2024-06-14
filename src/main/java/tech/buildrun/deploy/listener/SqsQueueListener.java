package tech.buildrun.deploy.listener;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;
import tech.buildrun.deploy.core.MessagingJsonMapper;
import tech.buildrun.deploy.core.SqsMessageWrapper;

@Component
public class SqsQueueListener {
    private final MessagingJsonMapper jsonMapper;

    public SqsQueueListener(final MessagingJsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @SqsListener(value = "public-api-sqs")
    public void receiveSqsEvent(String rawEvent) {
        final var sqsMessageWrapper = jsonMapper.fromJson(rawEvent, SqsMessageWrapper.class);
        System.out.println("Mensagem recebida do SQS: " + sqsMessageWrapper.getMessage());
    }
}
