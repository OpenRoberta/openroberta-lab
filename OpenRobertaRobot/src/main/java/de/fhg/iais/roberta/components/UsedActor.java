package de.fhg.iais.roberta.components;

/**
 * Stores information for port and mode of used actor in a blockly program. This information is used for program validation.
 *
 * @author ensonic
 */

public class UsedActor {
    private final String port;
    private final String type;

    public UsedActor(String port, String type) {
        this.port = port;
        this.type = type;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return this.port;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.port == null) ? 0 : this.port.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
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
        UsedActor other = (UsedActor) obj;
        if ( this.port == null ) {
            if ( other.port != null ) {
                return false;
            }
        } else if ( !this.port.equals(other.port) ) {
            return false;
        }
        if ( this.type == null ) {
            if ( other.type != null ) {
                return false;
            }
        } else if ( !this.type.equals(other.type) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UsedActor [" + this.port + ", " + this.type + "]";
    }

}
