package lu.perso.menuback.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class AdminController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> mapResponse = Map.of(
                "appCode", "MY_MENU_APP",
                "status", "up",
                "detail", "MenuApp-back is up at " + LocalDateTime.now()
        );
        return new ResponseEntity<>(mapResponse, HttpStatus.OK);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<Map<String, String>> authenticate() {
        return ResponseEntity.ok(Map.of("status", "authorized"));
    }
}
