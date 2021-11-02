package lu.perso.menuback.controller;

import lu.perso.menuback.constant.MenuEnum;
import lu.perso.menuback.data.IngredientEntity;
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
@RequestMapping("/ingredients")
public class IngredientController {
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    DishRepository dishRepository;
    @Autowired
    DatabaseServices databaseServices;

    @GetMapping("")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<IngredientEntity> ingredientList = ingredientRepository.findAll();
        List<Ingredient> collect = ingredientList.stream()
                .map(element -> new Ingredient(element.id(), element.name(), element.sectionId(), element.unit()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> createIngredient(@RequestBody Ingredient ingredientParam) {
        try {
            // Check if ingredient already exists
            if (!Objects.isNull(ingredientRepository.findByName(ingredientParam.name()))) {
                return new ResponseEntity<>(-1L, HttpStatus.ALREADY_REPORTED);
            }
            IngredientEntity newIngredient = new IngredientEntity(
                    databaseServices.generateSequence(MenuEnum.SEQUENCE_TYPE.INGREDIENTS),
                    StringUtils.capitalize(ingredientParam.name()),
                    ingredientParam.sectionId(),
                    ingredientParam.unit()
            );
            IngredientEntity savedObject = ingredientRepository.save(newIngredient);
            return new ResponseEntity<>(savedObject.id(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<Long> updateIngredient(@RequestBody Ingredient ingredientParam) {
        try {
            // Check if ingredient does exist
            if (ingredientRepository.findById(ingredientParam.id()).isEmpty()) {
                return new ResponseEntity<>(-1L, HttpStatus.BAD_REQUEST);
            }

            IngredientEntity newIngredient = new IngredientEntity(
                    ingredientParam.id(),
                    StringUtils.capitalize(ingredientParam.name()),
                    ingredientParam.sectionId(),
                    ingredientParam.unit()
            );
            ingredientRepository.save(newIngredient);
            return new ResponseEntity<>(ingredientParam.id(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Long> deleteIngredient(@RequestBody Ingredient ingredientParam) {
        try {
            // Check if ingredient does exist
            if (ingredientRepository.findById(ingredientParam.id()).isEmpty()) {
                return new ResponseEntity<>(-1L, HttpStatus.BAD_REQUEST);
            }
            // TODO ERK: Check ingredient does not exist in any recipes


            ingredientRepository.deleteById(ingredientParam.id());
            return new ResponseEntity<>(ingredientParam.id(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}






