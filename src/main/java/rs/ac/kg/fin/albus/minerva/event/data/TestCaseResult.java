package rs.ac.kg.fin.albus.minerva.event.data;

public record TestCaseResult(String testCaseId,
                             String value,
                             String solution,
                             float score) {
}
