package lu.perso.menuback.repository;

import lu.perso.menuback.data.DatabaseSequence;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SequenceRepository extends MongoRepository<DatabaseSequence, String> {
}