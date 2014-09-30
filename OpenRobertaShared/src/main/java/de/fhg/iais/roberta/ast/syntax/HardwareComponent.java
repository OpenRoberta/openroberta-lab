package de.fhg.iais.roberta.ast.syntax;

import java.util.Arrays;

import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.HardwareComponentType;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class is represents hardware component (sensor or motor) that will be attached to the brick and it is used to set up a brick configuration.
 * All possible hardware components are listed in {@link HardwareComponentType} enum.
 */
public class HardwareComponent {
    private final HardwareComponentType componentType;
    private final DriveDirection rotationDirection;
    private final MotorSide motorSide;

    /**
     * Creates hardware component of type {@link Category#ACTOR} that will be attached to the brick configuration.
     * Client must provide valid {@link HardwareComponentType} from {@link Category#ACTOR} category.
     * 
     * @param componentType of the motor
     * @param rotationDirection rotation direction of the motor
     * @param motorSide on the brick
     */
    public HardwareComponent(HardwareComponentType componentType, DriveDirection rotationDirection, MotorSide motorSide) {
        Assert.isTrue(componentType != null && rotationDirection != null && motorSide != null);
        this.componentType = componentType;
        this.rotationDirection = rotationDirection;
        this.motorSide = motorSide;
    }

    /**
     * Creates hardware component of type {@link Category#SENSOR} that will be attached to the brick configuration.
     * Client must provide valid {@link HardwareComponentType} from {@link Category#SENSOR} category.
     * 
     * @param componentType of the sensor
     */
    public HardwareComponent(HardwareComponentType componentType) {
        Assert.isTrue(componentType.getCategory() == Category.SENSOR);
        this.componentType = componentType;
        this.rotationDirection = null;
        this.motorSide = null;
    }

    /**
     * @return type of the hardware component
     */
    public HardwareComponentType getComponentType() {
        return this.componentType;
    }

    /**
     * @return rotation direction for the motor
     */
    public DriveDirection getRotationDirection() {
        return this.rotationDirection;
    }

    /**
     * @return side on which the motor is connected
     */
    public MotorSide getMotorSide() {
        return this.motorSide;
    }

    /**
     * @return category in which the component belongs
     */
    public Category getCategory() {
        return this.componentType.getCategory();
    }

    /**
     * @return true if the motor is regulated
     */
    public boolean isRegulated() {
        return this.componentType.attributesMatchAttributes("regulated");
    }

    /**
     * @return Java code used in the code generation to regenerates the same hardware component
     */
    public String generateRegenerate() {
        StringBuilder sb = new StringBuilder();
        sb.append("new HardwareComponent(").append(this.componentType.getJavaCode());
        if ( getCategory() == Category.ACTOR ) {
            sb.append(", ").append(this.rotationDirection.getJavaCode()).append(", ").append(this.motorSide.getJavaCode());
        }
        sb.append(")");
        return sb.toString();
    }

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
                return new HardwareComponent(ct, dd, ms);
            } else {
                return new HardwareComponent(ct);
            }
        }
        throw new DbcException("No hardware component matches attributes " + Arrays.toString(attributes));
    }

    @Override
    public String toString() {
        return "HardwareComponent [componentType="
            + this.componentType
            + ", rotationDirection="
            + this.rotationDirection
            + ", motorSide="
            + this.motorSide
            + "]";
    }
}
