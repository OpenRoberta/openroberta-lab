package de.fhg.iais.roberta.factory.mbed.calliope.calliope2016;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.mbed.SimCompilerWorkflow;
import de.fhg.iais.roberta.factory.mbed.calliope.AbstractFactory;
import de.fhg.iais.roberta.factory.mbed.calliope.CompilerWorkflow;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends AbstractFactory {

    public Factory(RobotCommunicator unusedForArdu) {
        this.calliopeProperties = Util1.loadProperties("classpath:Calliope2016.properties");
        this.name = this.calliopeProperties.getProperty("robot.name");
        this.robotPropertyNumber = RobertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow =
            new CompilerWorkflow(
                RobertaProperties.getTempDirForUserProjects(),
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"),
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.dir"));
        this.calliopeSimCompilerWorkflow = new SimCompilerWorkflow();
        addBlockTypesFromProperties("Calliope2016.properties", this.calliopeProperties);
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return null;
    }

    @Override
    public RobotBrickCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public IJoystickMode getJoystickMode(String joystickMode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IJoystickMode> getJoystickMode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getVendorId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCommandline() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSignature() {
        // TODO Auto-generated method stub
        return null;
    }
}
