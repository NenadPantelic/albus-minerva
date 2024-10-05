package rs.ac.kg.fin.albus.minerva.dto;

import java.util.List;

public record ExamResult(String examId,
                         String userId,
                         List<AssignmentResult> assignmentResults,
                         float totalScore,
                         float maxPossibleScore) {
}
