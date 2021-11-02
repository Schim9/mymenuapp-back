package lu.perso.menuback.controller;

import lu.perso.menuback.models.Ingredient;
import lu.perso.menuback.services.IngredientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    @Autowired
    IngredientServices ingredientServices;

    @GetMapping("")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<Ingredient> collect = ingredientServices.getAllIngredients();
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient ingredientParam) {
        try {
            Ingredient createdIngredient = ingredientServices.createIngredient(ingredientParam);
            return new ResponseEntity<>(createdIngredient, HttpStatus.CREATED);
        } catch (IllegalStateException ise) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<Long> updateIngredient(@RequestBody Ingredient ingredientParam) {
        try {
           ingredientServices.updateIngredient(ingredientParam);
            return new ResponseEntity<>(ingredientParam.id(), HttpStatus.OK);
        } catch (IllegalStateException ise) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Long> deleteIngredient(@RequestBody Ingredient ingredientParam) {
        try {
            // Check if ingredient does exist
            ingredientServices.deleteIngredient(ingredientParam);
            return new ResponseEntity<>(ingredientParam.id(), HttpStatus.OK);
        } catch (IllegalStateException ise) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}






