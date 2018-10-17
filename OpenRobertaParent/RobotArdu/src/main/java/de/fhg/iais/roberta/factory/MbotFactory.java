package de.fhg.iais.roberta.factory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.codegen.MbotCompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.arduino.MbotConfiguration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.sensor.Axis;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.codegen.MbotCppVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;

public class MbotFactory extends AbstractRobotFactory {
    private final MbotCompilerWorkflow compilerWorkflow;
    Map<String, SensorPort> sensorToPorts = IRobotFactory.getSensorPortsFromProperties(Util1.loadProperties("classpath:mbotports.properties"));
    Map<String, ActorPort> actorToPorts = IRobotFactory.getActorPortsFromProperties(Util1.loadProperties("classpath:mbotports.properties"));

    public MbotFactory(String robotName, Properties robotProperties, String tempDirForUserProjects) {
        super(robotName, robotProperties);
        String os = "linux";
        if ( SystemUtils.IS_OS_WINDOWS ) {
            os = "windows";
        }
        this.compilerWorkflow =
            new MbotCompilerWorkflow(
                tempDirForUserProjects,
                robotProperties.getProperty("robot.plugin.compiler.resources.dir"),
                robotProperties.getProperty("robot.plugin.compiler." + os + ".dir"));

        addBlockTypesFromProperties(robotName, robotProperties);
    }

    @Override
    public IActorPort getActorPort(String port) {
        return getActorPortValue(port, this.actorToPorts);
    }

    @Override
    public IGyroSensorMode getGyroSensorMode(String gyroSensorMode) {
        return IRobotFactory.getModeValue(gyroSensorMode, Axis.class);
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return getSensorPortValue(port, this.sensorToPorts);
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.compilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFileExtension() {
        return "ino";
    }

    @Override
    public AbstractSimValidatorVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        return null;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return MbotCppVisitor.generate((MbotConfiguration) brickConfiguration, phrasesSet, withWrapping);
    }

    @Override
    public IJoystickMode getJoystickMode(String joystickMode) {
        return IRobotFactory.getModeValue(joystickMode, Axis.class);
    }

    @Override
    public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

}
