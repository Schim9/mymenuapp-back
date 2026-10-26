package lu.perso.menuback.controller;

import lu.perso.menuback.models.Menu;
import lu.perso.menuback.services.MenuServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {

    private final MenuServices menuServices;
    private final Logger logger = LogManager.getLogger(getClass());

    public MenuController(MenuServices menuServices) {
        this.menuServices = menuServices;
    }

    @GetMapping("")
    public ResponseEntity<List<Menu>> findAllMenus() {
        logger.info("call findAllMenus");
        List<Menu> allMenus = menuServices.getAllMenus(LocalDate.now());
        return new ResponseEntity<>(allMenus, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Menu> updateMenu(@RequestBody Menu menuParam) {
        logger.info("call updateMenu — {}", menuParam.date());
        Menu savedMenu = menuServices.handleMenuUpdate(menuParam);
        return new ResponseEntity<>(savedMenu, HttpStatus.OK);
    }

    @DeleteMapping("/{date}")
    public ResponseEntity<LocalDate> deleteMenu(@PathVariable LocalDate date) {
        logger.info("call deleteMenu — {}", date);
        menuServices.deleteMenu(date);
        return new ResponseEntity<>(date, HttpStatus.OK);
    }
}
