package rs.ac.kg.fin.albus.minerva.service;

import rs.ac.kg.fin.albus.minerva.dto.DetailedSubmissionDTO;
import rs.ac.kg.fin.albus.minerva.dto.MinimalSubmissionDTO;
import rs.ac.kg.fin.albus.minerva.dto.NewSubmission;
import rs.ac.kg.fin.albus.minerva.dto.SubmissionAllowance;

import java.util.List;

public interface SubmissionService {

    MinimalSubmissionDTO createSubmission(NewSubmission newSubmission);

    DetailedSubmissionDTO getSubmission(String submissionId);

    SubmissionAllowance getSubmissionAllowance(String assignmentId);

    List<MinimalSubmissionDTO> listAllSubmissions(int pageNo, int pageSize);

    List<MinimalSubmissionDTO> listMySubmissions(String examId, int pageNo, int pageSize);

    // TODO: add missing CRUD operations

}
