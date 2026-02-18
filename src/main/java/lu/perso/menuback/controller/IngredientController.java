package lu.perso.menuback.controller;

import lu.perso.menuback.models.Ingredient;
import lu.perso.menuback.services.IngredientServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientServices ingredientServices;

    public IngredientController(IngredientServices ingredientServices) {
        this.ingredientServices = ingredientServices;
    }

    @GetMapping("")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<Ingredient> collect = ingredientServices.getAllIngredients();
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient ingredientParam) {
        Ingredient createdIngredient = ingredientServices.createIngredient(ingredientParam);
        return new ResponseEntity<>(createdIngredient, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{ingredientId}")
    public ResponseEntity<Long> updateIngredient(@RequestBody Ingredient ingredientParam, @PathVariable final Long ingredientId) {
        ingredientServices.updateIngredient(ingredientId, ingredientParam);
        return new ResponseEntity<>(ingredientParam.id(), HttpStatus.OK);
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Long> deleteIngredient(@PathVariable final Long ingredientId) {
        ingredientServices.deleteIngredient(ingredientId);
        return new ResponseEntity<>(ingredientId, HttpStatus.OK);
    }
}
