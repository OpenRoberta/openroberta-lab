package de.fhg.iais.roberta.factory.ev3.dev;

import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.ev3.Ev3SimCompilerWorkflow;
import de.fhg.iais.roberta.factory.ev3.lejos.EV3AbstracFactory;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends EV3AbstracFactory {
    private final CompilerWorkflow robotCompilerWorkflow;

    public Factory() {
        this.ev3Properties = Util1.loadProperties("classpath:EV3dev.properties");
        this.name = this.ev3Properties.getProperty("robot.name");
        this.robotPropertyNumber = RobertaProperties.getRobotNumberFromProperty(this.name);
        this.robotCompilerWorkflow = new CompilerWorkflow(RobertaProperties.getTempDirForUserProjects());

        this.simCompilerWorkflow = new Ev3SimCompilerWorkflow();
        addBlockTypesFromProperties("EV3dev.properties", this.ev3Properties);
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.robotCompilerWorkflow;
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

}
