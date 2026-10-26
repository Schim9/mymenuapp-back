package lu.perso.menuback.controller;

import lu.perso.menuback.models.Ingredient;
import lu.perso.menuback.services.IngredientServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientServices ingredientServices;
    private final Logger logger = LogManager.getLogger(getClass());

    public IngredientController(IngredientServices ingredientServices) {
        this.ingredientServices = ingredientServices;
    }

    @GetMapping("")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        logger.info("call getAllIngredients");
        List<Ingredient> collect = ingredientServices.getAllIngredients();
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient ingredientParam) {
        logger.info("call createIngredient — {}", ingredientParam.name());
        Ingredient createdIngredient = ingredientServices.createIngredient(ingredientParam);
        return new ResponseEntity<>(createdIngredient, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{ingredientId}")
    public ResponseEntity<Long> updateIngredient(@RequestBody Ingredient ingredientParam, @PathVariable final Long ingredientId) {
        logger.info("call updateIngredient — id={}", ingredientId);
        ingredientServices.updateIngredient(ingredientId, ingredientParam);
        return new ResponseEntity<>(ingredientParam.id(), HttpStatus.OK);
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Long> deleteIngredient(@PathVariable final Long ingredientId) {
        logger.info("call deleteIngredient — id={}", ingredientId);
        ingredientServices.deleteIngredient(ingredientId);
        return new ResponseEntity<>(ingredientId, HttpStatus.OK);
    }
}
