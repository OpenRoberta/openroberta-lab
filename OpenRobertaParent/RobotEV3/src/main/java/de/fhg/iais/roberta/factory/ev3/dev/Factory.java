package de.fhg.iais.roberta.factory.ev3.dev;

import de.fhg.iais.roberta.factory.ev3.lejos.EV3AbstractFactory;
import de.fhg.iais.roberta.util.RobertaProperties;

public class Factory extends EV3AbstractFactory {

    public Factory(RobertaProperties robertaProperties) {
        super(robertaProperties, "EV3dev.properties");
        this.robotCompilerWorkflow = new CompilerWorkflow(robertaProperties.getTempDirForUserProjects());
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

}
