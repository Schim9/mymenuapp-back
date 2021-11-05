package lu.perso.menuback.models;

import java.util.List;

public record Menu(
        Long id,
        String name,
        List<MenuItem> lunchMeals,
        List<MenuItem> dinnerMeals
) {
}
