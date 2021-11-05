package lu.perso.menuback.mappers;

import lu.perso.menuback.data.IngredientEntity;
import lu.perso.menuback.models.Ingredient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    Ingredient toView(IngredientEntity source);
    IngredientEntity toEntity(Ingredient destination);
}