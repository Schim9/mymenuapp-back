package lu.perso.menuback.controller;

import lu.perso.menuback.util.DateUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class AdminController {

    private final DateUtil dateUtil;

    public AdminController(DateUtil dateUtil) {
        this.dateUtil = dateUtil;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("appCode", "MY_MENU_APP");
        body.put("status", "up");
        body.put("uptime", dateUtil.formatUptime());
        body.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<Map<String, String>> authenticate() {
        return ResponseEntity.ok(Map.of("status", "authorized"));
    }
}
