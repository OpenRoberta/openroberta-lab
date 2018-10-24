package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;
import de.fhg.iais.roberta.visitor.validate.AbstractBrickValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.Ev3BrickValidatorVisitor;

public class ProgramConfigurationCompatabilityTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void ev3program_configuration_compatibility_4_errors() throws Exception {

        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", BlocklyConstants.NO_SLOT, "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", BlocklyConstants.NO_SLOT, "B", motorBproperties);

        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", BlocklyConstants.NO_SLOT, "1", createMap("TYPE", "TOUCH"));
        ConfigurationComponent ultrasonicSensor =
            new ConfigurationComponent("ULTRASONIC", false, "S2", BlocklyConstants.NO_SLOT, "2", createMap("TYPE", "ULTRASONIC"));

        final Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB, touchSensor, ultrasonicSensor));

        Configuration brickConfiguration = builder.build();
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/program_config_compatibility.xml");

        AbstractBrickValidatorVisitor programChecker = new Ev3BrickValidatorVisitor(brickConfiguration);
        programChecker.check(phrases);

        Assert.assertEquals(4, programChecker.getErrorCount());

    }

    private static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
    }

    @Test
    public void ev3program_configuration_compatibility_0_errors() throws Exception {
        Configuration.Builder builder = new Configuration.Builder();

        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("MEDIUM", true, "A", BlocklyConstants.NO_SLOT, "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", BlocklyConstants.NO_SLOT, "B", motorBproperties);

        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", BlocklyConstants.NO_SLOT, "1", createMap("TYPE", "TOUCH"));
        ConfigurationComponent colorSensor = new ConfigurationComponent("COLOR", false, "S2", BlocklyConstants.NO_SLOT, "2", createMap("TYPE", "COLOR"));
        ConfigurationComponent gyroSensor = new ConfigurationComponent("GYRO", false, "S3", BlocklyConstants.NO_SLOT, "3", createMap("TYPE", "GYRO"));
        ConfigurationComponent ultrasonicSensor =
            new ConfigurationComponent("ULTRASONIC", false, "S4", BlocklyConstants.NO_SLOT, "4", createMap("TYPE", "ULTRASONIC"));

        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB, touchSensor, colorSensor, gyroSensor, ultrasonicSensor));

        Configuration brickConfiguration = builder.build();
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/program_config_compatibility_gyro_touch_ultra_color.xml");

        AbstractBrickValidatorVisitor programChecker = new Ev3BrickValidatorVisitor(brickConfiguration);
        programChecker.check(phrases);

        Assert.assertEquals(0, programChecker.getErrorCount());

    }
}
