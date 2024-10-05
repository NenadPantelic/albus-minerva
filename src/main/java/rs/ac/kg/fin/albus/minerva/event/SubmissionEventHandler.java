package rs.ac.kg.fin.albus.minerva.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import rs.ac.kg.fin.albus.minerva.event.data.CodeSubmission;
import rs.ac.kg.fin.albus.minerva.event.data.GradingResult;
import rs.ac.kg.fin.albus.minerva.event.data.SubmissionGradingResult;
import rs.ac.kg.fin.albus.minerva.model.Submission;
import rs.ac.kg.fin.albus.minerva.model.SubmissionStatus;
import rs.ac.kg.fin.albus.minerva.repository.SubmissionRepository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class SubmissionEventHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    private final SubmissionRepository submissionRepository;

    public SubmissionEventHandler(KafkaTemplate<String, String> kafkaTemplate,
                                  @Value("${spring.kafka.producer.topic}") String topic,
                                  SubmissionRepository submissionRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.submissionRepository = submissionRepository;
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
        String eventPayload = serializeCodeSubmission(codeSubmission);
        CompletableFuture<SendResult<String, String>> completableFuture = kafkaTemplate.send(
                topic, key, eventPayload
        );

        completableFuture.whenComplete((sendResult, throwable) -> {
            if (throwable != null) {
                handleFailure(key, codeSubmission, throwable);
            } else {
                handleSuccess(key, codeSubmission, sendResult);
            }
        });
    }

    @KafkaListener(topics = {"submission-grading-result-events"}, groupId = "submission-grading-events-listener-group")
    public void handleCodeSubmission(ConsumerRecord<String, String> consumerRecord) {
        String submissionId = consumerRecord.key();
        SubmissionGradingResult submissionGradingResult = deserializeSubmissionGradingResult(consumerRecord.value());
        log.info("Received a submission grading result {}", submissionGradingResult);

        Optional<Submission> submissionOptional = submissionRepository.findById(submissionId);
        if (submissionOptional.isEmpty()) {
            log.error("Submission[id = {}] not present", submissionId);
            return;
        }

        Submission submission = submissionOptional.get();
        // TODO should save the details too - test cases etc
        GradingResult gradingResult = submissionGradingResult.gradingResult();
        submission.setScore(gradingResult.score());

        if (gradingResult.score() == 0) {
            updateSubmissionStatus(submission, SubmissionStatus.FAILED);
        } else if (gradingResult.score() != gradingResult.total()) {
            updateSubmissionStatus(submissionId, SubmissionStatus.PARTIALLY_CORRECT);
        } else {
            updateSubmissionStatus(submissionId, SubmissionStatus.OK);
        }
    }


    private void handleSuccess(String key,
                               CodeSubmission codeSubmission,
                               SendResult<String, String> sendResult) {
        log.info(
                "Message sent successfully for: key = {}, value = {}, partition = {}",
                key, codeSubmission, sendResult.getRecordMetadata().partition()
        );
        updateSubmissionStatus(key, SubmissionStatus.RUNNING);
    }

    private void handleFailure(String key, CodeSubmission codeSubmission, Throwable throwable) {
        log.error(
                "An error occurred when sending the message {}/{} due to {}",
                key, codeSubmission, throwable.getMessage(), throwable
        );
        updateSubmissionStatus(key, SubmissionStatus.FAILED);
    }

    private String serializeCodeSubmission(CodeSubmission codeSubmission) {
        try {
            return OBJECT_MAPPER.writeValueAsString(codeSubmission);
        } catch (JsonProcessingException e) {
            log.error("Unable to serialize code submission {} due to {}", codeSubmission, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private SubmissionGradingResult deserializeSubmissionGradingResult(String payload) {
        try {
            return OBJECT_MAPPER.readValue(payload, SubmissionGradingResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateSubmissionStatus(String submissionId, SubmissionStatus status) {
        Optional<Submission> submissionOptional = submissionRepository.findById(submissionId);
        if (submissionOptional.isEmpty()) {
            log.error("Submission[id = {}] not present", submissionId);
            return;
        }
        updateSubmissionStatus(submissionOptional.get(), status);
    }

    private void updateSubmissionStatus(Submission submission, SubmissionStatus status) {
        submission.setStatus(status);
        submissionRepository.save(submission);
    }
}
