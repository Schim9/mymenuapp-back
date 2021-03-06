package lu.perso.menuback.controller;

import lu.perso.menuback.models.Menu;
import lu.perso.menuback.services.MenuServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://menu-app.kaminski.lu"})
@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    MenuServices menuServices;

    @GetMapping("")
    public ResponseEntity<List<Menu>> findAllMenus() {
        List<Menu> allMenus = menuServices.getAllMenus(LocalDate.now());
        return new ResponseEntity<>(allMenus, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> createMenu(@RequestBody Menu menuParam) {
        try {
            Menu createdDish = menuServices.createMenu(menuParam);
            return new ResponseEntity<>(createdDish.name() + " has been created", HttpStatus.CREATED);
        } catch (IllegalStateException ise) {
            return new ResponseEntity<>(ise.getMessage(), HttpStatus.BAD_REQUEST);
        }  catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<LocalDate> updateMenu(@RequestBody Menu menuParam) {
        try {
            menuServices.handleMenuUpdate(menuParam);
            return new ResponseEntity<>(menuParam.date(), HttpStatus.OK);
        } catch (IllegalStateException ise) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<LocalDate> deleteMenu(@RequestBody Menu menuParam) {
        try {
            menuServices.deleteMenu(menuParam);
            return new ResponseEntity<>(menuParam.date(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
