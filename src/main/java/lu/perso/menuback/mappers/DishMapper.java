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

    @Mapping(source = "ingredients", target = "ingredients",qualifiedByName = "CustomMapperToEntity")
    DishEntity toEntity(Dish view);

    @Mapping(source = "ingredients", target = "ingredients",qualifiedByName = "CustomMapperToView")
    Dish toView(DishEntity entity);


    @Named("CustomMapperToView")
    static List<Long> fromEntityToView(List<IngredientEntity> ingredients) {
        return ingredients.stream().map(IngredientEntity::getId).collect(Collectors.toList());
    }

    @Named("CustomMapperToEntity")
    static List<IngredientEntity> fromViewToEntity(List<Long> ingredients) {
        return ingredients.stream()
                .map(ingredientId -> new IngredientEntity(ingredientId, "", 0L, MenuEnum.UNIT.PIECE, null))
                .collect(Collectors.toList());
    }
}