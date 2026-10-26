package lu.perso.menuback.mappers;

import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.data.IngredientEntity;
import lu.perso.menuback.models.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DishMapper {

    @Mapping(source = "ingredients", target = "ingredients", qualifiedByName = "CustomMapperToView")
    Dish toView(DishEntity entity);

    @Named("CustomMapperToView")
    static List<Long> fromEntityToView(List<IngredientEntity> ingredients) {
        return ingredients.stream().map(IngredientEntity::getId).toList();
    }
}