package de.fhg.iais.roberta.components;

import java.util.List;

/**
 * Stores information for type, name, ports and pins of used configuration blocks in a blockly program.
 * This information is shared between the server and the brick for configuration block
 * debugging on the brick. If configuration block is used then the brick is sending debug information to the server for this particular sensor.
 *
 * @author eovchinnikova
 */

public class UsedConfigurationBlock {
    private final IConfigurationBlockType type;
    private final String blockName;
    private final List<String> ports;
    private final List<String> pins;

    public UsedConfigurationBlock(IConfigurationBlockType type, String blockName, List<String> ports, List<String> pins) {
        this.type = type;
        this.blockName = blockName;
        this.ports = ports;
        this.pins = pins;
    }

    /**
     * @return the configuration block type
     */
    public IConfigurationBlockType getType() {
        return this.type;
    }

    /**
     * @return the configuration block type
     */
    public String getBlockName() {
        return this.blockName.toUpperCase();
    }

    /**
     * @return the configuration block type
     */
    public List<String> getPorts() {
        return this.ports;
    }

    /**
     * @return the configuration block type
     */
    public List<String> getPins() {
        return this.pins;
    }

    @Override
    public String toString() {
        return "UsedSensor [" + this.type + ", " + this.blockName + ", " + this.ports + ", " + this.pins + ", " + "]";
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (this.blockName == null ? 0 : this.blockName.hashCode());
        result = (prime * result) + (this.ports == null ? 0 : this.ports.hashCode());
        result = (prime * result) + (this.pins == null ? 0 : this.pins.hashCode());
        result = (prime * result) + this.type.hashCode();
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
        UsedConfigurationBlock other = (UsedConfigurationBlock) obj;
        if ( this.type != other.type ) {
            return false;
        }
        if ( this.blockName == null ) {
            if ( other.blockName != null ) {
                return false;
            }
        } else if ( this.blockName != other.blockName ) {
            return false;
        }
        if ( this.ports == null ) {
            if ( other.ports != null ) {
                return false;
            }
        } else if ( this.ports != other.ports ) {
            return false;
        }
        if ( this.pins == null ) {
            if ( other.pins != null ) {
                return false;
            }
        } else if ( this.pins != other.pins ) {
            return false;
        }
        return true;
    }
}
