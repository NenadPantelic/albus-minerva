package rs.ac.kg.fin.albus.minerva.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.ac.kg.fin.albus.minerva.dto.DetailedSubmissionDTO;
import rs.ac.kg.fin.albus.minerva.dto.MinimalSubmissionDTO;
import rs.ac.kg.fin.albus.minerva.dto.NewSubmission;
import rs.ac.kg.fin.albus.minerva.dto.SubmissionAllowance;
import rs.ac.kg.fin.albus.minerva.service.SubmissionService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public MinimalSubmissionDTO createSubmission(@Valid @RequestBody NewSubmission newSubmission) {
        log.info("Received a new submission...");
        return submissionService.createSubmission(newSubmission);
    }

    @GetMapping("/{submissionId}")
    public DetailedSubmissionDTO getSubmission(@PathVariable("submissionId") String submissionId) {
        log.info("Received a request to get submission...");
        return submissionService.getSubmission(submissionId);
    }

    @GetMapping("/allowance")
    public SubmissionAllowance getSubmissionAllowance(@RequestParam(value = "assignmentId") String assignmentId) {
        log.info("Received a request to fetch submission allowance...");
        return submissionService.getSubmissionAllowance(assignmentId);
    }

    // TODO: admin only
    @GetMapping("/_all")
    public List<MinimalSubmissionDTO> listSubmissions(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "50") int size) {
        log.info("Received a request to list all submissions...");
        return submissionService.listAllSubmissions(page, size);
    }

    @GetMapping
    public List<MinimalSubmissionDTO> listMySubmissions(@RequestParam(value = "examId") String examId,
                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "50") int size) {
        log.info("Received a request to list user submissions...");
        return submissionService.listMySubmissions(examId, page, size);
    }
}
