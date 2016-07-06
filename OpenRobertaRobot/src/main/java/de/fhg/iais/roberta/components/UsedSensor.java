package de.fhg.iais.roberta.components;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

/**
 * Stores information for port, type and the mode of used sensor in a blockly program.
 * This information is shared between the server and the brick for sensor debugging on the brick.
 * If sensor is used then the brick is sending debug information to the server for this particular sensor.
 *
 * @author kcvejoski
 */
@SuppressWarnings("rawtypes")
public class UsedSensor {
    private final ISensorPort port;
    private final SensorType sensor;
    private final Enum mode;

    public UsedSensor(ISensorPort port, SensorType sensor, Enum mode) {
        this.port = port;
        this.sensor = sensor;
        this.mode = mode;
    }

    public UsedSensor(ISensorPort port, SensorType sensor, IMode mode) {
        this.port = port;
        this.sensor = sensor;
        this.mode = (Enum) mode;
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
    public SensorType getSensorType() {
        return this.sensor;
    }

    /**
     * @return the mode
     */
    public Enum getMode() {
        return this.mode;
    }

    /**
     * @return valid Java code for generating blockly program
     */
    public String generateRegenerate() {
        StringBuilder sb = new StringBuilder();
        sb.append("new UsedSensor(");
        sb.append("SensorPort." + this.port.toString()).append(", ");
        sb.append(this.sensor.getClass().getSimpleName() + "." + this.sensor.name()).append(", ");
        sb.append(this.mode.getClass().getSimpleName() + "." + this.mode.toString()).append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "UsedSensor [" + this.port + ", " + this.sensor + ", " + this.mode + "]";
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.port.hashCode();
        result = prime * result + (this.mode == null ? 0 : this.mode.hashCode());
        result = prime * result + this.sensor.hashCode();
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
        if ( this.sensor != other.sensor ) {
            return false;
        }
        if ( this.port == null ) {
            if ( other.port != null ) {
                return false;
            }
        } else if ( this.port != other.port ) {
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
