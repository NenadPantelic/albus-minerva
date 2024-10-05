package rs.ac.kg.fin.albus.minerva.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@AllArgsConstructor
@Getter
@Builder
@Document("executedTestCases")
public class ExecutedTestCase {

    @Id
    String id;
    String testCaseId;
    String orderNo;
    @Indexed
    String submissionId;
    String input;
    String result;
    boolean success;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
