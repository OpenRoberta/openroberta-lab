package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.codegen.Ev3DevCompilerWorkflow;
import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.util.PluginProperties;

public class Ev3DevFactory extends AbstractEV3Factory {

    public Ev3DevFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return new Ev3DevCompilerWorkflow(pluginProperties);
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

}
