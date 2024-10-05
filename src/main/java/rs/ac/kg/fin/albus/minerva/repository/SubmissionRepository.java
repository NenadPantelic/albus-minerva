package rs.ac.kg.fin.albus.minerva.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import rs.ac.kg.fin.albus.minerva.model.Submission;

import java.util.List;

public interface SubmissionRepository extends MongoRepository<Submission, String> {

    @Query("{userId:'?0', examId:'?1'}")
    List<Submission> findByUserIdAndExamId(String userId, String examId, Pageable pageable);

    @Query(value = "{userId: ?0, assignmentId: ?1}", count = true)
    long countSubmissionsByUserAndAssignment(String userId, String assignmentId);
}
