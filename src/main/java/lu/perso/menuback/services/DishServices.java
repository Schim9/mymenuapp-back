package lu.perso.menuback.services;

import lu.perso.menuback.constant.MenuEnum;
import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.models.Dish;
import lu.perso.menuback.models.Ingredient;
import lu.perso.menuback.repository.DishRepository;
import lu.perso.menuback.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DishServices {
    @Autowired
    DishRepository dishRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    DatabaseServices databaseServices;

    public List<Dish> getAllDishes() {
        List<DishEntity> dishList = dishRepository.findAll();
        return dishList.stream()
                .map(element -> {
                    List<Ingredient> ingredients = element.recipe().stream()
                            .map(ingredientEntity -> new Ingredient(
                                            ingredientEntity.id(),
                                            ingredientEntity.name(),
                                            ingredientEntity.sectionId(),
                                            ingredientEntity.unit()
                                    )
                            )
                            .collect(Collectors.toList());
                    return new Dish(element.id(), element.name(), ingredients);
                })
                .collect(Collectors.toList());
    }

    public Dish createDish(Dish newDish) throws IllegalStateException {
        // Check if dish already exists
        if (!Objects.isNull(dishRepository.findByName(newDish.name()))) {
           throw new IllegalStateException(newDish.name() + " does already exist");
        }
        // Get Ingredients from database
        // In case one of them does not exist, an error is raised
        var recipe = newDish.recipe()
                .stream()
                .map(
                        ingredient ->
                                ingredientRepository.findById(ingredient.id())
                                        .orElseThrow(() -> new IllegalStateException("This ingredient does not exist [" + ingredient.name() + "]"))
                ).collect(Collectors.toList());

        DishEntity createdDish = new DishEntity(
                databaseServices.generateSequence(MenuEnum.SEQUENCE_TYPE.DISHES),
                StringUtils.capitalize(newDish.name()),
                recipe
        );
        DishEntity savedObject = dishRepository.save(createdDish);
        return newDish;
    }
}
