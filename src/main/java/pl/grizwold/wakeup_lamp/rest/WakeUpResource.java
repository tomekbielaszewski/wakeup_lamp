package pl.grizwold.wakeup_lamp.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.grizwold.wakeup_lamp.logic.RaspberryPi;
import pl.grizwold.wakeup_lamp.logic.WakeUpService;
import pl.grizwold.wakeup_lamp.model.WakeUpWeek;

@Slf4j
@RestController
@RequestMapping("/wakeup")
public class WakeUpResource {

    @Autowired
    private WakeUpService wakeUpService;

    @Autowired
    private RaspberryPi raspberryPi;

    @GetMapping
    public WakeUpWeek getWakeUpConfig() {
        log.info("Getting current wake up config");
        return wakeUpService.getWakeUpWeek();
    }

    @GetMapping("/{pwm}")
    public void overwritePWM(@PathVariable("pwm") int pwm) {
        log.info("Setting PWM to {}", pwm);
        raspberryPi.setPWM(pwm);
    }

    @PutMapping
    public WakeUpWeek updateWakeUpConfig(@RequestBody WakeUpWeek wakeUpWeek) {
        log.info("Updating wake up config to {}", wakeUpWeek);
        return wakeUpService.updateWakeUpWeek(wakeUpWeek);
    }

    @PutMapping("/default")
    public WakeUpWeek defaultWakeUpConfig() {
        log.info("Setting wake up config to default");
        return wakeUpService.setDefaultWakeUpWeek();
    }
}