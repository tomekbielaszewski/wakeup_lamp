package pl.grizwold.wakeup_lamp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.grizwold.wakeup_lamp.logic.RaspberryPi;
import pl.grizwold.wakeup_lamp.model.WakeUpDay;
import pl.grizwold.wakeup_lamp.model.WakeUpWeek;

import java.time.Duration;
import java.time.LocalTime;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class Starter {
    public static final WakeUpWeek DEFAULT_WAKEUP = WakeUpWeek.builder()
            .weekend(WakeUpDay.builder()
                    .start(LocalTime.of(23, 0))
                    .end(LocalTime.of(23, 35))
                    .build())
            .workDay(WakeUpDay.builder()
                    .start(LocalTime.of(6, 0))
                    .end(LocalTime.of(7, 0))
                    .build())
            .dimDelay(Duration.ofMinutes(30))
            .dimDuration(Duration.ofMinutes(5))
            .build();
    private static final int BLINK_REPEATS = 3;

    public static void main(String[] args) {
        RaspberryPi raspberryPi = SpringApplication.run(Starter.class)
                .getBean(RaspberryPi.class);
        welcomeBlink(raspberryPi);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }

    private static void welcomeBlink(RaspberryPi raspberryPi) {
        for (int b = 0; b <= BLINK_REPEATS; b++) {
            for (int i = 0; i <= RaspberryPi.MAX_PWM_RATE; i++) {
                raspberryPi.setPWM(i);
                sleep();
            }
            for (int i = RaspberryPi.MAX_PWM_RATE; i >= 0; i--) {
                raspberryPi.setPWM(i);
                sleep();
            }
        }
    }

    @SneakyThrows
    private static void sleep() {
        Thread.sleep(1);
    }
}
