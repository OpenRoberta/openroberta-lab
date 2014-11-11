package de.fhg.iais.roberta.ast.syntax;

import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.HardwareComponentType;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.dbc.Assert;

public class EV3Actor extends HardwareComponent {
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
    public EV3Actor(HardwareComponentType componentType, DriveDirection rotationDirection, MotorSide motorSide) {
        Assert.isTrue(componentType.getCategory() == Category.ACTOR && rotationDirection != null && motorSide != null);
        this.setComponentType(componentType);
        this.rotationDirection = rotationDirection;
        this.motorSide = motorSide;
    }

    /**
     * @return side on which the motor is connected
     */
    public MotorSide getMotorSide() {
        return this.motorSide;
    }

    /**
     * @return rotation direction for the motor
     */
    public DriveDirection getRotationDirection() {
        return this.rotationDirection;
    }

    /**
     * @return true if the motor is regulated
     */
    public boolean isRegulated() {
        return getComponentType().attributesMatchAttributes("regulated");
    }

    @Override
    public String generateRegenerate() {
        StringBuilder sb = new StringBuilder();
        sb.append("new EV3Actor(").append(getComponentType().getJavaCode());
        sb.append(", ").append(this.rotationDirection.getJavaCode()).append(", ").append(this.motorSide.getJavaCode()).append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "EV3Actor [" + getComponentType() + ", " + this.rotationDirection + ", " + this.motorSide + "]";
    }

}
