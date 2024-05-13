package de.fhg.iais.roberta.worker.validate;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fhg.iais.roberta.util.syntax.SC;

public abstract class MbedV2ValidatorAndCollectorWorker extends MbedValidatorAndCollectorWorker {
    public MbedV2ValidatorAndCollectorWorker(List<String> freePins, List<String> defaultProps, HashMap<String, String> mapCorrectConfigPins) {
        super(freePins, defaultProps, mapCorrectConfigPins);
    }
}
