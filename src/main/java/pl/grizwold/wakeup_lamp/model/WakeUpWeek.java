package pl.grizwold.wakeup_lamp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WakeUpWeek {
    private WakeUpDay workDay;
    private WakeUpDay weekend;

    private Duration dimDelay;
    private Duration dimDuration;
}
