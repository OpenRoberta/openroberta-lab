package de.fhg.iais.roberta.ast.syntax;

import java.util.Arrays;

import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.HardwareComponentType;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

abstract public class HardwareComponent {
    private HardwareComponentType componentType;

    public HardwareComponent() {
        super();
    }

    /**
     * Set a component type
     *
     * @param componentType
     */
    public void setComponentType(HardwareComponentType componentType) {
        this.componentType = componentType;
    }

    /**
     * @return type of the hardware component
     */
    public HardwareComponentType getComponentType() {
        Assert.isTrue(this.componentType != null, "Hardware type is null!");
        return this.componentType;
    }

    /**
     * @return category in which the component belongs
     */
    public Category getCategory() {
        return this.componentType.getCategory();
    }

    /**
     * @return Java code used in the code generation to regenerates the same hardware component
     */
    abstract public String generateRegenerate();

    /**
     * Searches hardware component by the given attributes. If there is no hardware component that matches all attributes exception is thrown.
     *
     * @param attributes for given hardware component
     * @return hardware component with given attributes
     */
    public static HardwareComponent attributesMatch(String... attributes) {
        HardwareComponentType ct = HardwareComponentType.attributesMatch(attributes);
        if ( ct != null ) {
            if ( ct.getCategory() == Category.ACTOR ) {
                DriveDirection dd = DriveDirection.attributesMatch(attributes);
                MotorSide ms = MotorSide.attributesMatch(attributes);
                return new EV3Actor(ct, dd, ms);
            } else {
                return new EV3Sensor(ct);
            }
        }
        throw new DbcException("No hardware component matches attributes " + Arrays.toString(attributes));
    }
}
