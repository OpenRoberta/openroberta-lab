package de.fhg.iais.roberta.factory.ev3.dev;

import de.fhg.iais.roberta.factory.ev3.lejos.EV3AbstractFactory;

public class Factory extends EV3AbstractFactory {

    public Factory() {
        super("EV3dev.properties");
        this.robotCompilerWorkflow = new CompilerWorkflow(robertaProperties.getTempDirForUserProjects());
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

}
