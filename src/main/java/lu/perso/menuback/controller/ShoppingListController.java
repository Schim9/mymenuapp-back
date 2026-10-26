package lu.perso.menuback.controller;

import lu.perso.menuback.services.ShoppingListServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping-list")
public class ShoppingListController {

    private final ShoppingListServices shoppingListServices;
    private final Logger logger = LogManager.getLogger(getClass());

    public ShoppingListController(ShoppingListServices shoppingListServices) {
        this.shoppingListServices = shoppingListServices;
    }

    @GetMapping("")
    public ResponseEntity<List<String>> getItems() {
        logger.info("call getItems");
        return new ResponseEntity<>(shoppingListServices.getItems(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> addItems(@RequestBody List<String> names) {
        logger.info("call addItems — {} item(s)", names.size());
        shoppingListServices.addItems(names);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("")
    public ResponseEntity<List<String>> consumeItems() {
        logger.info("call consumeItems");
        return new ResponseEntity<>(shoppingListServices.consumeItems(), HttpStatus.OK);
    }
}
