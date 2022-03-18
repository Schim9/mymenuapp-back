package lu.perso.menuback.repository;

import lu.perso.menuback.data.MenuEntity;
import lu.perso.menuback.data.MenuItemEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuRepository extends MongoRepository<MenuEntity, Long> {

    @Query("{$and :[{date:  { $lte: ?1 }},{date:  { $gte: ?0 }}]}")
    List<MenuEntity> findAll(LocalDate startDate, LocalDate endDate);

    Optional<MenuEntity> findByDate(LocalDate date);

    List<MenuEntity> findMenuEntitiesByLunchMealsContainingOrDinnerMealsContaining(List<MenuItemEntity> lunchMeals, List<MenuItemEntity> dinnerMeals);
}