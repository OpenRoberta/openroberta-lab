package de.fhg.iais.roberta.components;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;

public class CalliopeConfiguration extends ConfigurationAst {
    private static final List<ConfigurationComponent> components;
    static {
        ConfigurationComponent pin0 =
            new ConfigurationComponent(
                "pin0",
                false,
                "P12",
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
                "P0",
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
                "P1",
                "2",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("pin2", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C16 =
            new ConfigurationComponent(
                "C16",
                false,
                "P2",
                "C16",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C16", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C04 =
            new ConfigurationComponent(
                "C04",
                false,
                "P3",
                "C04",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C04", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C05 =
            new ConfigurationComponent(
                "C05",
                false,
                "P4",
                "C05",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C05", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C12 =
            new ConfigurationComponent(
                "C12",
                false,
                "P6",
                "C12",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C12", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C11 =
            new ConfigurationComponent(
                "C11",
                false,
                "P7",
                "C11",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C11", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C17 =
            new ConfigurationComponent(
                "C17",
                false,
                "P8",
                "C17",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C17", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C10 =
            new ConfigurationComponent(
                "C10",
                false,
                "P9",
                "C10",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C10", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C06 =
            new ConfigurationComponent(
                "C06",
                false,
                "P10",
                "C06",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C06", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C07 =
            new ConfigurationComponent(
                "C07",
                false,
                "P13",
                "C07",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C07", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C08 =
            new ConfigurationComponent(
                "C08",
                false,
                "P14",
                "C08",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C08", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C09 =
            new ConfigurationComponent(
                "C09",
                false,
                "P15",
                "C09",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C09", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent pin3 =
            new ConfigurationComponent(
                "3",
                false,
                "P16",
                "3",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("3", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C19 =
            new ConfigurationComponent(
                "C19",
                false,
                "P19",
                "C19",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C19", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent C18 =
            new ConfigurationComponent(
                "C18",
                false,
                "P20",
                "C18",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("C18", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent pin4 =
            new ConfigurationComponent(
                "4",
                false,
                "P19",
                "4",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("4", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent pin5 =
            new ConfigurationComponent(
                "5",
                false,
                "P2",
                "5",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("5", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent X =
            new ConfigurationComponent(
                "X",
                false,
                "Pitch",
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
                "Roll",
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
                "Yaw",
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
        ConfigurationComponent AB =
            new ConfigurationComponent(
                "AB",
                false,
                "AB",
                "AB",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("AB", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);
        ConfigurationComponent BOTH =
            new ConfigurationComponent(
                "BOTH",
                false,
                "BOTH",
                "BOTH",
                Collections.emptyMap(),
                BlocklyBlockProperties.make("BOTH", "this-will-be-regenerated-anyway"),
                BlocklyComment.make("", false, "0", "0"),
                0,
                0);

        components =
            Lists
                .newArrayList(
                    pin0,
                    pin1,
                    pin2,
                    C16,
                    C04,
                    C05,
                    C12,
                    C11,
                    C17,
                    C10,
                    C06,
                    C07,
                    C08,
                    C09,
                    pin3,
                    C19,
                    C18,
                    pin4,
                    pin5,
                    X,
                    Y,
                    Z,
                    STRENGTH,
                    NO_PORT,
                    A,
                    B,
                    AB,
                    BOTH);
    }

    private CalliopeConfiguration(
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
        super(components, robottype, xmlversion, description, tags, wheelDiameter, trackWidth, ipAddress,userName, password);
    }
}
