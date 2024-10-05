package rs.ac.kg.fin.albus.minerva.dto;

import lombok.Builder;

@Builder
public record ExecutedTestCaseDTO(String id,
                                  String submissionId,
                                  String input,
                                  String result,
                                  boolean success) {
}
