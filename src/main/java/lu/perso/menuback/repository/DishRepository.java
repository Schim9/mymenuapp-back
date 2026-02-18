package lu.perso.menuback.repository;

import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.data.IngredientEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DishRepository extends CrudRepository<DishEntity, Long> {
    List<DishEntity> findAll();

    DishEntity findByName(String name);

    @Query("SELECT DISTINCT d FROM DishEntity d " +
            "JOIN d.ingredients i " +
            "WHERE i IN :ingredients")
    List<DishEntity> findDishEntitiesByIngredientsContaining(@Param("ingredients") List<IngredientEntity> ingredients);
}