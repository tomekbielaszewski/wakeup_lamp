package pl.grizwold.wakeup_lamp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.grizwold.wakeup_lamp.logic.RaspberryPi;
import pl.grizwold.wakeup_lamp.logic.WakeUpService;
import pl.grizwold.wakeup_lamp.model.WakeUpWeek;

@RestController
@RequestMapping("/wakeup")
public class WakeUpResource {

    @Autowired
    private WakeUpService wakeUpService;

    @Autowired
    private RaspberryPi raspberryPi;

    @GetMapping
    public WakeUpWeek getWakeUpConfig() {
        return wakeUpService.getWakeUpWeek();
    }

    @GetMapping("/{pwm}")
    public void overwritePWM(@PathVariable("pwm") int pwm) {
        raspberryPi.setPWM(pwm);
    }

    @PutMapping
    public WakeUpWeek updateWakeUpConfig(@RequestBody WakeUpWeek wakeUpWeek) {
        return wakeUpService.updateWakeUpWeek(wakeUpWeek);
    }

    @PutMapping("/default")
    public WakeUpWeek defaultWakeUpConfig() {
        return wakeUpService.setDefaultWakeUpWeek();
    }
}