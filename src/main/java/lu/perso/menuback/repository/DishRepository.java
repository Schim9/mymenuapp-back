package lu.perso.menuback.repository;

import lu.perso.menuback.data.DishEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DishRepository extends MongoRepository<DishEntity, Long> {
    List<DishEntity> findAll();

    DishEntity findByName(String name);
}