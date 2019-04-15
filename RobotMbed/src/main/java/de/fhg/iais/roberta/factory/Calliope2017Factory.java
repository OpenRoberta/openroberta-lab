package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.codegen.CalliopeCompilerWorkflow;
import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.util.PluginProperties;

public class Calliope2017Factory extends AbstractMbedFactory {
    public Calliope2017Factory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return new CalliopeCompilerWorkflow(pluginProperties);
    }
}
