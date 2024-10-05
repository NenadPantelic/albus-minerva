package rs.ac.kg.fin.albus.minerva.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@Document("submissionProhibitions")
@CompoundIndexes({
        @CompoundIndex(name = "idx_user_assignment", def = "{'userId' : 1, 'assignmentId': 1}", unique = true)
})
public class SubmissionProhibition {

    @Id
    private String id;
    private String userId;
    private String assignmentId;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
