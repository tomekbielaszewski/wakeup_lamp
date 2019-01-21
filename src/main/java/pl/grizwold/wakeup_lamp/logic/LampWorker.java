package pl.grizwold.wakeup_lamp.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.grizwold.wakeup_lamp.model.WakeUpDay;

import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.SECONDS;
import static pl.grizwold.wakeup_lamp.logic.RaspberryPi.MAX_PWM_RATE;

@Slf4j
@Component
public class LampWorker {
    @Autowired
    private WakeUpService wakeUpService;

    @Autowired
    private RaspberryPi raspberryPi;

    @Autowired
    private TimeService time;

    @Scheduled(fixedRate = 1000)
    public void scheduled() {
        WakeUpDay todayWakeUp = wakeUpService.getTodayWakeUpDay();

        whenMorningComes(todayWakeUp);
        whenMorningEnds(todayWakeUp);
    }

    private void whenMorningComes(WakeUpDay todayWakeUp) {
        LocalTime wakeUpStart = todayWakeUp.getStart();
        LocalTime wakeUpEnd = todayWakeUp.getEnd();

        if (isBetween(wakeUpStart, wakeUpEnd)) {
            smoothlyLightUpLamp(wakeUpStart, wakeUpEnd);
        }
    }

    private void whenMorningEnds(WakeUpDay todayWakeUp) {
        LocalTime dimStart = getDimStart(todayWakeUp);
        LocalTime dimStop = getDimStop(todayWakeUp);

        if (isBetween(dimStart, dimStop)) {
            smoothlyDimLamp(dimStart, dimStop);
        }
    }

    private void smoothlyLightUpLamp(LocalTime start, LocalTime end) {
        int pwmForThisSecond = calculateLightPower(start, end);

        log.info("Light power: {}/{}", pwmForThisSecond, MAX_PWM_RATE);
        raspberryPi.setPWM(pwmForThisSecond);
    }

    private void smoothlyDimLamp(LocalTime start, LocalTime end) {
        int pwmForThisSecond = MAX_PWM_RATE - calculateLightPower(start, end);

        log.info("Light power: {}/{}", pwmForThisSecond, MAX_PWM_RATE);
        raspberryPi.setPWM(pwmForThisSecond);
    }

    private int calculateLightPower(LocalTime start, LocalTime end) {
        long secondsInTotal = SECONDS.between(start, end);
        long secondsElapsed = SECONDS.between(start, time.now());

        return (int) (((float) secondsElapsed / (float) secondsInTotal) * MAX_PWM_RATE);
    }

    private LocalTime getDimStop(WakeUpDay todayWakeUp) {
        return this.getDimStart(todayWakeUp).plus(wakeUpService.getDimDuration());
    }

    private LocalTime getDimStart(WakeUpDay todayWakeUp) {
        return todayWakeUp.getEnd().plus(wakeUpService.getDimDelay());
    }

    private boolean isBetween(LocalTime start, LocalTime end) {
        LocalTime now = time.now();
        return now.compareTo(start) == 0 ||
                (now.isAfter(start) &&
                        now.isBefore(end));
    }
}
