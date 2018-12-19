package pl.grizwold.wakeup_lamp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.grizwold.wakeup_lamp.logic.LampWorker;
import pl.grizwold.wakeup_lamp.model.WakeUpDay;
import pl.grizwold.wakeup_lamp.model.WakeUpWeek;

import java.time.Duration;
import java.time.LocalTime;

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

    public static void main(String[] args) {
        SpringApplication.run(Starter.class).getBean(LampWorker.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }
}
