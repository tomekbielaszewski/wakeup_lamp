package pl.grizwold.wakeup_lamp.logic;

import org.springframework.stereotype.Service;
import pl.grizwold.wakeup_lamp.model.WakeUpDay;
import pl.grizwold.wakeup_lamp.model.WakeUpWeek;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

import static pl.grizwold.wakeup_lamp.Starter.DEFAULT_WAKEUP;

@Service
public class WakeUpService {
    private WakeUpWeek wakeUpWeek;

    public WakeUpWeek getWakeUpWeek() {
        return Optional.ofNullable(wakeUpWeek)
                .orElse(DEFAULT_WAKEUP);
    }

    public WakeUpWeek updateWakeUpWeek(WakeUpWeek wakeUpWeek) {
        return this.wakeUpWeek = wakeUpWeek;
    }

    public WakeUpWeek setDefaultWakeUpWeek() {
        return this.wakeUpWeek = DEFAULT_WAKEUP;
    }

    public WakeUpDay getTodayWakeUpDay() {
        WakeUpWeek wakeUpWeek = this.getWakeUpWeek();
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        boolean isWeekend = DayOfWeek.SATURDAY.equals(dayOfWeek) || DayOfWeek.SUNDAY.equals(dayOfWeek);
        return isWeekend ? wakeUpWeek.getWeekend() : wakeUpWeek.getWorkDay();
    }

    public Duration getDimDelay() {
        return this.getWakeUpWeek().getDimDelay();
    }

    public Duration getDimDuration() {
        return this.getWakeUpWeek().getDimDuration();
    }
}
