package de.fhg.iais.roberta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.Util;

public class Ev3AstTest extends AstTest {
    protected static ConfigurationAst makeStandard() {
        Map<String, String> motorBproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        Map<String, String> motorCproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", "C", motorCproperties);

        Map<String, String> touchSensorProperties = Util.createMap();
        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", touchSensorProperties);

        Map<String, String> gyroSensorProperties = Util.createMap();
        ConfigurationComponent gyroSensor = new ConfigurationComponent("GYRO", false, "S2", "2", gyroSensorProperties);

        Map<String, String> colourSensorProperties = Util.createMap();
        ConfigurationComponent colourSensor = new ConfigurationComponent("COLOR", false, "S3", "3", colourSensorProperties);

        Map<String, String> ultrasonicSensorProperties = Util.createMap();
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S4", "4", ultrasonicSensorProperties);

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(18f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorB, motorC, touchSensor, gyroSensor, colourSensor, ultrasonicSensor));
        ConfigurationAst configuration = builder.build();
        configuration.setRobotName("ev3lejosv1");
        return configuration;
    }

    protected static ConfigurationAst makeLargeLargeMediumTouchGyroColorUltrasonic() {
        Map<String, String> motorAproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        Map<String, String> motorDproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorD = new ConfigurationComponent("MEDIUM", true, "D", "D", motorDproperties);

        Map<String, String> touchSensorProperties = Util.createMap();
        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", touchSensorProperties);

        Map<String, String> gyroSensorProperties = Util.createMap();
        ConfigurationComponent gyroSensor = new ConfigurationComponent("GYRO", false, "S2", "2", gyroSensorProperties);

        Map<String, String> colourSensorProperties = Util.createMap();
        ConfigurationComponent colourSensor = new ConfigurationComponent("COLOR", false, "S3", "3", colourSensorProperties);

        Map<String, String> ultrasonicSensorProperties = Util.createMap();
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S4", "4", ultrasonicSensorProperties);

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder
            .setTrackWidth(18f)
            .setWheelDiameter(5.6f)
            .addComponents(Arrays.asList(motorA, motorB, motorD, touchSensor, gyroSensor, colourSensor, ultrasonicSensor));
        ConfigurationAst configuration = builder.build();
        configuration.setRobotName("ev3lejosv1");
        return configuration;
    }

    protected static ConfigurationAst makeMediumLargeTouchGyroColorUltrasonic() {
        Map<String, String> motorBproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("MEDIUM", true, "B", "B", motorBproperties);

        Map<String, String> motorCproperties = Util.createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", "C", motorCproperties);

        Map<String, String> touchSensorProperties = Util.createMap();
        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", touchSensorProperties);

        Map<String, String> gyroSensorProperties = Util.createMap();
        ConfigurationComponent gyroSensor = new ConfigurationComponent("GYRO", false, "S2", "2", gyroSensorProperties);

        Map<String, String> colourSensorProperties = Util.createMap();
        ConfigurationComponent colourSensor = new ConfigurationComponent("COLOR", false, "S3", "3", colourSensorProperties);

        Map<String, String> ultrasonicSensorProperties = Util.createMap();
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S4", "4", ultrasonicSensorProperties);

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorB, motorC, touchSensor, gyroSensor, colourSensor, ultrasonicSensor));
        ConfigurationAst configuration = builder.build();
        configuration.setRobotName("ev3dev");
        return configuration;
    }

    protected static ConfigurationAst makeLargeLargeTouchGyroInfraredUltrasonic() {
        Map<String, String> motorBproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "A", "A", motorBproperties);

        Map<String, String> motorCproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "B", "B", motorCproperties);

        Map<String, String> touchSensorProperties = Util.createMap();
        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", touchSensorProperties);

        Map<String, String> gyroSensorProperties = Util.createMap();
        ConfigurationComponent gyroSensor = new ConfigurationComponent("GYRO", false, "S2", "2", gyroSensorProperties);

        Map<String, String> infraredSensorProperties = Util.createMap();
        ConfigurationComponent colourSensor = new ConfigurationComponent("INFRARED", false, "S3", "3", infraredSensorProperties);

        Map<String, String> ultrasonicSensorProperties = Util.createMap();
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S4", "4", ultrasonicSensorProperties);

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorB, motorC, touchSensor, gyroSensor, colourSensor, ultrasonicSensor));
        ConfigurationAst configuration = builder.build();
        configuration.setRobotName("ev3dev");
        return configuration;
    }

    protected static ConfigurationAst makeStandardConfigurationNonRegulated() {
        Map<String, String> motorBproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        Map<String, String> motorCproperties = Util.createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", "C", motorCproperties);

        Map<String, String> touchSensorProperties = Util.createMap();
        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", touchSensorProperties);

        Map<String, String> gyroSensorProperties = Util.createMap();
        ConfigurationComponent gyroSensor = new ConfigurationComponent("GYRO", false, "S2", "2", gyroSensorProperties);

        Map<String, String> colourSensorProperties = Util.createMap();
        ConfigurationComponent colourSensor = new ConfigurationComponent("COLOR", false, "S3", "3", colourSensorProperties);

        Map<String, String> ultrasonicSensorProperties = Util.createMap();
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S4", "4", ultrasonicSensorProperties);

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorB, motorC, touchSensor, gyroSensor, colourSensor, ultrasonicSensor));
        ConfigurationAst configuration = builder.build();
        configuration.setRobotName("ev3dev");
        return configuration;
    }

    protected static ConfigurationAst makeMediumLargeTouchUltrasonicColor() {
        Map<String, String> motorAproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("MEDIUM", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S2", "2", Collections.emptyMap());
        ConfigurationComponent colorSensor = new ConfigurationComponent("COLOR", false, "S3", "3", Collections.emptyMap());
        //        ConfigurationComponent ultrasonicSensor4 = new ConfigurationComponent("ULTRASONIC", false, "S4", BlocklyConstants.NO_SLOT, "4", Collections.emptyMap());

        return build(motorA, motorB, touchSensor, ultrasonicSensor, colorSensor);
    }

    protected static ConfigurationAst makeMediumLargeTouchUltrasonicColorUltrasonic() {
        Map<String, String> motorAproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("MEDIUM", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S2", "2", Collections.emptyMap());
        ConfigurationComponent colorSensor = new ConfigurationComponent("COLOR", false, "S3", "3", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor4 = new ConfigurationComponent("ULTRASONIC", false, "S4", "4", Collections.emptyMap());

        return build(motorA, motorB, touchSensor, ultrasonicSensor, colorSensor, ultrasonicSensor4);
    }

    protected static ConfigurationAst makeMediumLargeTouchGyroInfraredUltrasonic() {
        Map<String, String> motorAproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("MEDIUM", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);
        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", Collections.emptyMap());
        ConfigurationComponent gyro = new ConfigurationComponent("GYRO", false, "S2", "2", Collections.emptyMap());
        ConfigurationComponent infrared = new ConfigurationComponent("INFRARED", false, "S3", "3", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor4 = new ConfigurationComponent("ULTRASONIC", false, "S4", "4", Collections.emptyMap());

        return build(motorA, motorB, touchSensor, infrared, ultrasonicSensor4, gyro);
    }

    protected static ConfigurationAst makeRotateRegulatedUnregulatedForwardBackwardMotors() {
        Map<String, String> motorAproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "ON", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        Map<String, String> motorCproperties = Util.createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", "C", motorCproperties);

        Map<String, String> motorDproperties = Util.createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "ON", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorD = new ConfigurationComponent("LARGE", true, "D", "D", motorDproperties);

        return build(motorA, motorB, motorC, motorD);
    }

    protected static ConfigurationAst makeHTColor() {
        ConfigurationComponent htColor = new ConfigurationComponent("HT_COLOR", false, "S3", "3", Collections.emptyMap());
        return build(htColor);
    }

    protected static ConfigurationAst build(ConfigurationComponent... components) {
        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        return builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(components)).build();
    }
}
