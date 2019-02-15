package pl.grizwold.wakeup_lamp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WakeUpDay {
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime start;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime end;
}
