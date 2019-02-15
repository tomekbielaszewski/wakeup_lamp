package pl.grizwold.wakeup_lamp.logic;

import org.junit.Test;
import pl.grizwold.wakeup_lamp.Starter;
import pl.grizwold.wakeup_lamp.model.WakeUpDay;
import pl.grizwold.wakeup_lamp.model.WakeUpWeek;

import java.time.Duration;
import java.time.LocalTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WakeUpServiceTest {
    private WakeUpService wakeUpService = new WakeUpService();

    @Test
    public void returns_default_config_when_nothing_set_before() {
        WakeUpWeek wakeUpWeek = wakeUpService.getWakeUpWeek();

        assertThat(wakeUpWeek, is(Starter.DEFAULT_WAKEUP));
    }

    @Test
    public void sets_valid_wakeup_week() {
        WakeUpWeek wakeUpWeek = WakeUpWeek.builder()
                .workDay(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now().plusSeconds(1))
                        .build())
                .weekend(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now().plusSeconds(1))
                        .build())
                .dimDelay(Duration.ofMinutes(10))
                .dimDuration(Duration.ofMinutes(10))
                .build();

        wakeUpService.updateWakeUpWeek(wakeUpWeek);

        assertThat(wakeUpWeek, is(wakeUpService.getWakeUpWeek()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validation_fails_when_start_equals_end_in_workday() {
        WakeUpWeek wakeUpWeek = WakeUpWeek.builder()
                .workDay(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now())
                        .build())
                .weekend(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now().plusSeconds(1))
                        .build())
                .dimDelay(Duration.ofMinutes(10))
                .dimDuration(Duration.ofMinutes(10))
                .build();

        wakeUpService.updateWakeUpWeek(wakeUpWeek);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validation_fails_when_start_equals_end_in_weekend() {
        WakeUpWeek wakeUpWeek = WakeUpWeek.builder()
                .workDay(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now().plusSeconds(1))
                        .build())
                .weekend(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now())
                        .build())
                .dimDelay(Duration.ofMinutes(10))
                .dimDuration(Duration.ofMinutes(10))
                .build();

        wakeUpService.updateWakeUpWeek(wakeUpWeek);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validation_fails_when_dim_delay_is_negative() {
        WakeUpWeek wakeUpWeek = WakeUpWeek.builder()
                .workDay(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now().plusSeconds(1))
                        .build())
                .weekend(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now().plusSeconds(1))
                        .build())
                .dimDelay(Duration.ofMinutes(-10))
                .dimDuration(Duration.ofMinutes(10))
                .build();

        wakeUpService.updateWakeUpWeek(wakeUpWeek);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validation_fails_when_dim_duration_is_negative() {
        WakeUpWeek wakeUpWeek = WakeUpWeek.builder()
                .workDay(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now().plusSeconds(1))
                        .build())
                .weekend(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now().plusSeconds(1))
                        .build())
                .dimDelay(Duration.ofMinutes(10))
                .dimDuration(Duration.ofMinutes(-10))
                .build();

        wakeUpService.updateWakeUpWeek(wakeUpWeek);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validation_fails_when_end_is_before_start_in_workday() {
        WakeUpWeek wakeUpWeek = WakeUpWeek.builder()
                .workDay(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now().minusSeconds(1))
                        .build())
                .weekend(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now().plusSeconds(1))
                        .build())
                .dimDelay(Duration.ofMinutes(10))
                .dimDuration(Duration.ofMinutes(10))
                .build();

        wakeUpService.updateWakeUpWeek(wakeUpWeek);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validation_fails_when_end_is_before_start_in_weekend() {
        WakeUpWeek wakeUpWeek = WakeUpWeek.builder()
                .workDay(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now().plusSeconds(1))
                        .build())
                .weekend(WakeUpDay.builder()
                        .start(LocalTime.now())
                        .end(LocalTime.now().minusSeconds(1))
                        .build())
                .dimDelay(Duration.ofMinutes(10))
                .dimDuration(Duration.ofMinutes(10))
                .build();

        wakeUpService.updateWakeUpWeek(wakeUpWeek);
    }
}
