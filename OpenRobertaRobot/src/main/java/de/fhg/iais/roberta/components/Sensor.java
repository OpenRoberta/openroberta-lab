package de.fhg.iais.roberta.components;

public class Sensor {

    private SensorType name;

    /**
     * Creates hardware component of type {@link Category#SENSOR} that will be attached to the brick configuration.
     * Client must provide valid {@link HardwareComponentType} from {@link Category#SENSOR} category.
     *
     * @param componentType of the sensor
     */
    public Sensor(SensorType sensorType) {
        this.setName(sensorType);
    }

    @Override
    public String toString() {
        return "Sensor [" + getName() + "]";
    }

    public SensorType getName() {
        return this.name;
    }

    public void setName(SensorType name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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
        Sensor other = (Sensor) obj;
        if ( this.name == null ) {
            if ( other.getName() != null ) {
                return false;
            }
        } else if ( !this.name.equals(other.getName()) ) {
            return false;
        }
        return true;
    }

}
