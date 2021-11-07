package lu.perso.menuback.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class AdminController {

    @GetMapping("/health-check")
    public ResponseEntity<String> createDish() {
        return new ResponseEntity<>("MenuApp-back is up at " + LocalDateTime.now(), HttpStatus.OK);
    }
}
