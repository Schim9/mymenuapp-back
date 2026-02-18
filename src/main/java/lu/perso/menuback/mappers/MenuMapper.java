package lu.perso.menuback.mappers;

import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.data.IngredientEntity;
import lu.perso.menuback.data.MenuEntity;
import lu.perso.menuback.data.MenuItemEntity;
import lu.perso.menuback.models.Dish;
import lu.perso.menuback.models.Ingredient;
import lu.perso.menuback.models.Menu;
import lu.perso.menuback.models.MenuItem;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class MenuMapper {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    IngredientMapper ingredientMapper;

    public Menu toView(MenuEntity entity) {
        List<MenuItem> lunchMeals = entity.getLunchMeals()
                .stream()
                .map(meal -> switch (meal) {
                    case DishEntity dish -> dishMapper.toView(dish);
                    case IngredientEntity ingredient -> ingredientMapper.toView(ingredient);
                    default -> throw new IllegalStateException("Unknown meal type");
                })
                .collect(Collectors.toList());
        List<MenuItem> dinnerMeals = entity.getDinnerMeals()
                .stream()
                .map(meal -> switch (meal) {
                    case DishEntity dish -> dishMapper.toView(dish);
                    case IngredientEntity ingredient -> ingredientMapper.toView(ingredient);
                    default -> throw new IllegalStateException("Unknown meal type");
                })
                .collect(Collectors.toList());

        return new Menu(
                entity.getName(),
                entity.getDate(),
                lunchMeals,
                dinnerMeals
                );
    }

    public MenuEntity toEntity(Menu view) {
        List<MenuItemEntity> lunchMeals = view.lunchMeals()
                .stream()
                .map(meal -> switch (meal) {
                    case Dish dish -> dishMapper.toEntity(dish);
                    case Ingredient ingredient -> ingredientMapper.toEntity(ingredient);
                })
                .collect(Collectors.toList());
        List<MenuItemEntity> dinnerMeals = view.dinnerMeals()
                .stream()
                .map(meal -> switch (meal) {
                    case Dish dish -> dishMapper.toEntity(dish);
                    case Ingredient ingredient -> ingredientMapper.toEntity(ingredient);
                })
                .collect(Collectors.toList());

        return new MenuEntity(
                null,
                view.name(),
                view.date(),
                lunchMeals,
                dinnerMeals
        );
    }
}
