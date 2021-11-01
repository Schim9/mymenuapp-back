package lu.perso.menuback.repository;

import lu.perso.menuback.data.IngredientEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends MongoRepository<IngredientEntity, Long> {

    List<IngredientEntity> findAll();

    Optional<IngredientEntity> findById(Long id);

    IngredientEntity findByName(String name);


}