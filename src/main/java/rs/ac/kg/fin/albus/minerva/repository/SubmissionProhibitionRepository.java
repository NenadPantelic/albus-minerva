package rs.ac.kg.fin.albus.minerva.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import rs.ac.kg.fin.albus.minerva.model.SubmissionProhibition;

public interface SubmissionProhibitionRepository extends MongoRepository<SubmissionProhibition, String> {

    @Query(value = "{userId:'?0', assignmentId:'?1'}", exists = true)
    boolean existsSubmissionProhibition(String userId, String assignmentId);
}
