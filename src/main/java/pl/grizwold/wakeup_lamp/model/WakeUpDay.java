package pl.grizwold.wakeup_lamp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WakeUpDay {
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime start;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime end;
}
