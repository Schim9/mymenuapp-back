package lu.perso.menuback.controller;

import lu.perso.menuback.constant.MenuEnum;
import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.models.Dish;
import lu.perso.menuback.models.Ingredient;
import lu.perso.menuback.repository.DishRepository;
import lu.perso.menuback.repository.IngredientRepository;
import lu.perso.menuback.services.DatabaseServices;
import lu.perso.menuback.services.DishServices;
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
@RequestMapping("/dishes")
public class DishController {
    @Autowired
    DishServices dishServices;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    DishRepository dishRepository;
    @Autowired
    DatabaseServices databaseServices;

    @GetMapping("")
    public ResponseEntity<List<Dish>> findAllDishes() {
        List<Dish> allDishes = dishServices.getAllDishes();
        return new ResponseEntity<>(allDishes, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> createDish(@RequestBody Dish dishParam) {
        try {
            // Check if dish already exists
            if (!Objects.isNull(dishRepository.findByName(dishParam.name()))) {
                return new ResponseEntity<>(dishParam.name() + " does already exist", HttpStatus.ALREADY_REPORTED);
            }
            // Get Ingredients from database
            // In case one of them does not exist, an error is raised
            var recipe = dishParam.recipe()
                    .stream()
                    .map(
                            ingredient ->
                                    ingredientRepository.findById(ingredient.id())
                                            .orElseThrow(() -> new IllegalStateException("This ingredient does not exist [" + ingredient.name() + "]"))
                    ).collect(Collectors.toList());

            DishEntity newDish = new DishEntity(
                    databaseServices.generateSequence(MenuEnum.SEQUENCE_TYPE.DISHES),
                    StringUtils.capitalize(dishParam.name()),
                    recipe
            );
            DishEntity savedObject = dishRepository.save(newDish);
            return new ResponseEntity<>(savedObject.name() + " has been created", HttpStatus.CREATED);
        } catch (IllegalStateException ise) {
            return new ResponseEntity<>(ise.getMessage(), HttpStatus.BAD_REQUEST);
        }  catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<Long> updateDish(@RequestBody Dish dishParam) {
        try {
            // Check if dish does exist
            if (dishRepository.findById(dishParam.id()).isEmpty()) {
                return new ResponseEntity<>(-1L, HttpStatus.BAD_REQUEST);
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
                    dishParam.id(),
                    StringUtils.capitalize(dishParam.name()),
                    recipe
            );
            dishRepository.save(newDish);
            return new ResponseEntity<>(newDish.id(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Long> deleteDish(@RequestBody Dish dishParam) {
        try {
            // Check if dish does exist
            if (dishRepository.findById(dishParam.id()).isEmpty()) {
                return new ResponseEntity<>(-1L, HttpStatus.BAD_REQUEST);
            }
            // TODO ERK: Check dish does not exist in any menu


            dishRepository.deleteById(dishParam.id());
            return new ResponseEntity<>(dishParam.id(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}






