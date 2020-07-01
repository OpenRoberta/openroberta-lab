package de.fhg.iais.roberta.components.vorwerk;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.SC;

public class VorwerkConfiguration extends ConfigurationAst {
    private static List<ConfigurationComponent> components;
    static {
        Map<String, String> motorAproperties = createMap(SC.MOTOR_REGULATION, SC.TRUE, SC.MOTOR_REVERSE, SC.OFF, SC.MOTOR_DRIVE, SC.LEFT);
        ConfigurationComponent motorA =
            new ConfigurationComponent(
                SC.LARGE,
                true,
                "left",
                "LEFT",
                motorAproperties,
                BlocklyBlockProperties.make(SC.LARGE, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);

        Map<String, String> motorBproperties = createMap(SC.MOTOR_REGULATION, SC.TRUE, SC.MOTOR_REVERSE, SC.OFF, SC.MOTOR_DRIVE, SC.RIGHT);
        ConfigurationComponent motorB =
            new ConfigurationComponent(
                SC.LARGE,
                true,
                "right",
                "RIGHT",
                motorBproperties,
                BlocklyBlockProperties.make(SC.LARGE, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);

        ConfigurationComponent leftUltrasonic =
            new ConfigurationComponent(
                SC.ULTRASONIC,
                false,
                "left_ultrasonic",
                "LEFT_ULTRASONIC",
                Collections.emptyMap(),
                BlocklyBlockProperties.make(SC.ULTRASONIC, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent centerUltrasonic =
            new ConfigurationComponent(
                SC.ULTRASONIC,
                false,
                "center_ultrasonic",
                "CENTER_ULTRASONIC",
                Collections.emptyMap(),
                BlocklyBlockProperties.make(SC.ULTRASONIC, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent rightUltrasonic =
            new ConfigurationComponent(
                SC.ULTRASONIC,
                false,
                "right_ultrasonic",
                "RIGHT_ULTRASONIC",
                Collections.emptyMap(),
                BlocklyBlockProperties.make(SC.ULTRASONIC, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent leftTouch =
            new ConfigurationComponent(
                SC.TOUCH,
                false,
                "left",
                "LEFT_TOUCH",
                Collections.emptyMap(),
                BlocklyBlockProperties.make(SC.TOUCH, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent rightTouch =
            new ConfigurationComponent(
                SC.TOUCH,
                false,
                "right",
                "RIGHT_TOUCH",
                Collections.emptyMap(),
                BlocklyBlockProperties.make(SC.TOUCH, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent leftDropoff =
            new ConfigurationComponent(
                SC.TOUCH,
                false,
                "left",
                "LEFT_DROPOFF",
                Collections.emptyMap(),
                BlocklyBlockProperties.make(SC.TOUCH, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent rightDropoff =
            new ConfigurationComponent(
                SC.TOUCH,
                false,
                "right",
                "RIGHT_DROPOFF",
                Collections.emptyMap(),
                BlocklyBlockProperties.make(SC.TOUCH, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent x =
            new ConfigurationComponent(
                SC.ACCELEROMETER,
                false,
                "x",
                "X",
                Collections.emptyMap(),
                BlocklyBlockProperties.make(SC.ACCELEROMETER, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent y =
            new ConfigurationComponent(
                SC.ACCELEROMETER,
                false,
                "y",
                "Y",
                Collections.emptyMap(),
                BlocklyBlockProperties.make(SC.ACCELEROMETER, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent z =
            new ConfigurationComponent(
                SC.ACCELEROMETER,
                false,
                "z",
                "Z",
                Collections.emptyMap(),
                BlocklyBlockProperties.make(SC.ACCELEROMETER, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent strength =
            new ConfigurationComponent(
                SC.ACCELEROMETER,
                false,
                "strength",
                "STRENGTH",
                Collections.emptyMap(),
                BlocklyBlockProperties.make(SC.ACCELEROMETER, "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);

        components =
            Lists
                .newArrayList(
                    motorA,
                    motorB,
                    leftUltrasonic,
                    rightUltrasonic,
                    centerUltrasonic,
                    leftTouch,
                    rightTouch,
                    x,
                    y,
                    z,
                    strength,
                    leftDropoff,
                    rightDropoff);
    }

    private VorwerkConfiguration(
        Collection<ConfigurationComponent> configurationComponents,
        String robottype,
        String xmlversion,
        String description,
        String tags,
        float wheelDiameter,
        float trackWidth,
        String ipAddress,
        String userName,
        String password) {
        super(components, robottype, xmlversion, description, tags, wheelDiameter, trackWidth, ipAddress, userName, password);
    }

    private static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
    }
}
