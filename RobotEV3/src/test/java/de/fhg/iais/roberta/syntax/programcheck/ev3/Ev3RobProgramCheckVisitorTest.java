package de.fhg.iais.roberta.syntax.programcheck.ev3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;
import de.fhg.iais.roberta.visitor.validate.Ev3BrickValidatorVisitor;

public class Ev3RobProgramCheckVisitorTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    private static Configuration makeConfiguration() {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", BlocklyConstants.NO_SLOT, "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", BlocklyConstants.NO_SLOT, "B", motorBproperties);

        Map<String, String> motorDproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorD = new ConfigurationComponent("MEDIUM", true, "D", BlocklyConstants.NO_SLOT, "D", motorDproperties);

        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", BlocklyConstants.NO_SLOT, "1", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S2", BlocklyConstants.NO_SLOT, "2", Collections.emptyMap());

        final Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB, motorD, touchSensor, ultrasonicSensor));
        return builder.build();
    }

    private static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
    }

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithOneElement() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/MoveWithZeroSpeed.xml");

        Ev3BrickValidatorVisitor checkVisitor = new Ev3BrickValidatorVisitor(makeConfiguration());
        checkVisitor.check(phrases);
        Assert.assertEquals(2, checkVisitor.getWarningCount());

    }
}
