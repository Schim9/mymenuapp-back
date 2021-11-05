package lu.perso.menuback.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document(collection = "menus")
public record MenuEntity(
        @Id
        Long id,
        String name,
        List<MenuItemEntity> lunchMeals,
        List<MenuItemEntity> dinnerMeals
    ) {
}
