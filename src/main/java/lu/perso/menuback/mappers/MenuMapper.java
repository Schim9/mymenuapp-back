package lu.perso.menuback.mappers;

import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.data.IngredientEntity;
import lu.perso.menuback.data.MenuEntity;
import lu.perso.menuback.models.Menu;
import lu.perso.menuback.models.MenuItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuMapper {
    private final DishMapper dishMapper;
    private final IngredientMapper ingredientMapper;

    public MenuMapper(DishMapper dishMapper, IngredientMapper ingredientMapper) {
        this.dishMapper = dishMapper;
        this.ingredientMapper = ingredientMapper;
    }

    public Menu toView(MenuEntity entity) {
        List<MenuItem> lunchMeals = entity.getLunchMeals()
                .stream()
                .<MenuItem>map(meal -> switch (meal) {
                    case DishEntity dish -> dishMapper.toView(dish);
                    case IngredientEntity ingredient -> ingredientMapper.toView(ingredient);
                    default -> throw new IllegalStateException("Unknown meal type");
                })
                .toList();
        List<MenuItem> dinnerMeals = entity.getDinnerMeals()
                .stream()
                .<MenuItem>map(meal -> switch (meal) {
                    case DishEntity dish -> dishMapper.toView(dish);
                    case IngredientEntity ingredient -> ingredientMapper.toView(ingredient);
                    default -> throw new IllegalStateException("Unknown meal type");
                })
                .toList();

        return new Menu(
                entity.getName(),
                entity.getDate(),
                lunchMeals,
                dinnerMeals
                );
    }

}
