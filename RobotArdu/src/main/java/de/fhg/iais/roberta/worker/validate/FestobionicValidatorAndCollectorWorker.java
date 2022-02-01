package de.fhg.iais.roberta.worker.validate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FestobionicValidatorAndCollectorWorker extends ArduinoValidatorAndCollectorWorker {
    public FestobionicValidatorAndCollectorWorker() {
        super(
            Stream
                .of("LED_BUILTIN", "1", "2", "3", "4")
                .collect(Collectors.toList()));
    }
}
