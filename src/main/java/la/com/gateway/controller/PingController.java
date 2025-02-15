package la.com.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping("/public/ping")
    public ResponseEntity<String> ping(){
        return ResponseEntity.ok("Gateway Pong");
    }
}
