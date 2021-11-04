package lu.perso.menuback.mappers;

import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.models.Dish;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DishMapper {
    Dish toView(DishEntity entity);
    DishEntity toEntity(Dish view);
}