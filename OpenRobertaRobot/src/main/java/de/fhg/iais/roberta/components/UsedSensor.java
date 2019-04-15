package de.fhg.iais.roberta.components;

/**
 * Stores information for port, type and the mode of used sensor in a blockly program. This information is shared between the server and the brick for sensor
 * debugging on the brick. If sensor is used then the brick is sending debug information to the server for this particular sensor.
 *
 * @author kcvejoski
 */

public class UsedSensor {
    private final String port;
    private final String type;
    private final String mode;

    public UsedSensor(String port, String type, String mode) {
        this.port = port;
        this.type = type;
        this.mode = mode;
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

    /**
     * @return the mode
     */
    public String getMode() {
        return this.mode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.mode == null) ? 0 : this.mode.hashCode());
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
        UsedSensor other = (UsedSensor) obj;
        if ( this.mode == null ) {
            if ( other.mode != null ) {
                return false;
            }
        } else if ( !this.mode.equals(other.mode) ) {
            return false;
        }
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
        return "UsedSensor [" + this.port + ", " + this.type + ", " + this.mode + "]";
    }

}
