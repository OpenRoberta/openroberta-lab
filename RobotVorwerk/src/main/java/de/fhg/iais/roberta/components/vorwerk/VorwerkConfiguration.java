package de.fhg.iais.roberta.components.vorwerk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Lists;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.SC;

public class VorwerkConfiguration extends ConfigurationAst {
    private static ArrayList<ConfigurationComponent> components;
    private final String ipAddress;
    private final String portNumber;
    private final String userName;
    private final String password;
    static {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent(SC.LARGE, true, "left", "LEFT", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent(SC.LARGE, true, "right", "RIGHT", motorBproperties);

        ConfigurationComponent leftUltrasonic = new ConfigurationComponent(SC.ULTRASONIC, false, "left_ultrasonic", "LEFT_ULTRASONIC", Collections.emptyMap());
        ConfigurationComponent centerUltrasonic =
            new ConfigurationComponent(SC.ULTRASONIC, false, "center_ultrasonic", "CENTER_ULTRASONIC", Collections.emptyMap());
        ConfigurationComponent rightUltrasonic =
            new ConfigurationComponent(SC.ULTRASONIC, false, "right_ultrasonic", "RIGHT_ULTRASONIC", Collections.emptyMap());
        ConfigurationComponent leftTouch = new ConfigurationComponent(SC.TOUCH, false, "left", "LEFT_TOUCH", Collections.emptyMap());
        ConfigurationComponent rightTouch = new ConfigurationComponent(SC.TOUCH, false, "right", "RIGHT_TOUCH", Collections.emptyMap());
        ConfigurationComponent leftDropoff = new ConfigurationComponent(SC.TOUCH, false, "left", "LEFT_DROPOFF", Collections.emptyMap());
        ConfigurationComponent rightDropoff = new ConfigurationComponent(SC.TOUCH, false, "right", "RIGHT_DROPOFF", Collections.emptyMap());
        ConfigurationComponent x = new ConfigurationComponent(SC.ACCELEROMETER, false, "x", "X", Collections.emptyMap());
        ConfigurationComponent y = new ConfigurationComponent(SC.ACCELEROMETER, false, "y", "Y", Collections.emptyMap());
        ConfigurationComponent z = new ConfigurationComponent(SC.ACCELEROMETER, false, "z", "Z", Collections.emptyMap());
        ConfigurationComponent strength = new ConfigurationComponent(SC.ACCELEROMETER, false, "strength", "STRENGTH", Collections.emptyMap());

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

    public VorwerkConfiguration(String ipAddress, String portNumber, String userName, String password) {
        super(components, 0.0f, 0.0f);

        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.userName = userName;
        this.password = password;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getPortNumber() {
        return this.portNumber;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    private static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
    }

    /**
     * This class is a builder of {@link ConfigurationAst}
     */
    public static class Builder extends ConfigurationAst.Builder {
        private String ipAddress;
        private String portNumber;
        private String userName;
        private String password;

        public Builder setIpAddres(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder setPortNumber(String portNumber) {
            this.portNumber = portNumber;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        @Override
        public VorwerkConfiguration build() {
            return new VorwerkConfiguration(this.ipAddress, this.portNumber, this.userName, this.password);
        }

    }

}
