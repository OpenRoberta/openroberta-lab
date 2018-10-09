package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.codegen.CalliopeCompilerWorkflow;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Calliope2017Factory extends AbstractMbedFactory {

    public Calliope2017Factory(RobertaProperties robertaProperties) {
        super(robertaProperties);
        this.calliopeProperties = Util1.loadProperties("classpath:Calliope2017.properties");
        this.name = this.calliopeProperties.getProperty("robot.name");
        this.compilerWorkflow =
            new CalliopeCompilerWorkflow(
                robertaProperties.getTempDirForUserProjects(),
                robertaProperties.getStringProperty("robot.plugin." + this.name + ".compiler.resources.dir"),
                robertaProperties.getStringProperty("robot.plugin." + this.name + ".compiler.dir"));

        addBlockTypesFromProperties("Calliope2017.properties", this.calliopeProperties);
    }
}
