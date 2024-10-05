package rs.ac.kg.fin.albus.minerva.dto;

import lombok.Builder;
import rs.ac.kg.fin.albus.minerva.model.SubmissionStatus;

import java.util.List;

@Builder
public record DetailedSubmissionDTO(String id,
                                    String content,
                                    String environment,
                                    String userId,
                                    String assignmentId,
                                    String examId,
                                    List<ExecutedTestCaseDTO> testCases,
                                    int score,
                                    SubmissionStatus status) {
}
