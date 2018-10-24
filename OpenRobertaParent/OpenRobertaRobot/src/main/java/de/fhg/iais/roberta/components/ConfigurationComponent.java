package de.fhg.iais.roberta.components;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.util.dbc.Assert;

public final class ConfigurationComponent {
    private final String componentType;

    private final boolean isActor;
    private final String userDefinedPortName;
    private final String portName;
    private final String slotName;
    private final Map<String, String> componentProperties;

    public ConfigurationComponent(
        String compnentType,
        boolean isActor,
        String portName,
        String slotName,
        String userDefinedName,
        Map<String, String> componentProperties) {
        this.componentType = compnentType;
        this.isActor = isActor;
        this.portName = portName;
        this.slotName = slotName;
        this.userDefinedPortName = userDefinedName;
        this.componentProperties = Collections.unmodifiableMap(new HashMap<>(componentProperties));
    }

    public String getComponentType() {
        return this.componentType;
    }

    public boolean isActor() {
        return this.isActor;
    }

    public boolean isSensor() {
        return !this.isActor;
    }

    public boolean isRegulated() {
        return this.getProperty(SC.MOTOR_REGULATION).equals(SC.TRUE);
    }

    public boolean isReverse() {
        return this.getProperty(SC.MOTOR_REVERSE).equals(SC.ON);
    }

    public String getPortName() {
        return this.portName;
    }

    public String getSlotName() {
        return this.slotName;
    }

    public String getUserDefinedPortName() {
        return this.userDefinedPortName;
    }

    public Map<String, String> getComponentProperties() {
        return this.componentProperties;
    }

    public String getProperty(String propertyName) {
        Assert.nonEmptyString(propertyName, "No valid property name %s", propertyName);
        String propertyValue = this.componentProperties.get(propertyName);
        Assert.notNull(propertyValue, "No property with name %s", propertyName);

        return propertyValue;
    }

    public String getOptProperty(String propertyName) {
        Assert.nonEmptyString(propertyName, "No valid property name %s", propertyName);
        String propertyValue = this.componentProperties.get(propertyName);

        return propertyValue;
    }

    @Override
    public String toString() {
        return "ConfigurationComponent ["
            + "isActor="
            + this.isActor
            + ", userDefinedName="
            + this.userDefinedPortName
            + ", portName="
            + this.portName
            + ", slotName="
            + this.slotName
            + ", componentProperties="
            + this.componentProperties
            + "]";
    }

}
