package lu.perso.menuback.repository;

import lu.perso.menuback.data.MenuEntity;
import lu.perso.menuback.data.MenuItemEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MenuRepository extends MongoRepository<MenuEntity, Long> {
    List<MenuEntity> findAll();

    MenuEntity findByName(String name);

    List<MenuEntity> findMenuEntitiesByLunchMealsContainingOrDinnerMealsContaining(List<MenuItemEntity> lunchMeals, List<MenuItemEntity> dinnerMeals);
}