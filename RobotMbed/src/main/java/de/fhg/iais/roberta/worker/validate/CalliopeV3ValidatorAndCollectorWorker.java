package de.fhg.iais.roberta.worker.validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.CalliopeMethods;
import de.fhg.iais.roberta.visitor.validate.CalliopeV3ValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;

public class CalliopeV3ValidatorAndCollectorWorker extends MbedValidatorAndCollectorWorker{
    /**
     * @param freePins All the pins that can be used on the calliope/microbit by the configuration blocks. Also used for checking if the configuration blocks only
     *     contain existing pins (variable existingPins)
     * @param defaultProps The default properties to get the pins from the calliope/microbit.
     * @param mapCorrectConfigPins There are a few configuration blocks that either have no property to get the pins or the pins aren't mapped correctly so this
     *     hashmap maps them to their specific pins, so they can be checked.
     */
    public static final List<String> FREE_PINS = Stream.of("0", "1", "2", "3", "4", "5", "C04", "C05", "C06", "C07", "C08", "C09", "C10", "C11", "C12", "C13", "C14", "C15", "C16", "C17", "C18", "C19", "C20", "M0", "M1").collect(Collectors.toList());
    public static final List<String> DEFAULT_PROPERTIES = Stream.of("KEY", "RGBLED", "BUZZER", "LOGOTOUCH", "ACCELEROMETER", "GYRO", "COMPASS", "TEMPERATURE", "LIGHT", "SOUND", "ROBOT").collect(Collectors.toList());
    public static final HashMap<String, String> MAP_CORRECT_CONFIG_PINS = new HashMap<String, String>() {{
        put(SC.ULTRASONIC, "5");
        put(SC.COLOUR, "4");
    }};

    public CalliopeV3ValidatorAndCollectorWorker() {
        super(FREE_PINS, DEFAULT_PROPERTIES, MAP_CORRECT_CONFIG_PINS);
    }
    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(CalliopeMethods.class);
    }

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new CalliopeV3ValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders, false, true);
    }
}
