package rs.ac.kg.fin.albus.minerva.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.ac.kg.fin.albus.minerva.auth.UserContext;
import rs.ac.kg.fin.albus.minerva.configproperties.SubmissionConfigProperties;
import rs.ac.kg.fin.albus.minerva.dto.DetailedSubmissionDTO;
import rs.ac.kg.fin.albus.minerva.dto.MinimalSubmissionDTO;
import rs.ac.kg.fin.albus.minerva.dto.NewSubmission;
import rs.ac.kg.fin.albus.minerva.dto.SubmissionAllowance;
import rs.ac.kg.fin.albus.minerva.exception.ApiException;
import rs.ac.kg.fin.albus.minerva.mapper.SubmissionMapper;
import rs.ac.kg.fin.albus.minerva.model.ExecutedTestCase;
import rs.ac.kg.fin.albus.minerva.model.Submission;
import rs.ac.kg.fin.albus.minerva.model.SubmissionProhibition;
import rs.ac.kg.fin.albus.minerva.model.SubmissionStatus;
import rs.ac.kg.fin.albus.minerva.repository.ExecutedTestCaseRepository;
import rs.ac.kg.fin.albus.minerva.repository.SubmissionProhibitionRepository;
import rs.ac.kg.fin.albus.minerva.repository.SubmissionRepository;
import rs.ac.kg.fin.albus.minerva.service.SubmissionService;

import java.util.List;

@Slf4j
@Service
public class SubmissionServiceImpl implements SubmissionService {

    private static final Sort SORT_BY_ORDER_NO = Sort.by(Sort.Direction.ASC, "orderNo");
    private final SubmissionRepository submissionRepository;
    private final SubmissionProhibitionRepository submissionProhibitionRepository;
    private final ExecutedTestCaseRepository executedTestCaseRepository;
    private final int submissionsLimitPerAssignment;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository,
                                 SubmissionProhibitionRepository submissionProhibitionRepository,
                                 ExecutedTestCaseRepository executedTestCaseRepository,
                                 SubmissionConfigProperties submissionConfigProperties) {
        this.submissionRepository = submissionRepository;
        this.submissionProhibitionRepository = submissionProhibitionRepository;
        this.executedTestCaseRepository = executedTestCaseRepository;
        submissionsLimitPerAssignment = submissionConfigProperties.getSubmissionsLimitPerAssignment();
    }

    @Override
    public MinimalSubmissionDTO createSubmission(NewSubmission newSubmission) {
        log.info("Create new submission: {}", newSubmission);

        String userId = getUserId();
        String assignmentId = newSubmission.assignmentId();

        if (submissionProhibitionRepository.existsSubmissionProhibition(userId, assignmentId)) {
            throw new ApiException("You reached the maximum number of submissions", 403);
        }

        long numOfSubmissions = submissionRepository.countSubmissionsByUserAndAssignment(userId, assignmentId);
        if (numOfSubmissions >= submissionsLimitPerAssignment) {
            SubmissionProhibition submissionProhibition = SubmissionProhibition.builder()
                    .assignmentId(assignmentId)
                    .userId(userId)
                    .build();
            submissionProhibitionRepository.save(submissionProhibition);
            throw new ApiException("You reached the maximum number of submissions", 403);
        }

        Submission submission = Submission.builder()
                .assignmentId(assignmentId)
                .assignmentName(newSubmission.assignmentName())
                .content(newSubmission.content())
                .environment(newSubmission.environment())
                .examId(newSubmission.examId())
                .status(SubmissionStatus.PENDING)
                .creatorId(userId)
                .build();

        submission = submissionRepository.save(submission);
        return SubmissionMapper.mapToMinimalDTO(submission);
    }

    @Override
    public DetailedSubmissionDTO getSubmission(String submissionId) {
        log.info("Get submission[testCaseId = {}]", submissionId);
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ApiException("Not found.", 404));

        String userId = getUserId();
        if (!submission.getCreatorId().equals(userId)) {
            throw new ApiException("Forbidden.", 403);
        }

        List<ExecutedTestCase> executedTestCases = executedTestCaseRepository.findBySubmissionId(
                submissionId, SORT_BY_ORDER_NO
        );
        return SubmissionMapper.mapToDetailedDTO(submission, executedTestCases);
    }

    @Override
    public SubmissionAllowance getSubmissionAllowance(String assignmentId) {
        String userId = getUserId();
        log.info("Get submission allowance report: userId = {}, assignmentId = {}", userId, assignmentId);

        int submissionCount;
        int submissionLeft;

        boolean existSubmissionProhibition = submissionProhibitionRepository.existsSubmissionProhibition(
                userId, assignmentId
        );

        if (existSubmissionProhibition) {
            submissionCount = submissionsLimitPerAssignment;
            submissionLeft = 0;
        } else {
            submissionCount = (int) submissionRepository.countSubmissionsByUserAndAssignment(userId, assignmentId);
            submissionLeft = submissionsLimitPerAssignment - submissionCount;
        }

        return new SubmissionAllowance(assignmentId, submissionCount, submissionLeft);
    }

    @Override
    public List<MinimalSubmissionDTO> listAllSubmissions(int pageNo, int pageSize) {
        log.info("List all submissions...");
        PageRequest pageRequest = PageRequest.of(
                pageNo, pageSize, Sort.Direction.DESC, "created_at"
        );
        return SubmissionMapper.mapToMinimalDTOList(
                submissionRepository.findAll(pageRequest).toList()
        );
    }

    @Override
    public List<MinimalSubmissionDTO> listMySubmissions(String examId, int pageNo, int pageSize) {
        String userId = getUserId();
        log.info("List submissions made by user '{}', examId = {}", userId, examId);

        PageRequest pageRequest = PageRequest.of(
                pageNo, pageSize, Sort.Direction.DESC, "created_at"
        );
        return SubmissionMapper.mapToMinimalDTOList(
                submissionRepository.findByUserIdAndExamId(userId, examId, pageRequest)
        );
    }

    private String getUserId() {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userContext.userId();
    }
}
