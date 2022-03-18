package lu.perso.menuback.data;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
@Document(collection = "menus")
public record MenuEntity(
        String name,
        LocalDate date,
        List<MenuItemEntity> lunchMeals,
        List<MenuItemEntity> dinnerMeals
    ) {
}
