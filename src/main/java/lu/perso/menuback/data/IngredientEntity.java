package lu.perso.menuback.data;

import lu.perso.menuback.constant.MenuEnum.UNIT;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ingredients")
public record IngredientEntity (
        @Id
        Long id,
        String name,
        Long sectionId,
        UNIT unit
)  implements MenuItemEntity { }
