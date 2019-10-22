package de.fhg.iais.roberta.components;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;

public final class ConfigurationComponent extends Phrase<Void> {
    private final String componentType;

    private final boolean isActor;
    private final String userDefinedPortName;
    private final String portName;
    private final Map<String, String> componentProperties;

    public ConfigurationComponent(String componentType, boolean isActor, String portName, String userDefinedName, Map<String, String> componentProperties) {
        super(
            new BlockType(userDefinedName, Category.CONFIGURATION_BLOCK, ConfigurationComponent.class),
            BlocklyBlockProperties.make(componentType, "this-will-be-regenerated-anyway"),
            BlocklyComment.make("empty-comment", false, "10", "10"));
        this.componentType = componentType;
        this.isActor = isActor;
        this.portName = portName;
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

    public String getPortName() {
        return this.portName;
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
            + ", componentProperties="
            + this.componentProperties
            + "]";
    }

    @Override
    protected Void acceptImpl(IVisitor<Void> visitor) {
        // WE ACCEPT NOTHING!
        throw new DbcException("ConfigurationComponent should not be visited on it's own, instead the whole Configuration should be visited");
    }

    @Override
    public Block astToBlock() {
        Block destination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, destination);
        Ast2JaxbHelper.addField(destination, "NAME", this.userDefinedPortName);
        this.componentProperties.forEach((k, v) -> {
            Ast2JaxbHelper.addField(destination, k, v);
        });
        return destination;
    }

}
