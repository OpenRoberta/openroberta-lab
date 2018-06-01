package de.fhg.iais.roberta.components;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

/**
 * Stores information for port, type and the mode of used sensor in a blockly program. This information is shared between the server and the brick for sensor
 * debugging on the brick. If sensor is used then the brick is sending debug information to the server for this particular sensor.
 *
 * @author kcvejoski
 */

public class UsedSensor {
    private final ISensorPort port;
    private final ISensorType type;
    private final IMode mode;

    public UsedSensor(ISensorPort port, ISensorType type, IMode mode) {
        this.port = port;
        this.type = type;
        this.mode = mode;
    }

    /**
     * @return the port
     */
    public ISensorPort getPort() {
        return this.port;
    }

    /**
     * @return the sensorType
     */
    public ISensorType getType() {
        return this.type;
    }

    /**
     * @return the mode
     */
    public IMode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return "UsedSensor [" + this.port + ", " + this.type + ", " + this.mode + "]";
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.port == null ? 0 : this.port.hashCode());
        result = prime * result + (this.mode == null ? 0 : this.mode.hashCode());
        result = prime * result + this.type.hashCode();
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
        UsedSensor other = (UsedSensor) obj;
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
        if ( this.mode == null ) {
            if ( other.mode != null ) {
                return false;
            }
        } else if ( this.mode != other.mode ) {
            return false;
        }
        return true;
    }
}
