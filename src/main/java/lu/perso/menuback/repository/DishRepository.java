package lu.perso.menuback.repository;

import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.data.IngredientEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DishRepository extends MongoRepository<DishEntity, String> {
    List<DishEntity> findAll();

    DishEntity findByName(String name);
}