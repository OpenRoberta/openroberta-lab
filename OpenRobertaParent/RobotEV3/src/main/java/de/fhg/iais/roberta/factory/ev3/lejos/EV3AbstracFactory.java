package de.fhg.iais.roberta.factory.ev3.lejos;

import java.util.ArrayList;
import java.util.Properties;

import com.google.inject.AbstractModule;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.factory.ev3.Ev3GuiceModule;
import de.fhg.iais.roberta.factory.ev3.Ev3SimCompilerWorkflow;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IBlinkMode;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.general.IWorkingState;
import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IMotorTachoMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISoundSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.BlinkMode;
import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.ev3.ShowPicture;
import de.fhg.iais.roberta.mode.sensor.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.mode.sensor.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.SoundSensorMode;
import de.fhg.iais.roberta.mode.sensor.TouchSensorMode;
import de.fhg.iais.roberta.mode.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.mode.sensor.ev3.BrickKey;
import de.fhg.iais.roberta.mode.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.ev3.BrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.ev3.SimulationCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;

public abstract class EV3AbstracFactory extends AbstractRobotFactory {
    protected Ev3SimCompilerWorkflow simCompilerWorkflow;
    protected Properties ev3Properties;
    protected String name;
    protected int robotPropertyNumber;

    @Override
    public IBlinkMode getBlinkMode(String mode) {
        return IRobotFactory.getModeValue(mode, BlinkMode.class);
    }

    @Override
    public IActorPort getActorPort(String port) {
        return IRobotFactory.getModeValue(port, ActorPort.class);
    }

    @Override
    public IBrickLedColor getBrickLedColor(String color) {
        return IRobotFactory.getModeValue(color, BrickLedColor.class);
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        return IRobotFactory.getModeValue(picture, ShowPicture.class);
    }

    @Override
    public IBrickKey getBrickKey(String brickKey) {
        return IRobotFactory.getModeValue(brickKey, BrickKey.class);
    }

    @Override
    public IColorSensorMode getColorSensorMode(String modeName) {
        return IRobotFactory.getModeValue(modeName, ColorSensorMode.class);
    }

    @Override
    public ISoundSensorMode getSoundSensorMode(String modeName) {
        return IRobotFactory.getModeValue(modeName, SoundSensorMode.class);
    }

    @Override
    public IGyroSensorMode getGyroSensorMode(String modeName) {
        return IRobotFactory.getModeValue(modeName, GyroSensorMode.class);
    }

    @Override
    public IInfraredSensorMode getInfraredSensorMode(String modeName) {
        return IRobotFactory.getModeValue(modeName, InfraredSensorMode.class);
    }

    @Override
    public IMotorTachoMode getMotorTachoMode(String modeName) {
        return IRobotFactory.getModeValue(modeName, MotorTachoMode.class);
    }

    @Override
    public IUltrasonicSensorMode getUltrasonicSensorMode(String modeName) {
        return IRobotFactory.getModeValue(modeName, UltrasonicSensorMode.class);
    }

    @Override
    public ITouchSensorMode getTouchSensorMode(String modeName) {
        return IRobotFactory.getModeValue(modeName, TouchSensorMode.class);
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return IRobotFactory.getModeValue(port, SensorPort.class);
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return this.simCompilerWorkflow;
    }

    @Override
    public AbstractModule getGuiceModule() {
        return new Ev3GuiceModule(RobertaProperties.getRobertaProperties());
    }

    @Override
    public ILightSensorActionMode getLightActionColor(String mode) {
        return null;
    }

    @Override
    public IWorkingState getWorkingState(String mode) {
        return null;
    }

    @Override
    public ILightSensorMode getLightColor(String mode) {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "java";
    }

    @Override
    public String getProgramToolboxBeginner() {
        return this.ev3Properties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.ev3Properties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.ev3Properties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.ev3Properties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.ev3Properties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.ev3Properties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.ev3Properties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.ev3Properties.getProperty("robot.info") != null ? this.ev3Properties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.ev3Properties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public String getConnectionType() {
        return this.ev3Properties.getProperty("robot.connection");
    }

    @Override
    public RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return new SimulationCheckVisitor(brickConfiguration);
    }

    @Override
    public RobotBrickCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return new BrickCheckVisitor(brickConfiguration);
    }

    @Override
    public Boolean hasConfiguration() {
        return this.ev3Properties.getProperty("robot.configuration") != null ? false : true;
    }

    @Override
    public String getGroup() {
        return RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group") != null
            ? RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group")
            : this.name;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return null;
    }

    @Override
    public IJoystickMode getJoystickMode(String joystickMode) {

        return null;
    }

    @Override
    public String getVendorId() {

        return null;
    }

    @Override
    public String getCommandline() {

        return null;
    }

    @Override
    public String getSignature() {
        return null;
    }
}
