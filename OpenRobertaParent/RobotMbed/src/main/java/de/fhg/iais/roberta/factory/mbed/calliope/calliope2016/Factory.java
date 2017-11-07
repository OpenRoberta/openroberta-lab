package de.fhg.iais.roberta.factory.mbed.calliope.calliope2016;

import de.fhg.iais.roberta.factory.mbed.calliope.AbstractFactory;
import de.fhg.iais.roberta.factory.mbed.calliope.CompilerWorkflow;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends AbstractFactory {

    public Factory() {
        this.calliopeProperties = Util1.loadProperties("classpath:Calliope2016.properties");
        this.name = this.calliopeProperties.getProperty("robot.name");
        this.robotPropertyNumber = RobertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow =
            new CompilerWorkflow(
                RobertaProperties.getTempDirForUserProjects(),
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"),
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.dir"));

        addBlockTypesFromProperties("Calliope2016.properties", this.calliopeProperties);
    }

}
