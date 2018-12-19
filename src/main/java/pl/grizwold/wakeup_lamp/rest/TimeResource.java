package pl.grizwold.wakeup_lamp.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/time")
public class TimeResource {

    @GetMapping
    public LocalDateTime getTime() {
        log.info("Getting local time");
        return LocalDateTime.now();
    }

    @PutMapping
    public ResponseEntity<String> updateTime(@RequestBody String time) {
        try {
            log.info("Updating system time to {}", time);
            Runtime.getRuntime().exec(String.format("date -s '%s'", time));
        } catch (IOException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok(LocalDateTime.now().toString());
    }


}
