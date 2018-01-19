package de.fhg.iais.roberta.components;

public class ConfigurationBlock {

    private final ConfigurationBlockType type;

    /**
     * Creates hardware component of type {@link Category#CONFIGURATION_BLOCK} that will be attached to the brick configuration. Client must provide valid
     * {@link HardwareComponentType} from {@link Category#CONFIGURATION_BLOCK} category.
     *
     * @param componentType of the sensor
     */
    public ConfigurationBlock(ConfigurationBlockType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ConfigurationBlock [" + getType() + "]";
    }

    public ConfigurationBlockType getType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.type == null) ? 0 : this.type.hashCode());
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
        ConfigurationBlock other = (ConfigurationBlock) obj;
        if ( this.type == null ) {
            if ( other.getType() != null ) {
                return false;
            }
        } else if ( !this.type.equals(other.getType()) ) {
            return false;
        }
        return true;
    }

}
