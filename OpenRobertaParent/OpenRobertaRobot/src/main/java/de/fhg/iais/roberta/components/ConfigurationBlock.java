package de.fhg.iais.roberta.components;

import java.util.Map;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 */
public class ConfigurationBlock {
    final private IConfigurationBlockType confType;
    final private String confName;
    final private Map<String, String> confPorts;

    public ConfigurationBlock(IConfigurationBlockType type, String confName, Map<String, String> confPorts) {
        this.confType = type;
        this.confName = confName.toUpperCase();
        this.confPorts = confPorts;
    }

    /**
     * @return the confType
     */
    public IConfigurationBlockType getConfType() {
        return this.confType;
    }

    /**
     * @return the confName
     */
    public String getConfName() {
        return this.confName;
    }

    /**
     * @return the confPorts
     */
    public Map<String, String> getConfPorts() {
        return this.confPorts;
    }

    /**
     * @param name
     * @return
     */
    public String getConfPortOf(String name) {
        if ( name == null || this.confPorts.get(name) == null ) {
            throw new DbcException("No configuration port for: " + name);
        }
        return this.confPorts.get(name);
    }
}