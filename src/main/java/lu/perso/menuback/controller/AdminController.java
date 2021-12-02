package lu.perso.menuback.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @SuppressWarnings("rawtypes")
    @GetMapping("/status")
    public ResponseEntity healthCheck() {
        var mapResponse = new HashMap<>();
        mapResponse.put("appCode", "MY_MENU_APP");
        mapResponse.put("status", "up");
        mapResponse.put("detail", "MenuApp-back is up at " + LocalDateTime.now());
        return new ResponseEntity<>(
                mapResponse,
                HttpStatus.OK
        );
    }
}
