package de.fhg.iais.roberta.components;

import de.fhg.iais.roberta.factory.action.IDriveDirection;
import de.fhg.iais.roberta.factory.action.IMotorSide;
import de.fhg.iais.roberta.util.dbc.Assert;

public class Actor {
    private final ActorType name;
    private final boolean regulated;
    private final IDriveDirection rotationDirection;
    private final IMotorSide motorSide;

    /**
     * Creates hardware component of type {@link Category#ACTOR} that will be attached to the brick configuration.
     * Client must provide valid {@link HardwareComponentType} from {@link Category#ACTOR} category.
     *
     * @param regulated
     * @param componentType of the motor
     * @param rotationDirection rotation direction of the motor
     * @param motorSide on the brick
     */
    public Actor(ActorType actorName, boolean regulated, IDriveDirection rotationDirection, IMotorSide motorSide) {
        Assert.isTrue(rotationDirection != null && motorSide != null);
        this.name = actorName;
        this.regulated = regulated;
        this.rotationDirection = rotationDirection;
        this.motorSide = motorSide;
    }

    /**
     * @return side on which the motor is connected
     */
    public IMotorSide getMotorSide() {
        return this.motorSide;
    }

    /**
     * @return rotation direction for the motor
     */
    public IDriveDirection getRotationDirection() {
        return this.rotationDirection;
    }

    /**
     * @return true if the motor is regulated
     */
    public boolean isRegulated() {
        return this.regulated;
    }

    @Override
    public String toString() {
        return "Actor [" + getName() + ", " + this.regulated + ", " + this.rotationDirection + ", " + this.motorSide + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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

        if ( getClass() != obj.getClass() ) {
            return false;
        }
        Actor other = (Actor) obj;
        if ( this.motorSide != other.motorSide ) {
            return false;
        }
        if ( !this.name.equals(other.name) ) {
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

    public ActorType getName() {
        return this.name;
    }

}
