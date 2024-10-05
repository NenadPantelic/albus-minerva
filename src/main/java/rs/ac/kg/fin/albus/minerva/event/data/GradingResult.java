package rs.ac.kg.fin.albus.minerva.event.data;

import lombok.Builder;

import java.util.List;

@Builder
public record GradingResult(List<TestCaseResult> testCaseResults,
                            float score,
                            float total) {
}
