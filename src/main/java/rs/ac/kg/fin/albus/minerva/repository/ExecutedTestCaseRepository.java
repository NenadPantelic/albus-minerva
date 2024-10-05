package rs.ac.kg.fin.albus.minerva.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import rs.ac.kg.fin.albus.minerva.model.ExecutedTestCase;

import java.util.List;

public interface ExecutedTestCaseRepository extends MongoRepository<ExecutedTestCase, String> {

    @Query("{submissionId:'?0'}")
    List<ExecutedTestCase> findBySubmissionId(String submissionId, Sort sort);
}
