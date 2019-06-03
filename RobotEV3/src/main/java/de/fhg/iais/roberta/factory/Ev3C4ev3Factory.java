package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.codegen.Ev3C4ev3CompilerWorkflow;
import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.util.PluginProperties;

import static de.fhg.iais.roberta.codegen.Ev3C4ev3CompilerWorkflow.GENERATED_SOURCE_CODE_EXTENSION;

public class Ev3C4ev3Factory extends AbstractEV3Factory {
    public Ev3C4ev3Factory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return new Ev3C4ev3CompilerWorkflow(pluginProperties);
    }

    @Override
    public String getFileExtension() {
        return GENERATED_SOURCE_CODE_EXTENSION.substring(1); // remove '.'
    }
}
