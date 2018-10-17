package de.fhg.iais.roberta.factory;

import java.util.Properties;

import de.fhg.iais.roberta.codegen.CalliopeCompilerWorkflow;

public class Calliope2017Factory extends AbstractMbedFactory {
    public Calliope2017Factory(String robotName, Properties robotProperties, String tempDirForUserProjects) {
        super(robotName, robotProperties);
        this.compilerWorkflow =
            new CalliopeCompilerWorkflow(
                tempDirForUserProjects,
                robotProperties.getProperty("robot.plugin.compiler.resources.dir"),
                robotProperties.getProperty("robot.plugin.compiler.dir"));
    }
}
