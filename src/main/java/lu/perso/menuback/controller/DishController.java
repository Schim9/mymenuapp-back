package lu.perso.menuback.controller;

import lu.perso.menuback.constant.MenuEnum;
import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.data.IngredientEntity;
import lu.perso.menuback.models.Dish;
import lu.perso.menuback.models.Ingredient;
import lu.perso.menuback.repository.DishRepository;
import lu.perso.menuback.repository.IngredientRepository;
import lu.perso.menuback.services.DatabaseServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/dishes")
public class DishController {
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    DishRepository dishRepository;
    @Autowired
    DatabaseServices databaseServices;

    @GetMapping("")
    public ResponseEntity<List<Dish>> findAllDishes() {
        List<DishEntity> dishList = dishRepository.findAll();
        List<Dish> collect = dishList.stream()
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
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> createDish(@RequestBody Dish dishParam) {
        try {
            // Check if dish already exists
            if (!Objects.isNull(dishRepository.findByName(dishParam.name()))) {
                return new ResponseEntity<>(-1L, HttpStatus.ALREADY_REPORTED);
            }
            // Get Ingredients from database
            // In case one of them does not exist, an error is raised
            var recipe = dishParam.recipe().stream()
                    .map(
                            ingredient ->
                                    ingredientRepository.findById(ingredient.id())
                                            .orElseThrow(IllegalStateException::new)
                    ).collect(Collectors.toList());

            DishEntity newDish = new DishEntity(
                    databaseServices.generateSequence(MenuEnum.SEQUENCE_TYPE.DISHES),
                    StringUtils.capitalize(dishParam.name()),
                    recipe
            );
            DishEntity savedObject = dishRepository.save(newDish);
            return new ResponseEntity<>(savedObject.id(), HttpStatus.CREATED);
        } catch (IllegalStateException ise) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }  catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // TODO: UPDATE

    // TODO DELETE (check in menus the dish is not planed
}






