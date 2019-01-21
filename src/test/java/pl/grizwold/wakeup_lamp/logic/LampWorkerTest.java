package pl.grizwold.wakeup_lamp.logic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import pl.grizwold.wakeup_lamp.model.WakeUpDay;

import java.time.Duration;
import java.time.LocalTime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    public void sets_light_power_to_max() {
        setWakeUpDay(
                LocalTime.of(1,0),
                LocalTime.of(10, 0)
        );
        dimDelay(Duration.ofSeconds(10));
        dimDuration(Duration.ofSeconds(10));

        now(LocalTime.of(9,59));

        lampWorker.scheduled();

        verify(raspberryPi).setPWM(RaspberryPi.MAX_PWM_RATE);
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