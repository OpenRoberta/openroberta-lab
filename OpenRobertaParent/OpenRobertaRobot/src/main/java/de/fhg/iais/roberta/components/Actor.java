package de.fhg.iais.roberta.components;

import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.IMotorSide;
import de.fhg.iais.roberta.util.dbc.Assert;

public class Actor {
    private final ActorType name;
    private final boolean regulated;
    private final IDriveDirection rotationDirection;
    private final IMotorSide motorSide;

    /**
     * Creates hardware component of type {@link Category#ACTOR} that will be attached to the brick configuration. Client must provide valid
     * {@link HardwareComponentType} from {@link Category#ACTOR} category.
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
        int result = 1;
        result = prime * result + ((motorSide == null) ? 0 : motorSide.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (regulated ? 1231 : 1237);
        result = prime * result + ((rotationDirection == null) ? 0 : rotationDirection.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        Actor other = (Actor) obj;
        if ( motorSide == null ) {
            if ( other.motorSide != null ) {
                return false;
            }
        } else if ( !motorSide.equals(other.motorSide) ) {
            return false;
        }
        if ( name != other.name ) {
            return false;
        }
        if ( regulated != other.regulated ) {
            return false;
        }
        if ( rotationDirection == null ) {
            if ( other.rotationDirection != null ) {
                return false;
            }
        } else if ( !rotationDirection.equals(other.rotationDirection) ) {
            return false;
        }
        return true;
    }

    public ActorType getName() {
        return this.name;
    }

}
