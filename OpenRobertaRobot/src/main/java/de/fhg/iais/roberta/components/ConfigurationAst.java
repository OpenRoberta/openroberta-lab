package de.fhg.iais.roberta.components;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * An AST representation of the old/new configurations.
 * Contains an insertion ordered Map of {@link ConfigurationComponent}s.
 * May have subclasses with hardcoded configurations which can reuse the {@link Builder} using the generic {@link Builder#build(Class)}.
 * TODO this subclassing should be removed and the class declared final once the hardcoded vorwerk configuration is removed
 */
public class ConfigurationAst {
    // LinkedHashMap to preserve insertion order of elements. Helps to recreate the same XML output as XML input.
    private final LinkedHashMap<String, ConfigurationComponent> configurationComponents;
    private final List<String> componentTypes;
    private final String robotType;
    private final String xmlVersion;
    private final String description;
    private final String tags;
    private final float wheelDiameter;
    private final float trackWidth;
    private final String ipAddress;
    private final String userName;
    private final String password;
    private String robotName;

    protected ConfigurationAst(
        Iterable<ConfigurationComponent> configurationComponents,
        String robotType,
        String xmlVersion,
        String description,
        String tags,
        float wheelDiameter,
        float trackWidth,
        String ipAddress,
        String userName,
        String password) {
        this.configurationComponents = buildConfigurationComponentMap(configurationComponents);
        this.robotType = robotType;
        this.xmlVersion = xmlVersion;
        this.description = description;
        this.tags = tags;
        this.wheelDiameter = wheelDiameter;
        this.trackWidth = trackWidth;
        this.ipAddress = ipAddress;
        this.userName = userName;
        this.password = password;
        this.componentTypes = new ArrayList<>();
        for ( ConfigurationComponent confComp : this.configurationComponents.values() ) {
            if ( isSuperBlock(confComp) ) {
                for ( Map.Entry<String, String> entry : confComp.getComponentProperties().entrySet() ) {
                    this.componentTypes.add(entry.getKey().split("_")[0]);
                }
            } else {
                this.componentTypes.add(confComp.getComponentType());
            }
        }
    }

    private static LinkedHashMap<String, ConfigurationComponent> buildConfigurationComponentMap(Iterable<ConfigurationComponent> configurationComponents) {
        LinkedHashMap<String, ConfigurationComponent> map = new LinkedHashMap<>();
        for ( ConfigurationComponent confComp : configurationComponents ) {
            map.put(confComp.getUserDefinedPortName(), confComp);
        }
        return map;
    }

    // TODO add better differentiation
    private static boolean isSuperBlock(ConfigurationComponent confComp) {
        return confComp.getComponentType().equals("CALLIBOT");
    }

    public String getRobotName() {
        return this.robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getRobotType() {
        return this.robotType;
    }

    public String getXmlVersion() {
        return this.xmlVersion;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTags() {
        return this.tags;
    }

    /**
     * @return the robot's wheel diameter in cm
     */
    public float getWheelDiameter() {
        return this.wheelDiameter;
    }

    /**
     * @return the robot's track width in cm
     */
    public float getTrackWidth() {
        return this.trackWidth;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the configuration component map.
     * While insertion order is preserved internally, it is unnecessary for code generation and similar tasks.
     * Here, the insertion ordered {@link LinkedHashMap} is wrapped in a {@link HashMap} to ensure a reproducible order.
     *
     * @return the configuration component map
     */
    public Map<String, ConfigurationComponent> getConfigurationComponents() {
        return new HashMap<>(this.configurationComponents);
    }

    public boolean isComponentTypePresent(String componentType) {
        return this.componentTypes.contains(componentType);
    }

    /**
     * Returns the values of the configuration component map.
     * While insertion order is preserved internally, it is unnecessary for code generation and similar tasks.
     * Here, the insertion ordered {@link LinkedHashMap} is wrapped in a {@link HashMap} to ensure a reproducible order.
     *
     * @return the values of the configuration component map
     */
    public Collection<ConfigurationComponent> getConfigurationComponentsValues() {
        return new HashMap<>(this.configurationComponents).values();
    }

    public ConfigurationComponent optConfigurationComponentByType(String type) {
        for ( ConfigurationComponent configComp : this.configurationComponents.values() ) {
            if ( configComp.getComponentType().equals(type) ) {
                return configComp;
            }
        }
        return null;
    }

    /**
     * Returns all actors in the configuration component map.
     * While insertion order is preserved internally, it is unnecessary for code generation and similar tasks.
     * Here, the insertion ordered {@link LinkedHashMap} is wrapped in a {@link HashMap} to ensure a reproducible order.
     *
     * @return all actors in the configuration component map
     */
    public Collection<ConfigurationComponent> getActors() {
        return new HashMap<>(this.configurationComponents).values().stream().filter(ConfigurationComponent::isActor).collect(Collectors.toList());
    }

    /**
     * Returns all sensors in the configuration component map.
     * While insertion order is preserved internally, it is unnecessary for code generation and similar tasks.
     * Here, the insertion ordered {@link LinkedHashMap} is wrapped in a {@link HashMap} to ensure a reproducible order.
     *
     * @return all sensors in the configuration component map
     */
    public Collection<ConfigurationComponent> getSensors() {
        return new HashMap<>(this.configurationComponents).values().stream().filter(ConfigurationComponent::isSensor).collect(Collectors.toList());
    }

    public ConfigurationComponent getConfigurationComponent(String userDefinedName) {
        ConfigurationComponent confComp = this.configurationComponents.get(userDefinedName);
        if ( confComp == null ) {
            confComp =
                this.configurationComponents
                    .values()
                    .stream()
                    .filter(ConfigurationAst::isSuperBlock)
                    .filter(cc -> cc.getComponentProperties().entrySet().stream().anyMatch(entry -> entry.getValue().equals(userDefinedName)))
                    .findFirst()
                    .orElseThrow(() -> new DbcException("configuration component missing for user defined name " + userDefinedName));
        }
        Assert.notNull(confComp, "configuration component missing for user defined name " + userDefinedName);
        return confComp;
    }

    public ConfigurationComponent optConfigurationComponent(String userDefinedName) {
        ConfigurationComponent confComp = this.configurationComponents.get(userDefinedName);
        if ( confComp == null ) {
            confComp =
                this.configurationComponents
                    .values()
                    .stream()
                    .filter(ConfigurationAst::isSuperBlock)
                    .filter(cc -> cc.getComponentProperties().entrySet().stream().anyMatch(entry -> entry.getValue().equals(userDefinedName)))
                    .findFirst()
                    .orElse(null);
        }
        return confComp;
    }

    public String getFirstMotorPort(String side) {
        return getFirstMotor(side).getUserDefinedPortName();
    }

    public ConfigurationComponent getFirstMotor(String side) {
        List<ConfigurationComponent> found = getMotors(side);
        return found.isEmpty() ? null : found.get(0);
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

    /**
     * This class is a builder of {@link ConfigurationAst}
     */
    public static class Builder {
        private List<ConfigurationComponent> configurationComponents = new ArrayList<>();

        private String robotType = "";
        private String xmlVersion = "";
        private String description = "";
        private String tags = "";

        private float wheelDiameter = 0.0f;
        private float trackWidth = 0.0f;
        private String ipAddress = "";
        private String userName = "";
        private String password = "";

        public Builder setRobotType(String robotType) {
            this.robotType = robotType;
            return this;
        }

        public Builder setXmlVersion(String xmlVersion) {
            this.xmlVersion = xmlVersion;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setTags(String tags) {
            this.tags = tags;
            return this;
        }

        /**
         * Sets the wheel diameter in cm.
         *
         * @param wheelDiameter the wheel diameter in cm
         */
        public Builder setWheelDiameter(float wheelDiameter) {
            this.wheelDiameter = wheelDiameter;
            return this;
        }

        /**
         * Sets the track width in cm.
         *
         * @param trackWidth the track width in cm
         */

        public Builder setTrackWidth(float trackWidth) {
            this.trackWidth = trackWidth;
            return this;
        }

        public Builder setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
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
         * Client must provide list of hardware components ({@link ConfigurationComponent}).
         *
         * @param components we want to connect to the brick configuration
         */
        public Builder addComponents(Collection<ConfigurationComponent> components) {
            this.configurationComponents.addAll(components);
            return this;
        }

        /**
         * Builds the {@link ConfigurationAst} from the provided.
         *
         * @return an instance of the built configuration
         */
        public ConfigurationAst build() {
            return new ConfigurationAst(
                this.configurationComponents,
                this.robotType,
                this.xmlVersion,
                this.description,
                this.tags,
                this.wheelDiameter,
                this.trackWidth,
                this.ipAddress,
                this.userName,
                this.password);
        }

        /**
         * Builds the configuration for a potential hardcoded subclass of {@link ConfigurationAst}.
         * Subclasses of {@link ConfigurationAst} should provide a private constructor matching the constructor arguments of {@link ConfigurationAst}.
         * TODO should be removed once the hardcoded vorwerk configuration is removed
         *
         * @param configAstClass the class of {@link ConfigurationAst} to be built
         * @param <C> the built subtype of {@link ConfigurationAst}
         * @return an instance of the provided child configuration of {@link ConfigurationAst}
         */
        public <C extends ConfigurationAst> C build(Class<C> configAstClass) {
            try {
                Constructor<C> declaredConstructor =
                    configAstClass
                        .getDeclaredConstructor(
                            Collection.class,
                            String.class,
                            String.class,
                            String.class,
                            String.class,
                            Float.TYPE,
                            Float.TYPE,
                            String.class,
                            String.class,
                            String.class);
                declaredConstructor.setAccessible(true);
                return declaredConstructor
                    .newInstance(
                        this.configurationComponents,
                        this.robotType,
                        this.xmlVersion,
                        this.description,
                        this.tags,
                        this.wheelDiameter,
                        this.trackWidth,
                        this.ipAddress,
                        this.userName,
                        this.password);
            } catch ( IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException
                | IllegalArgumentException e ) {
                throw new DbcException("configuration class " + configAstClass.getSimpleName() + " has no constructor usable by the configuration builder", e);
            }
        }
    }
}
