package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.util.PluginProperties;

public class EdisonFactory extends AbstractRobotFactory {

    /**
     * Creates a new instance of EdisonFactory by calling it's super() method
     *
     * @param pluginProperties the Edison robots properties
     */
    public EdisonFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    /**
     * returns the file extension for the generated code. Note that there is a difference between generated and compiled code.
     *
     * @return a String representing the file extension
     */
    @Override
    public String getFileExtension() {
        return "py";
    }
}
