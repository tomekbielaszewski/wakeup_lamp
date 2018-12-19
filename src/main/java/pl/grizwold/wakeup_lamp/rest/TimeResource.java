package pl.grizwold.wakeup_lamp.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/time")
public class TimeResource {

    @GetMapping
    public LocalDateTime getTime() {
        return LocalDateTime.now();
    }

    @PutMapping
    public ResponseEntity<String> updateTime(@RequestBody String time) {
        try {
            Runtime.getRuntime().exec(String.format("date -s '%s'", time));
        } catch (IOException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok(LocalDateTime.now().toString());
    }


}
