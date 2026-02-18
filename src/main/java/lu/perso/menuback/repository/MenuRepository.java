package lu.perso.menuback.repository;

import lu.perso.menuback.data.MenuEntity;
import lu.perso.menuback.data.MenuItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuRepository extends CrudRepository<MenuEntity, Long> {

    // Utilisation de JPQL avec @Query
    @Query("SELECT m FROM MenuEntity m WHERE m.date BETWEEN :startDate AND :endDate")
    List<MenuEntity> findAll(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Optional<MenuEntity> findByDate(LocalDate date);

    @Query("SELECT DISTINCT m FROM MenuEntity m " +
            "LEFT JOIN m.lunchMeals lm " +
            "LEFT JOIN m.dinnerMeals dm " +
            "WHERE lm IN :lunchItems OR dm IN :dinnerItems")
    List<MenuEntity> findMenus(
            @Param("lunchItems") List<MenuItemEntity> lunchItems,
            @Param("dinnerItems") List<MenuItemEntity> dinnerItems
    );
}