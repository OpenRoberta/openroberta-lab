package de.fhg.iais.roberta.factory;

import java.util.Properties;

import de.fhg.iais.roberta.codegen.Ev3DevCompilerWorkflow;

public class Ev3DevFactory extends AbstractEV3Factory {

    public Ev3DevFactory(String robotName, Properties robotProperties, String tempDirForUserProjects) {
        super(robotName, robotProperties, tempDirForUserProjects);
        this.robotCompilerWorkflow = new Ev3DevCompilerWorkflow(tempDirForUserProjects);
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

}
