package lu.perso.menuback.controller;

import lu.perso.menuback.models.Dish;
import lu.perso.menuback.services.DishServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/dishes")
public class DishController {
    @Autowired
    DishServices dishServices;

    @GetMapping("")
    public ResponseEntity<List<Dish>> findAllDishes() {
        List<Dish> allDishes = dishServices.getAllDishes();
        return new ResponseEntity<>(allDishes, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> createDish(@RequestBody Dish dishParam) {
        try {
            Dish createdDish = dishServices.createDish(dishParam);
            return new ResponseEntity<>(createdDish.name() + " has been created", HttpStatus.CREATED);
        } catch (IllegalStateException ise) {
            return new ResponseEntity<>(ise.getMessage(), HttpStatus.BAD_REQUEST);
        }  catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<Long> updateDish(@RequestBody Dish dishParam) {
        try {
            dishServices.updateDish(dishParam);
            return new ResponseEntity<>(dishParam.id(), HttpStatus.OK);
        } catch (IllegalStateException ise) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Long> deleteDish(@RequestBody Dish dishParam) {
        try {
            dishServices.deleteDish(dishParam);
            return new ResponseEntity<>(dishParam.id(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}






