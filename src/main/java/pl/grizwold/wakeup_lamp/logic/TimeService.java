package pl.grizwold.wakeup_lamp.logic;

import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class TimeService {
    public LocalTime now() {
        return LocalTime.now();
    }
}
