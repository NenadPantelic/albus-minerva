package rs.ac.kg.fin.albus.minerva.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.kg.fin.albus.minerva.dto.ExamResult;
import rs.ac.kg.fin.albus.minerva.service.ExamResultService;

@Slf4j
@RestController
public class ExamResultController {

    private final ExamResultService examResultService;

    public ExamResultController(ExamResultService examResultService) {
        this.examResultService = examResultService;
    }

    @GetMapping("/api/v1/exam/{examId}")
    public ExamResult completeExam(@PathVariable("examId") String examId) {
        log.info("Received a request to get exam results...");
        return examResultService.getExamResult(examId);
    }
}
