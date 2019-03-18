package de.fhg.iais.roberta.components.expedition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Lists;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.SC;

public class ExpeditionConfiguration extends Configuration {
    private static ArrayList<ConfigurationComponent> components;
    private final String ipAddress;
    private final String portNumber;
    private final String userName;
    private final String password;
    static {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent(SC.LARGE, true, "left", BlocklyConstants.NO_SLOT, "LEFT_MOTOR", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent(SC.LARGE, true, "right", BlocklyConstants.NO_SLOT, "RIGHT_MOTOR", motorBproperties);

        ConfigurationComponent leftUltrasonic =
            new ConfigurationComponent(SC.ULTRASONIC, false, "left_ultrasonic", "NO_SLOT", "LEFT_ULTRASONIC", Collections.emptyMap());
        ConfigurationComponent centerUltrasonic =
            new ConfigurationComponent(SC.ULTRASONIC, false, "center_ultrasonic", "NO_SLOT", "CENTER_ULTRASONIC", Collections.emptyMap());
        ConfigurationComponent rightUltrasonic =
            new ConfigurationComponent(SC.ULTRASONIC, false, "right_ultrasonic", "NO_SLOT", "RIGHT_ULTRASONIC", Collections.emptyMap());
        ConfigurationComponent leftTouch = new ConfigurationComponent(SC.TOUCH, false, "left", "NO_SLOT", "LEFT_TOUCH", Collections.emptyMap());
        ConfigurationComponent rightTouch = new ConfigurationComponent(SC.TOUCH, false, "right", "NO_SLOT", "RIGHT_TOUCH", Collections.emptyMap());
        ConfigurationComponent leftDropoff = new ConfigurationComponent(SC.TOUCH, false, "left", "NO_SLOT", "LEFT_DROPOFF", Collections.emptyMap());
        ConfigurationComponent rightDropoff = new ConfigurationComponent(SC.TOUCH, false, "right", "NO_SLOT", "RIGHT_DROPOFF", Collections.emptyMap());
        ConfigurationComponent x = new ConfigurationComponent(SC.ACCELEROMETER, false, "x", "NO_SLOT", "X", Collections.emptyMap());
        ConfigurationComponent y = new ConfigurationComponent(SC.ACCELEROMETER, false, "y", "NO_SLOT", "Y", Collections.emptyMap());
        ConfigurationComponent z = new ConfigurationComponent(SC.ACCELEROMETER, false, "z", "NO_SLOT", "Z", Collections.emptyMap());
        ConfigurationComponent strength = new ConfigurationComponent(SC.ACCELEROMETER, false, "strength", "NO_SLOT", "STRENGTH", Collections.emptyMap());

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

    public ExpeditionConfiguration(String ipAddress, String portNumber, String userName, String password) {
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
     * @return text which defines the brick configuration
     */
    @Override
    public String generateText(String name) {
        return "";
    }

    /**
     * This class is a builder of {@link Configuration}
     */
    public static class Builder extends Configuration.Builder {
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
        public ExpeditionConfiguration build() {
            return new ExpeditionConfiguration(this.ipAddress, this.portNumber, this.userName, this.password);
        }

    }

}
