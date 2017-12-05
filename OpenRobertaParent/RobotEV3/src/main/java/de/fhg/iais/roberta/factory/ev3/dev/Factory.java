package de.fhg.iais.roberta.factory.ev3.dev;

import de.fhg.iais.roberta.factory.ev3.lejos.EV3AbstracFactory;
import de.fhg.iais.roberta.util.RobertaProperties;

public class Factory extends EV3AbstracFactory {

    public Factory() {
        super("EV3dev.properties");
        this.robotCompilerWorkflow = new CompilerWorkflow(RobertaProperties.getTempDirForUserProjects());

    }

    @Override
    public String getFileExtension() {
        return "py";
    }

}
