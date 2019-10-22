package de.fhg.iais.roberta.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * This class represents model of the hardware configuration of a robot (assume we have "left" and "right" motor). It is used in the code generation. <br>
 * <br>
 * The {@link ConfigurationAst} contains four sensor ports and four actor ports. Client cannot connect more than that.
 */
public class ConfigurationAst {
    protected final Map<String, ConfigurationComponent> configurationComponents;

    private String robotName;
    private final float wheelDiameterCM;
    private final float trackWidthCM;
    private final List<String> componentTypes;

    private String ipAddress;
    private String portNumber;
    private String userName;
    private String password;

    public ConfigurationAst(Collection<ConfigurationComponent> configurationComponents, float wheelDiameterCM, float trackWidthCM) {
        this.configurationComponents = buildConfigurationComponentMap(configurationComponents);
        this.wheelDiameterCM = wheelDiameterCM;
        this.trackWidthCM = trackWidthCM;
        this.componentTypes = new ArrayList<>();
        for ( ConfigurationComponent configurationComponent : this.configurationComponents.values() ) {
            this.componentTypes.add(configurationComponent.getComponentType());
        }
    }

    public void accept(IVisitor<Void> visitor) {
        for ( ConfigurationComponent configurationComponent : this.configurationComponents.values() ) {
            configurationComponent.acceptImpl(visitor);
        }
    }

    public String getRobotName() {
        return this.robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public HashMap<String, ConfigurationComponent> getConfigurationComponents() {
        return (HashMap<String, ConfigurationComponent>) this.configurationComponents;
    }

    public boolean isComponentTypePresent(String componentType) {
        if ( this.componentTypes.contains(componentType) ) {
            return true;
        } else {
            return false;
        }
    }

    public Collection<ConfigurationComponent> getConfigurationComponentsValues() {
        return this.configurationComponents.values();
    }

    public ConfigurationComponent getConfigurationComponentbyType(String type) {
        for ( ConfigurationComponent cc : this.configurationComponents.values() ) {
            if ( cc.getComponentType().equals(type) ) {
                return cc;
            }
        }
        return null;
    }

    public Collection<ConfigurationComponent> getActors() {
        return this.configurationComponents.values().stream().filter(item -> item.isActor()).collect(Collectors.toList());
    }

    public Collection<ConfigurationComponent> getSensors() {
        return this.configurationComponents.values().stream().filter(item -> item.isSensor()).collect(Collectors.toList());
    }

    public ConfigurationComponent getConfigurationComponent(String userDefinedName) {
        ConfigurationComponent configurationComponent = this.configurationComponents.get(userDefinedName);
        Assert.notNull(configurationComponent, "configuration component missing for user defined name " + userDefinedName);
        return configurationComponent;
    }

    public ConfigurationComponent optConfigurationComponent(String userDefinedName) {
        ConfigurationComponent configurationComponent = this.configurationComponents.get(userDefinedName);
        return configurationComponent;
    }

    public String getComponentTypeOfUserDefinedName(String userDefinedName) {
        ConfigurationComponent configurationComponent = getConfigurationComponent(userDefinedName);
        return configurationComponent.getComponentType();
    }

    public float getWheelDiameterCM() {
        return this.wheelDiameterCM;
    }

    public float getTrackWidthCM() {
        return this.trackWidthCM;
    }

    public String getFirstMotorPort(String side) {
        return getFirstMotor(side).getUserDefinedPortName();
    }

    public ConfigurationComponent getFirstMotor(String side) {
        List<ConfigurationComponent> found = getMotors(side);
        if ( found.size() == 0 ) {
            return null;
        } else {
            return found.get(0);
        }
    }

    public List<ConfigurationComponent> getMotors(String side) {
        List<ConfigurationComponent> found = new ArrayList<>();
        for ( ConfigurationComponent component : this.configurationComponents.values() ) {
            if ( component.isActor() && side.equals(component.getOptProperty(SC.MOTOR_DRIVE)) ) {
                found.add(component);
            }
        }
        return found;
    }

    public boolean isMotorRegulated(String port) {
        if ( getConfigurationComponent(port).getOptProperty(SC.MOTOR_REGULATION) == null ) {
            return false;
        } else {
            return getConfigurationComponent(port).getOptProperty(SC.MOTOR_REGULATION).equals(SC.TRUE);
        }
    }

    private Map<String, ConfigurationComponent> buildConfigurationComponentMap(Collection<ConfigurationComponent> configurationComponents) {
        Map<String, ConfigurationComponent> map = new HashMap<>();
        for ( ConfigurationComponent configurationComponent : configurationComponents ) {
            map.put(configurationComponent.getUserDefinedPortName(), configurationComponent);
        }
        return map;
    }

    public BlockSet generateBlockSet() {
        final BlockSet blockSet = new BlockSet();
        this.configurationComponents.values().forEach(v -> {
            final Instance instance = new Instance();
            blockSet.getInstance().add(instance);
            instance.getBlock().add(v.astToBlock());
        });
        return blockSet;
    }

    /**
     * @return text which defines the brick configuration
     */
    public String generateText(String name) {
        return "";
    }

    /**
     * This class is a builder of {@link ConfigurationAst}
     */
    public static class Builder {
        private List<ConfigurationComponent> configurationComponents = new ArrayList<>();

        private float wheelDiameter;
        private float trackWidth;
        private String ipAddress;
        private String portNumber;
        private String userName;
        private String password;

        public Builder setIpAddress(String ipAddress) {
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

        /**
         * Client must provide list of hardware components ({@link ConfigurationComponent})
         *
         * @param sensors we want to connect to the brick configuration
         * @return
         */
        public Builder addComponents(List<ConfigurationComponent> components) {
            this.configurationComponents = components;
            return this;
        }

        /**
         * Set the wheel diameter
         *
         * @param wheelDiameter in cm
         * @return
         */
        public Builder setWheelDiameter(float wheelDiameter) {
            this.wheelDiameter = wheelDiameter;
            return this;
        }

        /**
         * Set the track width
         *
         * @param trackWidth in cm
         * @return
         */

        public Builder setTrackWidth(float trackWidth) {
            this.trackWidth = trackWidth;
            return this;
        }

        public ConfigurationAst build() {
            ConfigurationAst configurationAst = new ConfigurationAst(this.configurationComponents, this.wheelDiameter, this.trackWidth);
            configurationAst.ipAddress = this.ipAddress;
            configurationAst.portNumber = this.portNumber;
            configurationAst.userName = this.userName;
            configurationAst.password = this.password;
            return configurationAst;
        }

        public <CC> CC build(Class<CC> clazz) {
            try {
                return clazz.getConstructor(Collection.class).newInstance(this.configurationComponents);
            } catch ( Exception e ) {
                throw new DbcException("configuration class " + clazz.getSimpleName() + " has no constructor usable by the configuration builder", e);
            }
        }
    }
}
