package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.codegen.Ev3DevCompilerWorkflow;
import de.fhg.iais.roberta.util.RobertaProperties;

public class Ev3DevFactory extends AbstractEV3Factory {

    public Ev3DevFactory(RobertaProperties robertaProperties) {
        super(robertaProperties, "EV3dev.properties");
        this.robotCompilerWorkflow = new Ev3DevCompilerWorkflow(robertaProperties.getTempDirForUserProjects());
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

}
