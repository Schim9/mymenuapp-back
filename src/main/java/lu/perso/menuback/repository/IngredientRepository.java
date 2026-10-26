package lu.perso.menuback.repository;

import lu.perso.menuback.data.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {

    Optional<IngredientEntity> findByName(String name);


}