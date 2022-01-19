package de.fhg.iais.roberta.worker;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fhg.iais.roberta.visitor.CalliopeMethods;

public abstract class CalliopeValidatorAndCollectorWorker extends MbedValidatorAndCollectorWorker {

    public CalliopeValidatorAndCollectorWorker() {
        super(Stream.of("A", "B", "0", "1", "2", "3", "4", "5", "C04","C05","C06","C07","C08","C09","C10","C11","C12","C16","C17","C18", "C19").collect(Collectors.toList()),
            Stream.of("KEY", "ACCELEROMETER", "RGBLED", "SOUND", "COMPASS", "BUZZER", "TEMPERATURE", "GYRO", "LIGHT", "ROBOT").collect(Collectors.toList()));
    }

    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(CalliopeMethods.class);
    }
}
