package pl.grizwold.wakeup_lamp.logic;

import com.pi4j.io.gpio.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.wiringpi.Gpio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RaspberryPi {
    public static final int MAX_PWM_RATE = 1024;

    private final GpioPinPwmOutput pwm;

    public RaspberryPi() {
        final GpioController gpio = GpioFactory.getInstance();
        Pin pin = CommandArgumentParser.getPin(
                RaspiPin.class,
                RaspiPin.GPIO_01);
        this.pwm = gpio.provisionPwmOutputPin(pin);
        Gpio.pwmSetMode(Gpio.PWM_MODE_BAL);
        this.pwm.setPwm(0);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            pwm.setPwm(0);
            gpio.shutdown();
        }));
    }

    public void setPWM(int rate) {
        this.pwm.setPwm(rate);
    }
}
