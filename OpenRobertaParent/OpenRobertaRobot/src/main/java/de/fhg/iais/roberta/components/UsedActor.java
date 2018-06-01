package de.fhg.iais.roberta.components;

import de.fhg.iais.roberta.inter.mode.action.IActorPort;

/**
 * Stores information for port and mode of used actor in a blockly program. This information is used for program validation.
 *
 * @author ensonic
 */

public class UsedActor {
    private final IActorPort port;
    private final ActorType type;

    public UsedActor(IActorPort port, ActorType type) {
        this.port = port;
        this.type = type;
    }

    /**
     * @return the port
     */
    public IActorPort getPort() {
        return this.port;
    }

    /**
     * @return the actorType
     */
    public ActorType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "UsedActor [" + this.port + ", " + this.type + "]";
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        try {
            result = prime * result + this.port.hashCode();
            result = prime * result + this.type.hashCode();
        } catch ( NullPointerException e ) {
            result = 31;
        }
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        UsedActor other = (UsedActor) obj;
        if ( this.type != other.type ) {
            return false;
        }
        if ( this.port == null ) {
            if ( other.port != null ) {
                return false;
            }
        } else if ( !this.port.equals(other.port) ) {
            return false;
        }
        return true;
    }
}
