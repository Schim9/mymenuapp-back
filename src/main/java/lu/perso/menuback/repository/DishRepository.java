package lu.perso.menuback.repository;

import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.data.IngredientEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<DishEntity, Long> {

    Optional<DishEntity> findByName(String name);

    @Query("SELECT DISTINCT d FROM DishEntity d " +
            "JOIN d.ingredients i " +
            "WHERE i IN :ingredients")
    List<DishEntity> findDishEntitiesByIngredientsContaining(@Param("ingredients") List<IngredientEntity> ingredients);
}