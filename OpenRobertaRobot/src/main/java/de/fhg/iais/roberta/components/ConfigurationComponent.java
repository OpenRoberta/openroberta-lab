package de.fhg.iais.roberta.components;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAst;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * Representation of old/new configuration blocks in the AST.
 * May have subclasses which override {@link ConfigurationComponent#astToBlock()} in order to implement changed behaviour,
 * these must follow the naming structure defined in {@link Jaxb2ConfigurationAst#BRICK_BLOCK_PATTERN} as implemented over in
 * {@link Jaxb2ConfigurationAst#instance2NewConfigComp(Instance, BlocklyDropdownFactory)}
 * TODO this subclassing should be removed and the class declared final if possible
 */
public class ConfigurationComponent extends Phrase<Void> {

    private final String componentType;
    private final boolean isActor; // TODO for the new configuration there is currently no distinction between actors and sensors, should there be one?
    private final String userDefinedPortName;
    private final String internalPortName;
    private final LinkedHashMap<String, String> componentProperties;
    private final int x;
    private final int y;

    /**
     * Should only be used by tests!
     * TODO remove this if possible
     */
    public ConfigurationComponent(
        String componentType,
        boolean isActor,
        String internalPortName,
        String userDefinedName,
        Map<String, String> componentProperties) {
        this(
            componentType,
            isActor,
            internalPortName,
            userDefinedName,
            componentProperties,
            BlocklyBlockProperties.make(componentType, "this-will-be-regenerated-anyway"),
            BlocklyComment.make("empty-comment", false, "10", "10"),
            0,
            0);
    }

    /**
     * Creates a configuration component. Should only be used by {@link Jaxb2ConfigurationAst}.
     *
     * @param componentType the type of configuration component
     * @param isActor whether the component is an actor or not
     * @param internalPortName the internal port name, may represent the name used for generating code
     * @param userDefinedName the user defined port name
     * @param componentProperties the map of component properties
     * @param properties the blockly block properties
     * @param comment the blockly comment
     * @param x the x location
     * @param y the y location
     */
    public ConfigurationComponent(
        String componentType,
        boolean isActor,
        String internalPortName,
        String userDefinedName,
        Map<String, String> componentProperties,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int x,
        int y) {
        super(new BlockType(userDefinedName, Category.CONFIGURATION_BLOCK, ConfigurationComponent.class), properties, comment);
        this.componentType = componentType;
        this.isActor = isActor;
        this.internalPortName = internalPortName;
        this.userDefinedPortName = userDefinedName;
        this.componentProperties = new LinkedHashMap<>(componentProperties);
        this.x = x;
        this.y = y;
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
        return getProperty(SC.MOTOR_REGULATION).equals(SC.TRUE);
    }

    public boolean isReverse() {
        return getProperty(SC.MOTOR_REVERSE).equals(SC.ON);
    }

    public String getSide() {
        switch ( getProperty(SC.MOTOR_DRIVE) ) {
            case SC.LEFT:
                return SC.LEFT;
            case SC.RIGHT:
                return SC.RIGHT;
            default:
                return SC.NONE;
        }
    }

    public String getInternalPortName() {
        return this.internalPortName;
    }

    public String getUserDefinedPortName() {
        return this.userDefinedPortName;
    }

    public Map<String, String> getComponentProperties() {
        return Collections.unmodifiableMap(this.componentProperties);
    }

    public String getProperty(String propertyName) {
        Assert.nonEmptyString(propertyName, "No valid property name %s", propertyName);
        String propertyValue = this.componentProperties.get(propertyName);
        Assert.notNull(propertyValue, "No property with name %s", propertyName);

        return propertyValue;
    }

    public String getOptProperty(String propertyName) {
        Assert.nonEmptyString(propertyName, "No valid property name %s", propertyName);
        return this.componentProperties.get(propertyName);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "ConfigurationComponent ["
            + "componentType="
            + this.componentType
            + ", isActor="
            + this.isActor
            + ", userDefinedName="
            + this.userDefinedPortName
            + ", portName="
            + this.internalPortName
            + ", componentProperties="
            + this.componentProperties
            + "]";
    }

    @Override
    protected Void acceptImpl(IVisitor<Void> visitor) {
        // TODO should this be rethought? ConfigurationComponents now act completely independent from the visitor pattern
        throw new DbcException("ConfigurationComponent should not be visited on it's own, instead the whole Configuration should be visited");
    }

    /**
     * Creates a block from the AST representation.
     * Can be overridden by creating a subclass named according to {@link Jaxb2ConfigurationAst#BRICK_BLOCK_PATTERN} this method is then called via reflection
     * in {@link Jaxb2ConfigurationAst#instance2NewConfigComp(Instance, BlocklyDropdownFactory)}
     * TODO this subclassing should be removed if possible
     *
     * @return the generated block
     */
    @Override
    public Block astToBlock() {
        Block destination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, destination);
        Ast2JaxbHelper.addField(destination, "NAME", this.userDefinedPortName);
        this.componentProperties.forEach((key, value) -> Ast2JaxbHelper.addField(destination, key, value));
        return destination;
    }

}
