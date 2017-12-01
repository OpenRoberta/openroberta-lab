package de.fhg.iais.roberta.factory.nxt;

import java.util.ArrayList;
import java.util.Properties;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IBlinkMode;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.general.IPickColor;
import de.fhg.iais.roberta.inter.mode.general.IWorkingState;
import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IMotorTachoMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.BlinkMode;
import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.nxt.LightSensorActionMode;
import de.fhg.iais.roberta.mode.general.WorkingState;
import de.fhg.iais.roberta.mode.general.nxt.PickColor;
import de.fhg.iais.roberta.mode.sensor.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.mode.sensor.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.mode.sensor.TouchSensorMode;
import de.fhg.iais.roberta.mode.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.mode.sensor.nxt.BrickKey;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.nxt.BrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.nxt.SimulationCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends AbstractRobotFactory {
    private final CompilerWorkflow robotCompilerWorkflow;
    private final SimCompilerWorkflow simCompilerWorkflow;
    private final Properties nxtProperties;
    private final String name;
    private final int robotPropertyNumber;

    public Factory() {
        this.nxtProperties = Util1.loadProperties("classpath:NXT.properties");
        this.name = this.nxtProperties.getProperty("robot.name");
        this.robotPropertyNumber = RobertaProperties.getRobotNumberFromProperty(this.name);
        this.robotCompilerWorkflow =
            new CompilerWorkflow(
                RobertaProperties.getTempDirForUserProjects(),
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"));
        this.simCompilerWorkflow = new SimCompilerWorkflow();
        addBlockTypesFromProperties("NXT.properties", this.nxtProperties);
    }

    @Override
    public IBlinkMode getBlinkMode(String mode) {
        return IRobotFactory.getModeValue(mode, BlinkMode.class);
    }

    @Override
    public IActorPort getActorPort(String port) {
        return IRobotFactory.getModeValue(port, ActorPort.class);
    }

    @Override
    public IPickColor getPickColor(String color) {
        return IRobotFactory.getModeValue(color, PickColor.class);
    }

    @Override
    public IBrickLedColor getBrickLedColor(String color) {
        return IRobotFactory.getModeValue(color, BrickLedColor.class);
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        return null;
    }

    @Override
    public IBrickKey getBrickKey(String brickKey) {
        return IRobotFactory.getModeValue(brickKey, BrickKey.class);
    }

    @Override
    public IColorSensorMode getColorSensorMode(String colorSensorMode) {
        return IRobotFactory.getModeValue(colorSensorMode, ColorSensorMode.class);
    }

    //    @Override
    //    public ILightSensorMode getLightSensorMode(String lightSensorMode) {
    //        return IRobotFactory.getModeValue(lightSensorMode, LightSensorMode.class);
    //    }

    @Override
    public IGyroSensorMode getGyroSensorMode(String gyroSensorMode) {
        return IRobotFactory.getModeValue(gyroSensorMode, GyroSensorMode.class);
    }

    @Override
    public IInfraredSensorMode getInfraredSensorMode(String infraredSensorMode) {
        return IRobotFactory.getModeValue(infraredSensorMode, InfraredSensorMode.class);
    }

    @Override
    public IMotorTachoMode getMotorTachoMode(String motorTachoMode) {
        return IRobotFactory.getModeValue(motorTachoMode, MotorTachoMode.class);
    }

    @Override
    public IUltrasonicSensorMode getUltrasonicSensorMode(String ultrasonicSensorMode) {
        return IRobotFactory.getModeValue(ultrasonicSensorMode, UltrasonicSensorMode.class);
    }

    @Override
    public ITouchSensorMode getTouchSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, TouchSensorMode.class);
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return IRobotFactory.getModeValue(port, SensorPort.class);
    }

    @Override
    public ILightSensorMode getLightColor(String mode) {
        return null;
    }

    @Override
    public ILightSensorActionMode getLightActionColor(String light) {
        return IRobotFactory.getModeValue(light, LightSensorActionMode.class);

    }

    @Override
    public IWorkingState getWorkingState(String state) {
        return IRobotFactory.getModeValue(state, WorkingState.class);
    }

    @Override
    public IJoystickMode getJoystickMode(String joystickMode) {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "nxc";
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.robotCompilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return this.simCompilerWorkflow;
    }

    @Override
    public String getProgramToolboxBeginner() {
        return this.nxtProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.nxtProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.nxtProperties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.nxtProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.nxtProperties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.nxtProperties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.nxtProperties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.nxtProperties.getProperty("robot.info") != null ? this.nxtProperties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.nxtProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public String getConnectionType() {
        return this.nxtProperties.getProperty("robot.connection");
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
        return this.nxtProperties.getProperty("robot.configuration") != null ? false : true;
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
    public String getVendorId() {
        return null;
    }

    @Override
    public String getCommandline() {
        return null;
    }

    @Override
    public String getSignature() {
        // TODO Auto-generated method stub
        return null;
    }
}
