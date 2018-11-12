package de.fhg.iais.roberta.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class CalliopeConfiguration extends Configuration {
    private static final Configuration configuration;
    static {
        ConfigurationComponent pin0 = new ConfigurationComponent("pin0", false, "P12", "NO_SLOT", "0", Collections.emptyMap());
        ConfigurationComponent pin1 = new ConfigurationComponent("pin1", false, "P0", "NO_SLOT", "1", Collections.emptyMap());
        ConfigurationComponent pin2 = new ConfigurationComponent("pin2", false, "P1", "NO_SLOT", "2", Collections.emptyMap());
        ConfigurationComponent C16 = new ConfigurationComponent("C16", false, "P2", "NO_SLOT", "C16", Collections.emptyMap());
        ConfigurationComponent C04 = new ConfigurationComponent("C04", false, "P3", "NO_SLOT", "C04", Collections.emptyMap());
        ConfigurationComponent C05 = new ConfigurationComponent("C05", false, "P4", "NO_SLOT", "C05", Collections.emptyMap());
        ConfigurationComponent C12 = new ConfigurationComponent("C12", false, "P6", "NO_SLOT", "C12", Collections.emptyMap());
        ConfigurationComponent C11 = new ConfigurationComponent("C11", false, "P7", "NO_SLOT", "C11", Collections.emptyMap());
        ConfigurationComponent C17 = new ConfigurationComponent("C17", false, "P8", "NO_SLOT", "C17", Collections.emptyMap());
        ConfigurationComponent C10 = new ConfigurationComponent("C10", false, "P9", "NO_SLOT", "C10", Collections.emptyMap());
        ConfigurationComponent C06 = new ConfigurationComponent("C06", false, "P10", "NO_SLOT", "C06", Collections.emptyMap());
        ConfigurationComponent C07 = new ConfigurationComponent("C07", false, "P13", "NO_SLOT", "C07", Collections.emptyMap());
        ConfigurationComponent C08 = new ConfigurationComponent("C08", false, "P14", "NO_SLOT", "C08", Collections.emptyMap());
        ConfigurationComponent C09 = new ConfigurationComponent("C09", false, "P15", "NO_SLOT", "C09", Collections.emptyMap());
        ConfigurationComponent pin3 = new ConfigurationComponent("3", false, "P16", "NO_SLOT", "3", Collections.emptyMap());
        ConfigurationComponent C19 = new ConfigurationComponent("C19", false, "P19", "NO_SLOT", "C19", Collections.emptyMap());
        ConfigurationComponent C18 = new ConfigurationComponent("C18", false, "P20", "NO_SLOT", "C18", Collections.emptyMap());
        ConfigurationComponent pin4 = new ConfigurationComponent("4", false, "P19", "NO_SLOT", "4", Collections.emptyMap());
        ConfigurationComponent pin5 = new ConfigurationComponent("5", false, "P2", "NO_SLOT", "5", Collections.emptyMap());
        ConfigurationComponent X = new ConfigurationComponent("X", false, "Pitch", "NO_SLOT", "X", Collections.emptyMap());
        ConfigurationComponent Y = new ConfigurationComponent("Y", false, "Roll", "NO_SLOT", "Y", Collections.emptyMap());
        ConfigurationComponent Z = new ConfigurationComponent("Z", false, "Yaw", "NO_SLOT", "Z", Collections.emptyMap());
        ConfigurationComponent STRENGTH = new ConfigurationComponent("STRENGTH", false, "STRENGTH", "NO_SLOT", "STRENGTH", Collections.emptyMap());
        ConfigurationComponent NO_PORT = new ConfigurationComponent("NO_PORT", false, "NO_PORT", "NO_SLOT", "NO_PORT", Collections.emptyMap());
        ConfigurationComponent A = new ConfigurationComponent("A", false, "A", "NO_SLOT", "A", Collections.emptyMap());
        ConfigurationComponent B = new ConfigurationComponent("B", false, "B", "NO_SLOT", "B", Collections.emptyMap());
        ConfigurationComponent AB = new ConfigurationComponent("AB", false, "AB", "NO_SLOT", "AB", Collections.emptyMap());
        ConfigurationComponent button_a = new ConfigurationComponent("button_a", false, "A", "NO_SLOT", "button_a", Collections.emptyMap());
        ConfigurationComponent button_b = new ConfigurationComponent("button_b", false, "B", "NO_SLOT", "button_b", Collections.emptyMap());

        ArrayList<ConfigurationComponent> components =
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
                    button_a,
                    button_b);
        configuration = new Configuration.Builder().addComponents(components).build();
    }

    public CalliopeConfiguration(Collection<ConfigurationComponent> configurationComponents, float wheelDiameterCM, float trackWidthCM) {
        super(configurationComponents, wheelDiameterCM, trackWidthCM);

    }

    public static class Builder extends Configuration.Builder {

        @Override
        public Builder addComponents(List<ConfigurationComponent> components) {
            throw new DbcException("Unsupported operation!");
        }

        @Override
        public Builder setWheelDiameter(float wheelDiameter) {
            throw new DbcException("Unsupported operation!");
        }

        @Override
        public Builder setTrackWidth(float trackWidth) {
            throw new DbcException("Unsupported operation!");
        }

        @Override
        public Configuration build() {
            return configuration;
        }
    }

}
