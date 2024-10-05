package rs.ac.kg.fin.albus.minerva.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@Document("submissions")
public class Submission {

    @Id
    private String id;
    private String content;
    private String environment;
    @Indexed
    private String creatorId;
    private String examId;
    private String assignmentId;
    private String assignmentName;
    @Builder.Default
    private SubmissionStatus status = SubmissionStatus.PENDING;
    private int score;
    private int maxPoints;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}