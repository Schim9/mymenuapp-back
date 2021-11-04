package lu.perso.menuback.data;

import java.util.List;

public record MenuEntity(
        Long id,
        String name,
        List<DishEntity> lunchMeals,
        List<DishEntity> dinnerMeals
    ) {
}
