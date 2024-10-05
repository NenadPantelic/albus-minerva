package rs.ac.kg.fin.albus.minerva.mapper;

import rs.ac.kg.fin.albus.minerva.dto.DetailedSubmissionDTO;
import rs.ac.kg.fin.albus.minerva.dto.MinimalSubmissionDTO;
import rs.ac.kg.fin.albus.minerva.model.ExecutedTestCase;
import rs.ac.kg.fin.albus.minerva.dto.ExecutedTestCaseDTO;
import rs.ac.kg.fin.albus.minerva.model.Submission;

import java.util.List;
import java.util.stream.Collectors;

public class SubmissionMapper {

    public static DetailedSubmissionDTO mapToDetailedDTO(Submission submission, List<ExecutedTestCase> executedTestCases) {
        if (submission == null) {
            return null;
        }

        return DetailedSubmissionDTO.builder()
                .id(submission.getId())
                .assignmentId(submission.getAssignmentId())
                .examId(submission.getExamId())
                .content(submission.getContent())
                .environment(submission.getEnvironment())
                .testCases(mapToTestCaseDTOList(executedTestCases))
                .status(submission.getStatus())
                .userId(submission.getCreatorId())
                .build();
    }

    public static MinimalSubmissionDTO mapToMinimalDTO(Submission submission) {
        if (submission == null) {
            return null;
        }

        return MinimalSubmissionDTO.builder()
                .id(submission.getId())
                .assignmentId(submission.getAssignmentId())
                .assignmentName(submission.getAssignmentName())
                .examId(submission.getExamId())
                .environment(submission.getEnvironment())
                .score(submission.getScore())
                .status(submission.getStatus())
                .userId(submission.getCreatorId())
                .build();
    }

    public static List<MinimalSubmissionDTO> mapToMinimalDTOList(List<Submission> submissions) {
        if (submissions == null) {
            return List.of();
        }

        return submissions.stream()
                .map(SubmissionMapper::mapToMinimalDTO)
                .collect(Collectors.toList());
    }

    public static List<ExecutedTestCaseDTO> mapToTestCaseDTOList(List<ExecutedTestCase> executedTestCases) {
        if (executedTestCases == null) {
            return null;
        }

        return executedTestCases.stream()
                .map(executedTestCase -> ExecutedTestCaseDTO.builder()
                        .id(executedTestCase.getId())
                        .submissionId(executedTestCase.getSubmissionId())
                        .input(executedTestCase.getInput())
                        .result(executedTestCase.getResult())
                        .success(executedTestCase.isSuccess())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
