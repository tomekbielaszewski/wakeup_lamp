package pl.grizwold.wakeup_lamp.logic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.grizwold.wakeup_lamp.model.WakeUpDay;

import java.time.Duration;
import java.time.LocalTime;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static pl.grizwold.wakeup_lamp.logic.RaspberryPi.MAX_PWM_RATE;
import static pl.grizwold.wakeup_lamp.matchers.IsCloseToInteger.closeTo;

@RunWith(MockitoJUnitRunner.class)
public class LampWorkerTest {
    @Mock
    private WakeUpService wakeUpService;

    @Mock
    private RaspberryPi raspberryPi;

    @Mock
    private TimeService time;

    @InjectMocks
    private LampWorker lampWorker = new LampWorker();

    @Test
    public void lamp_is_shut_down_before_lightning_up() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(10, 0)
        );
        setDimDelay(Duration.ofSeconds(1));
        setDimDuration(Duration.ofSeconds(1));
        setCurrentTime(LocalTime.of(0, 59));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(pwm -> {
                    assertThat(pwm, closeTo(0, 5));
                    return true;
                }
        ));
    }

    @Test
    public void sets_light_power_to_lowest_when_just_started() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(10, 0)
        );
        setDimDelay(Duration.ofSeconds(10));
        setDimDuration(Duration.ofSeconds(10));
        setCurrentTime(LocalTime.of(1, 1));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(pwm -> {
                    assertThat(pwm, closeTo(0, 5));
                    return true;
                }
        ));
    }

    @Test
    public void sets_light_power_to_max_when_close_to_end_time() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(10, 0)
        );
        setDimDelay(Duration.ofSeconds(10));
        setDimDuration(Duration.ofSeconds(10));
        setCurrentTime(LocalTime.of(9, 59));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(pwm -> {
                    assertThat(pwm, closeTo(MAX_PWM_RATE, 5));
                    return true;
                }
        ));
    }

    @Test
    public void keeps_max_value_during_dim_delay() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(10, 0)
        );
        setDimDelay(Duration.ofSeconds(10));
        setDimDuration(Duration.ofSeconds(10));
        setCurrentTime(LocalTime.of(10, 5));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(pwm -> {
                    assertThat(pwm, closeTo(MAX_PWM_RATE, 5));
                    return true;
                }
        ));
    }

    @Test
    public void lamp_is_still_close_to_max_light_when_started_dimming() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(2, 0)
        );
        setDimDelay(Duration.ofSeconds(1));
        setDimDuration(Duration.ofMinutes(100000));
        setCurrentTime(LocalTime.of(2, 0).plusMinutes(1));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(pwm -> {
                    assertThat(pwm, closeTo(MAX_PWM_RATE, 5));
                    return true;
                }
        ));
    }

    @Test
    public void lamp_is_close_to_shut_down_at_the_end_of_dimming() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(2, 0)
        );
        setDimDelay(Duration.ofSeconds(1));
        setDimDuration(Duration.ofMinutes(100000));
        setCurrentTime(LocalTime.of(2, 0).plusMinutes(99999));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(pwm -> {
                    assertThat(pwm, closeTo(0, 5));
                    return true;
                }
        ));
    }

    @Test
    public void shuts_down_the_lamp_after_dimming() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(10, 0)
        );
        setDimDelay(Duration.ofSeconds(10));
        setDimDuration(Duration.ofSeconds(10));
        setCurrentTime(LocalTime.of(10, 21));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(pwm -> {
                    assertThat(pwm, closeTo(0, 5));
                    return true;
                }
        ));
    }

    private void setWakeUpDay(LocalTime start, LocalTime end) {
        when(wakeUpService.getTodayWakeUpDay()).thenReturn(WakeUpDay.builder()
                .start(start)
                .end(end)
                .build());
    }

    private void setDimDelay(Duration value) {
        when(wakeUpService.getDimDelay()).thenReturn(value);
    }

    private void setDimDuration(Duration value) {
        when(wakeUpService.getDimDuration()).thenReturn(value);
    }

    private void setCurrentTime(LocalTime now) {
        when(time.now()).thenReturn(now);
    }
}
