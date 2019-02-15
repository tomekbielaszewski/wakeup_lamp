package pl.grizwold.wakeup_lamp.model;

import lombok.*;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WakeUpWeek {
    private WakeUpDay workDay;
    private WakeUpDay weekend;

    private Duration dimDelay;
    private Duration dimDuration;
}
