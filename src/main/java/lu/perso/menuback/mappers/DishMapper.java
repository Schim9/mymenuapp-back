package lu.perso.menuback.mappers;

import lu.perso.menuback.constant.MenuEnum;
import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.data.IngredientEntity;
import lu.perso.menuback.models.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DishMapper {

    @Mapping(source = "recipe", target = "ingredients",qualifiedByName = "CustomMapperToEntity")
    DishEntity toEntity(Dish view);

    @Mapping(source = "ingredients", target = "recipe",qualifiedByName = "CustomMapperToView")
    Dish toView(DishEntity entity);


    @Named("CustomMapperToView")
    static List<Long> fromEntityToView(List<IngredientEntity> recipe) {
        return recipe.stream().map(IngredientEntity::id).collect(Collectors.toList());
    }

    @Named("CustomMapperToEntity")
    static List<IngredientEntity> fromViewToEntity(List<Long> recipe) {
        return recipe.stream()
                .map(ingredientId -> new IngredientEntity(ingredientId, "", 0L, MenuEnum.UNIT.PIECE))
                .collect(Collectors.toList());
    }
}