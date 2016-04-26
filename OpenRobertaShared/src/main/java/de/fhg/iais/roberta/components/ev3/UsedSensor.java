package de.fhg.iais.roberta.components.ev3;

import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;

/**
 * Stores information for port, type and the mode of used sensor in a blockly program.
 * This information is shared between the server and the brick for sensor debugging on the brick.
 * If sensor is used then the brick is sending debug information to the server for this particular sensor.
 *
 * @author kcvejoski
 */
@SuppressWarnings("rawtypes")
public class UsedSensor {
    private final SensorPort port;
    private final EV3Sensors sensorType;
    private final Enum mode;

    public UsedSensor(SensorPort port, EV3Sensors sensorType, Enum mode) {
        this.port = port;
        this.sensorType = sensorType;
        this.mode = mode;
    }

    /**
     * @return the port
     */
    public SensorPort getPort() {
        return this.port;
    }

    /**
     * @return the sensorType
     */
    public EV3Sensors getSensorType() {
        return this.sensorType;
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
        sb.append("SensorPort." + this.port.name()).append(", ");
        sb.append(this.sensorType.getClass().getSimpleName() + "." + this.sensorType.getTypeName()).append(", ");
        sb.append(this.mode.getClass().getSimpleName() + "." + this.mode.name()).append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "[" + this.port + ", " + this.sensorType + ", " + this.mode + "]";
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.port.hashCode();
        result = prime * result + (this.mode == null ? 0 : this.mode.hashCode());
        result = prime * result + this.sensorType.hashCode();
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
        if ( this.sensorType != other.sensorType ) {
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
