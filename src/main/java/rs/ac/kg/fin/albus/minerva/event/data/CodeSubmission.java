package rs.ac.kg.fin.albus.minerva.event.data;

import lombok.Builder;

@Builder
public record CodeSubmission(String userId,
                             String assignmentId,
                             String submissionId,
                             String environment,
                             String code) {
}