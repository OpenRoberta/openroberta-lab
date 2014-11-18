package de.fhg.iais.roberta.brickconfiguration.ev3;

import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.brickconfiguration.HardwareComponent;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.hardwarecomponents.Category;
import de.fhg.iais.roberta.hardwarecomponents.ev3.HardwareComponentEV3Actor;

public class EV3Actor extends HardwareComponent {
    private final boolean regulated;
    private final DriveDirection rotationDirection;
    private final MotorSide motorSide;

    /**
     * Creates hardware component of type {@link Category#ACTOR} that will be attached to the brick configuration.
     * Client must provide valid {@link HardwareComponentType} from {@link Category#ACTOR} category.
     *
     * @param regulated
     * @param componentType of the motor
     * @param rotationDirection rotation direction of the motor
     * @param motorSide on the brick
     */
    public EV3Actor(HardwareComponentEV3Actor componentType, boolean regulated, DriveDirection rotationDirection, MotorSide motorSide) {
        Assert.isTrue(componentType != null && rotationDirection != null && motorSide != null);
        this.setComponentType(componentType);
        this.regulated = regulated;
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
        return this.regulated;
    }

    @Override
    public String generateRegenerate() {
        StringBuilder sb = new StringBuilder();
        sb.append("new EV3Actor(").append(getComponentType().getJavaCode());
        sb.append(", ").append(this.regulated);
        sb.append(", ").append(this.rotationDirection.getJavaCode()).append(", ").append(this.motorSide.getJavaCode()).append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "EV3Actor [" + getComponentType() + ", " + this.regulated + ", " + this.rotationDirection + ", " + this.motorSide + "]";
    }

}
