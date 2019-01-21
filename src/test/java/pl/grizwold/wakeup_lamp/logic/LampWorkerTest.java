package pl.grizwold.wakeup_lamp.logic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.grizwold.wakeup_lamp.model.WakeUpDay;

import java.time.Duration;
import java.time.LocalTime;

import static org.mockito.Mockito.*;
import static pl.grizwold.wakeup_lamp.logic.RaspberryPi.MAX_PWM_RATE;

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
    public void sets_light_power_to_max_when_close_to_end_time() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(10, 0)
        );
        dimDelay(Duration.ofSeconds(10));
        dimDuration(Duration.ofSeconds(10));

        now(LocalTime.of(9, 59));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(isCloseTo(MAX_PWM_RATE)));
    }

    @Test
    public void sets_light_power_to_lowest_when_just_started() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(10, 0)
        );
        dimDelay(Duration.ofSeconds(10));
        dimDuration(Duration.ofSeconds(10));

        now(LocalTime.of(1, 1));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(isCloseTo(0)));
    }

    @Test
    public void keeps_max_value_during_dim_delay() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(10, 0)
        );
        dimDelay(Duration.ofSeconds(10));
        dimDuration(Duration.ofSeconds(10));

        now(LocalTime.of(10, 5));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(isCloseTo(MAX_PWM_RATE)));
    }

    @Test
    public void shuts_down_the_lamp_after_dimming() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(10, 0)
        );
        dimDelay(Duration.ofSeconds(10));
        dimDuration(Duration.ofSeconds(10));

        now(LocalTime.of(10, 21));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(isCloseTo(0)));
    }

    @Test
    public void lamp_is_dimmed_at_the_end_of_dimming() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(2, 0)
        );
        dimDelay(Duration.ofSeconds(1));
        dimDuration(Duration.ofMinutes(100000));

        now(LocalTime.of(2, 0).plusMinutes(99999));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(isCloseTo(0)));
    }

    @Test
    public void lamp_is_still_close_to_max_light_when_started_dimming() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(2, 0)
        );
        dimDelay(Duration.ofSeconds(1));
        dimDuration(Duration.ofMinutes(100000));

        now(LocalTime.of(2, 0).plusMinutes(1));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(isCloseTo(MAX_PWM_RATE)));
    }

    @Test
    public void lamp_is_shut_down_before_lightning_up() {
        setWakeUpDay(
                LocalTime.of(1, 0),
                LocalTime.of(10, 0)
        );
        dimDelay(Duration.ofSeconds(1));
        dimDuration(Duration.ofSeconds(1));

        now(LocalTime.of(0, 59));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(argThat(isCloseTo(0)));
    }

    private ArgumentMatcher<Integer> isCloseTo(int value) {
        return argument -> Math.abs(argument - value) <= 5;
    }

    private void setWakeUpDay(LocalTime start, LocalTime end) {
        when(wakeUpService.getTodayWakeUpDay()).thenReturn(WakeUpDay.builder()
                .start(start)
                .end(end)
                .build());
    }

    private void dimDelay(Duration value) {
        when(wakeUpService.getDimDelay()).thenReturn(value);
    }

    private void dimDuration(Duration value) {
        when(wakeUpService.getDimDuration()).thenReturn(value);
    }

    private void now(LocalTime now) {
        when(time.now()).thenReturn(now);
    }
}