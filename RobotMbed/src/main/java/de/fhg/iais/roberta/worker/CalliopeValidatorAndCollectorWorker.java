package de.fhg.iais.roberta.worker;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.CalliopeMethods;

public abstract class CalliopeValidatorAndCollectorWorker extends MbedValidatorAndCollectorWorker {
    public static final List<String> FREE_PINS = Stream.of("A", "B", "0", "1", "2", "3", "4", "5", "C04", "C05", "C06", "C07", "C08", "C09", "C10", "C11", "C12", "C16", "C17", "C18", "C19").collect(Collectors.toList());
    public static final List<String> DEFAULT_PROPERTIES = Stream.of("KEY", "ACCELEROMETER", "RGBLED", "SOUND", "COMPASS", "BUZZER", "TEMPERATURE", "GYRO", "LIGHT", "ROBOT").collect(Collectors.toList());
    public static final HashMap<String, String> MAP_CORRECT_CONFIG_PINS = new HashMap<String, String>() {{
        put(SC.ULTRASONIC, "5");
        put(SC.COLOUR, "4");
    }};

    public CalliopeValidatorAndCollectorWorker() {
        super(FREE_PINS, DEFAULT_PROPERTIES, MAP_CORRECT_CONFIG_PINS);
    }

    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(CalliopeMethods.class);
    }
}
