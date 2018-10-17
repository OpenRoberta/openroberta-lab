package de.fhg.iais.roberta.factory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.codegen.MbedSimCompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.CalliopeSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.MbedBoardValidatorVisitor;

public abstract class AbstractMbedFactory extends AbstractRobotFactory {

    protected ICompilerWorkflow compilerWorkflow;
    protected MbedSimCompilerWorkflow calliopeSimCompilerWorkflow;
    Map<String, SensorPort> sensorToPorts = IRobotFactory.getSensorPortsFromProperties(Util1.loadProperties("classpath:Calliopeports.properties"));
    Map<String, ActorPort> actorToPorts = IRobotFactory.getActorPortsFromProperties(Util1.loadProperties("classpath:Calliopeports.properties"));

    public AbstractMbedFactory(String robotName, Properties robotProperties) {
        super(robotName, robotProperties);
        addBlockTypesFromProperties(robotName, robotProperties);
        addBlockTypesFromProperties("mbed", Util1.loadProperties("classpath:mbed.properties"));
        this.calliopeSimCompilerWorkflow = new MbedSimCompilerWorkflow();
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return getSensorPortValue(port, this.sensorToPorts);
    }

    @Override
    public IActorPort getActorPort(String port) {
        return getActorPortValue(port, this.actorToPorts);
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        return null;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "cpp";
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.compilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return this.calliopeSimCompilerWorkflow;
    }

    @Override
    public AbstractSimValidatorVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return new CalliopeSimValidatorVisitor(brickConfiguration);
    }

    @Override
    public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return new MbedBoardValidatorVisitor(brickConfiguration);
    }
}