package de.fhg.iais.roberta.worker.validate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NanoValidatorAndCollectorWorker extends ArduinoValidatorAndCollectorWorker {
    public NanoValidatorAndCollectorWorker() {
        super(
            Stream
                .of("LED_BUILTIN", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7")
                .collect(Collectors.toList()));
    }
}
