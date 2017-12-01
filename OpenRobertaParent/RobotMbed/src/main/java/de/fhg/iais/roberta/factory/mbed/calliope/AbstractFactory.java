package de.fhg.iais.roberta.factory.mbed.calliope;

import java.util.ArrayList;
import java.util.Properties;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.factory.mbed.SimCompilerWorkflow;
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
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.mode.action.mbed.ActorPort;
import de.fhg.iais.roberta.mode.sensor.mbed.BrickKey;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.mbed.calliope.SimulationCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public abstract class AbstractFactory extends AbstractRobotFactory {

    protected CompilerWorkflow compilerWorkflow;
    protected SimCompilerWorkflow calliopeSimCompilerWorkflow;
    protected Properties calliopeProperties;
    protected String name;
    protected int robotPropertyNumber;

    public AbstractFactory() {
        super();
        final Properties mbedProperties = Util1.loadProperties("classpath:Mbed.properties");
        addBlockTypesFromProperties("Mbed.properties", mbedProperties);

        calliopeSimCompilerWorkflow = new SimCompilerWorkflow();
    }

    @Override
    public IBlinkMode getBlinkMode(String mode) {
        return null;
    }

    @Override
    public IActorPort getActorPort(String port) {
        return IRobotFactory.getModeValue(port, ActorPort.class);
    }

    @Override
    public IBrickLedColor getBrickLedColor(String color) {
        return null;
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
        return null;
    }

    @Override
    public IGyroSensorMode getGyroSensorMode(String gyroSensorMode) {
        return null;
    }

    @Override
    public IInfraredSensorMode getInfraredSensorMode(String infraredSensorMode) {
        return null;
    }

    @Override
    public IMotorTachoMode getMotorTachoMode(String motorTachoMode) {
        return null;
    }

    @Override
    public IUltrasonicSensorMode getUltrasonicSensorMode(String ultrasonicSensorMode) {
        return null;
    }

    @Override
    public ITouchSensorMode getTouchSensorMode(String mode) {
        return null;
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

    @Override
    public String getFileExtension() {
        return "cpp";
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return compilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return calliopeSimCompilerWorkflow;
    }

    @Override
    public String getProgramToolboxBeginner() {
        return calliopeProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return calliopeProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return calliopeProperties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return calliopeProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return calliopeProperties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return calliopeProperties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return calliopeProperties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return calliopeProperties.getProperty("robot.info") != null ? calliopeProperties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return calliopeProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public String getConnectionType() {
        return calliopeProperties.getProperty("robot.connection");
    }

    @Override
    public RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return new SimulationCheckVisitor(brickConfiguration);
    }

    @Override
    public Boolean hasConfiguration() {
        return calliopeProperties.getProperty("robot.configuration") != null ? false : true;
    }

    @Override
    public String getGroup() {
        return RobertaProperties.getStringProperty("robot.plugin." + robotPropertyNumber + ".group") != null
            ? RobertaProperties.getStringProperty("robot.plugin." + robotPropertyNumber + ".group")
            : name;
    }

    @Override
    public RobotBrickCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

}