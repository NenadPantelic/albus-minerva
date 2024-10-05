package rs.ac.kg.fin.albus.minerva.event.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import rs.ac.kg.fin.albus.minerva.model.Submission;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class SubmissionEventProducer {

    private final KafkaTemplate<String, CodeSubmission> kafkaTemplate;
    private final String topic;

    public SubmissionEventProducer(KafkaTemplate<String, CodeSubmission> kafkaTemplate,
                                   @Value("${spring.kafka.producer.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }


    public void sendSubmissionEvent(Submission submission) throws JsonProcessingException {
        String key = submission.getId();
        CodeSubmission codeSubmission = CodeSubmission.builder()
                .submissionId(submission.getId())
                .code(submission.getContent())
                .assignmentId(submission.getAssignmentId())
                .environment(submission.getEnvironment())
                .userId(submission.getCreatorId())
                .build();
        CompletableFuture<SendResult<String, CodeSubmission>> completableFuture = kafkaTemplate.send(
                topic, key, codeSubmission
        );

        completableFuture.whenComplete((sendResult, throwable) -> {
            if (throwable != null) {
                handleFailure(key, codeSubmission, throwable);
            } else {
                handleSuccess(key, codeSubmission, sendResult);
            }
        });
    }

    private void handleSuccess(String key,
                               CodeSubmission codeSubmission,
                               SendResult<String, CodeSubmission> sendResult) {
        log.info(
                "Message sent successfully for: key = {}, value = {}, partition = {}",
                key, codeSubmission, sendResult.getRecordMetadata().partition()
        );
    }

    private void handleFailure(String key, CodeSubmission codeSubmission, Throwable throwable) {
        log.error(
                "An error occurred when sending the message {}/{} due to {}",
                key, codeSubmission, throwable.getMessage(), throwable
        );

    }
}
