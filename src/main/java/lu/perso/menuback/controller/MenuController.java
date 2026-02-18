package lu.perso.menuback.controller;

import lu.perso.menuback.models.Menu;
import lu.perso.menuback.services.MenuServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {

    private final MenuServices menuServices;

    public MenuController(MenuServices menuServices) {
        this.menuServices = menuServices;
    }

    @GetMapping("")
    public ResponseEntity<List<Menu>> findAllMenus() {
        List<Menu> allMenus = menuServices.getAllMenus(LocalDate.now());
        return new ResponseEntity<>(allMenus, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> createMenu(@RequestBody Menu menuParam) {
        Menu createdMenu = menuServices.createMenu(menuParam);
        return new ResponseEntity<>(createdMenu.name() + " has been created", HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<LocalDate> updateMenu(@RequestBody Menu menuParam) {
        menuServices.handleMenuUpdate(menuParam);
        return new ResponseEntity<>(menuParam.date(), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<LocalDate> deleteMenu(@RequestBody Menu menuParam) {
        menuServices.deleteMenu(menuParam);
        return new ResponseEntity<>(menuParam.date(), HttpStatus.OK);
    }
}
