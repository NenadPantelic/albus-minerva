package rs.ac.kg.fin.albus.minerva.event.data;

public record SubmissionGradingResult(String submissionId,
                                      String assignmentId,
                                      String userId,
                                      GradingResult gradingResult) {

}
