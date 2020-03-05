package de.fhg.iais.roberta.components;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;

public class MicrobitConfiguration extends ConfigurationAst {
    private static final List<ConfigurationComponent> components;
    static {
        ConfigurationComponent pin0 =
            new ConfigurationComponent(
                "pin0",
                false,
                "0",
                "0",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("pin0", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent pin1 =
            new ConfigurationComponent(
                "pin1",
                false,
                "1",
                "1",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("pin1", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent pin2 =
            new ConfigurationComponent(
                "pin2",
                false,
                "2",
                "2",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("pin2", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent pin3 =
            new ConfigurationComponent(
                "pin3",
                false,
                "3",
                "3",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("pin3", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent A =
            new ConfigurationComponent(
                "A",
                false,
                "A",
                "A",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("A", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent B =
            new ConfigurationComponent(
                "B",
                false,
                "B",
                "B",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("B", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent X =
            new ConfigurationComponent(
                "X",
                false,
                "x",
                "X",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("X", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent Y =
            new ConfigurationComponent(
                "Y",
                false,
                "y",
                "Y",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("Y", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent Z =
            new ConfigurationComponent(
                "Z",
                false,
                "z",
                "Z",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("Z", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent STRENGTH =
            new ConfigurationComponent(
                "STRENGTH",
                false,
                "STRENGTH",
                "STRENGTH",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("STRENGTH", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent NO_PORT =
            new ConfigurationComponent(
                "NO_PORT",
                false,
                "NO_PORT",
                "NO_PORT",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("NO_PORT", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);

        components = Lists.newArrayList(pin0, pin1, pin2, pin3, X, Y, Z, STRENGTH, NO_PORT);
    }

    private MicrobitConfiguration(
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
}
