package lu.perso.menuback.models;

import java.time.LocalDate;
import java.util.List;

public record Menu(
        String name,
        LocalDate date,
        List<MenuItem> lunchMeals,
        List<MenuItem> dinnerMeals
) {
}
