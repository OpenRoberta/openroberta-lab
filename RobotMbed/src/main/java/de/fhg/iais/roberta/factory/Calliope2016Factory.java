package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Calliope2016Factory extends AbstractCalliopeFactory {

    public Calliope2016Factory(RobotCommunicator unusedForArdu) {

        int robotPropertyNumber = RobertaProperties.getRobotNumberFromProperty("calliope2016");
        this.compilerWorkflow =
            new CalliopeCompilerWorkflow(
                RobertaProperties.getTempDirForUserProjects(),
                RobertaProperties.getStringProperty("robot.plugin." + robotPropertyNumber + ".compiler.resources.dir"),
                RobertaProperties.getStringProperty("robot.plugin." + robotPropertyNumber + ".compiler.dir"));
        this.calliopeProperties = Util1.loadProperties("classpath:Calliope2016.properties");
        this.calliopeSimCompilerWorkflow = new MbedSimCompilerWorkflow();
        addBlockTypesFromProperties("Calliope2016.properties", this.calliopeProperties);
    }
}
