package de.fhg.iais.roberta.worker.validate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Nano33bleConfigurationValidatorWorker extends ArduinoConfigurationValidatorWorker {
    public Nano33bleConfigurationValidatorWorker() {
        super(
            Stream
                .of(
                    "LED_BUILTIN",
                    "LSM9DS1",
                    "APDS9960",
                    "LPS22HB",
                    "HTS221",
                    "0",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "A0",
                    "A1",
                    "A2",
                    "A3",
                    "A4",
                    "A5")
                .collect(Collectors.toList()));
    }
}
