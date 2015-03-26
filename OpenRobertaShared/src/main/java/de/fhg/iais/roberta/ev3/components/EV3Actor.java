package de.fhg.iais.roberta.ev3.components;

import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.brickconfiguration.HardwareComponent;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.ev3.EV3Actors;
import de.fhg.iais.roberta.hardwarecomponents.Category;
import de.fhg.iais.roberta.hardwarecomponents.HardwareComponentType;

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
    public EV3Actor(EV3Actors componentType, boolean regulated, DriveDirection rotationDirection, MotorSide motorSide) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.motorSide == null) ? 0 : this.motorSide.hashCode());
        result = prime * result + (this.regulated ? 1231 : 1237);
        result = prime * result + ((this.rotationDirection == null) ? 0 : this.rotationDirection.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( !super.equals(obj) ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        EV3Actor other = (EV3Actor) obj;
        if ( this.motorSide != other.motorSide ) {
            return false;
        }
        if ( this.regulated != other.regulated ) {
            return false;
        }
        if ( this.rotationDirection != other.rotationDirection ) {
            return false;
        }
        return true;
    }

}
