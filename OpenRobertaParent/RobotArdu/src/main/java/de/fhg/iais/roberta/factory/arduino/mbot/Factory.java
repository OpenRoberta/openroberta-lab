package de.fhg.iais.roberta.factory.arduino.mbot;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;

import de.fag.iais.roberta.mode.sensor.arduino.mbot.SensorPort;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.arduino.MbotConfiguration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.actors.arduino.mbot.ActorPort;
import de.fhg.iais.roberta.mode.sensor.Axis;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotCommonCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.codegen.arduino.mbot.CppVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends AbstractRobotFactory {
    private final CompilerWorkflow compilerWorkflow;
    private final Properties mbotProperties;
    private final String name;
    private final int robotPropertyNumber;

    public Factory(RobertaProperties robertaProperties) {
        super(robertaProperties);
        String os = "linux";
        if ( SystemUtils.IS_OS_WINDOWS ) {
            os = "windows";
        }
        this.mbotProperties = Util1.loadProperties("classpath:mbot.properties");
        this.name = this.mbotProperties.getProperty("robot.name");
        this.robotPropertyNumber = robertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow =
            new CompilerWorkflow(
                robertaProperties.getTempDirForUserProjects(),
                robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"),
                robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler." + os + ".dir"));

        addBlockTypesFromProperties("mbot.properties", this.mbotProperties);
    }

    @Override
    public IActorPort getActorPort(String port) {
        return IRobotFactory.getPort(port, ActorPort.class);
    }

    @Override
    public IGyroSensorMode getGyroSensorMode(String gyroSensorMode) {
        return IRobotFactory.getModeValue(gyroSensorMode, Axis.class);
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return IRobotFactory.getModeValue(port, SensorPort.class);
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
    public ILightSensorActionMode getLightActionColor(String mode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFileExtension() {
        return "ino";
    }

    @Override
    public String getProgramToolboxBeginner() {
        return this.mbotProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.mbotProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.mbotProperties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.mbotProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.mbotProperties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.mbotProperties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.mbotProperties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.mbotProperties.getProperty("robot.info") != null ? this.mbotProperties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.mbotProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean hasConfiguration() {
        return this.mbotProperties.getProperty("robot.configuration") != null ? false : true;
    }

    @Override
    public String getGroup() {

        return this.robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group") != null
            ? this.robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group")
            : this.name;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return CppVisitor.generate((MbotConfiguration) brickConfiguration, phrasesSet, withWrapping);
    }

    @Override
    public String getConnectionType() {
        return this.mbotProperties.getProperty("robot.connection");
    }

    @Override
    public String getVendorId() {
        return this.mbotProperties.getProperty("robot.vendor");
    }

    @Override
    public IJoystickMode getJoystickMode(String joystickMode) {
        return IRobotFactory.getModeValue(joystickMode, Axis.class);
    }

    @Override
    public String getCommandline() {
        return this.mbotProperties.getProperty("robot.connection.commandLine");
    }

    @Override
    public String getSignature() {
        return this.mbotProperties.getProperty("robot.connection.signature");
    }

    @Override
    public RobotCommonCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

}
