package rs.ac.kg.fin.albus.minerva.model;

public enum SubmissionStatus {

    // not yet sent to execution
    PENDING,
    // running at the moment
    RUNNING,
    // not true
    FAILED,
    // partially correct
    PARTIALLY_CORRECT,
    // true
    OK

}
