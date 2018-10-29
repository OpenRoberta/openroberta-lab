package de.fhg.iais.roberta.components;

import java.util.Map;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * Stores information for type, name, ports and pins of used configuration blocks in a blockly program.
 * This information is shared between the server and the brick for configuration block
 * debugging on the brick. If configuration block is used then the brick is sending debug information to the server for this particular sensor.
 *
 * @author eovchinnikova
 */

public class UsedConfigurationBlockO {
    private final IConfigurationBlockType type;
    private final String blockName;
    private final Map confPorts;

    public UsedConfigurationBlockO(IConfigurationBlockType type, String blockName, Map confPorts) {
        this.type = type;
        this.blockName = blockName;
        this.confPorts = confPorts;
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
    public Map getConfPorts() {
        return this.confPorts;
    }

    /**
     * @return the configuration block type
     */
    public String getConfPortOf(String portName) {
        if ( portName == null || this.confPorts.get(portName) == null ) {
            throw new DbcException("No configuration port for: " + portName);
        }
        return (String) this.confPorts.get(portName);
    }

    @Override
    public String toString() {
        // TODO
        return " ";
        //return "UsedSensor [" + this.type + ", " + this.blockName + ", " + this.ports + ", " + this.pins + ", " + "]";
    }
}
