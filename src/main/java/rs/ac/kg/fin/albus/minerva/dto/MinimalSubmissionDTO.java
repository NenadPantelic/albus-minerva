package rs.ac.kg.fin.albus.minerva.dto;

import lombok.Builder;
import rs.ac.kg.fin.albus.minerva.model.SubmissionStatus;

@Builder
public record MinimalSubmissionDTO(String id,
                                   String environment,
                                   String userId,
                                   String assignmentId,
                                   String examId,
                                   String assignmentName,
                                   float score,
                                   SubmissionStatus status) {
}
