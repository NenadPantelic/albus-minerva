package rs.ac.kg.fin.albus.minerva.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewSubmission(@NotBlank String environment,
                            @NotBlank String examId,
                            @NotBlank String assignmentId,
                            @NotBlank String assignmentName,
                            @NotBlank @Size(max = 16384) String content) {
}
