package lu.perso.menuback.controller;

import lu.perso.menuback.models.Dish;
import lu.perso.menuback.services.DishServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishServices dishServices;
    private final Logger logger = LogManager.getLogger(getClass());

    public DishController(DishServices dishServices) {
        this.dishServices = dishServices;
    }

    @GetMapping("")
    public ResponseEntity<List<Dish>> findAllDishes() {
        logger.info("call findAllDishes");
        List<Dish> allDishes = dishServices.getAllDishes();
        return new ResponseEntity<>(allDishes, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Dish> createDish(@RequestBody Dish dishParam) {
        Dish createdDish = dishServices.createDish(dishParam);
        return new ResponseEntity<>(createdDish, HttpStatus.CREATED);
    }

    @PutMapping("/{dishId}")
    public ResponseEntity<Long> updateDish(@RequestBody Dish dishParam, @PathVariable final Long dishId) {
        dishServices.updateDish(dishId, dishParam);
        return new ResponseEntity<>(dishParam.id(), HttpStatus.OK);
    }

    @DeleteMapping("/{dishId}")
    public ResponseEntity<Long> deleteDish(@PathVariable final Long dishId) {
        dishServices.deleteDish(dishId);
        return new ResponseEntity<>(dishId, HttpStatus.OK);
    }
}
