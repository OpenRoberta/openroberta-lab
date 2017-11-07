package de.fhg.iais.roberta.factory.mbed.microbit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.mbed.SimCompilerWorkflow;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IBlinkMode;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.action.IWorkingState;
import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IMotorTachoMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISoundSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITimerSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.mode.sensor.mbed.BrickKey;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.mbed.microbit.SimulationCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class Factory extends AbstractRobotFactory {

    private final CompilerWorkflow compilerWorkflow;
    private final SimCompilerWorkflow microbitSimCompilerWorkflow;
    private final Properties microbitProperties;
    private final String name;
    private final int robotPropertyNumber;

    public Factory() {
        this.microbitProperties = Util1.loadProperties("classpath:Microbit.properties");
        this.name = this.microbitProperties.getProperty("robot.name");
        this.robotPropertyNumber = RobertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow =
            new CompilerWorkflow(
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"),
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.dir"));
        this.microbitSimCompilerWorkflow = new SimCompilerWorkflow();
        Properties mbedProperties = Util1.loadProperties("classpath:Mbed.properties");
        addBlockTypesFromProperties("Mbed.properties", mbedProperties);
        addBlockTypesFromProperties("Microbit.properties", this.microbitProperties);
    }

    @Override
    public IBlinkMode getBlinkMode(String mode) {
        return null;
    }

    @Override
    public List<IBlinkMode> getBlinkModes() {
        return null;
    }

    @Override
    public IActorPort getActorPort(String port) {
        return null;
    }

    @Override
    public List<IActorPort> getActorPorts() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IBrickLedColor getBrickLedColor(String color) {
        return null;
    }

    @Override
    public List<IBrickLedColor> getBrickLedColors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        return null;
    }

    @Override
    public List<IShowPicture> getShowPictures() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IBrickKey getBrickKey(String brickKey) {
        if ( brickKey == null || brickKey.isEmpty() ) {
            throw new DbcException("Invalid Brick Key: " + brickKey);
        }
        //String sUpper = brickKey.trim().toUpperCase(Locale.GERMAN);
        for ( BrickKey sp : BrickKey.values() ) {
            if ( sp.toString().equals(brickKey) ) {
                return sp;
            }
            for ( String value : sp.getValues() ) {
                if ( brickKey.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid Brick Key: " + brickKey);
    }

    @Override
    public List<IBrickKey> getBrickKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IColorSensorMode getColorSensorMode(String colorSensorMode) {
        return null;
    }

    @Override
    public List<IColorSensorMode> getColorSensorModes() {
        return null;
    }

    @Override
    public ILightSensorMode getLightSensorMode(String lightSensorMode) {
        return null;
    }

    @Override
    public List<ILightSensorMode> getLightSensorModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ISoundSensorMode getSoundSensorMode(String soundSensorMode) {
        return null;
    }

    @Override
    public List<ISoundSensorMode> getSoundSensorModes() {
        return null;
    }

    @Override
    public IGyroSensorMode getGyroSensorMode(String gyroSensorMode) {
        return null;
    }

    @Override
    public List<IGyroSensorMode> getGyroSensorModes() {
        return null;
    }

    @Override
    public IInfraredSensorMode getInfraredSensorMode(String infraredSensorMode) {
        return null;
    }

    @Override
    public List<IInfraredSensorMode> getInfraredSensorModes() {
        return null;
    }

    @Override
    public ITimerSensorMode getTimerSensorMode(String timerSensroMode) {
        if ( timerSensroMode == null || timerSensroMode.isEmpty() ) {
            throw new DbcException("Invalid Timer Sensor Mode: " + timerSensroMode);
        }
        String sUpper = timerSensroMode.trim().toUpperCase(Locale.GERMAN);
        for ( TimerSensorMode timerSens : TimerSensorMode.values() ) {
            if ( timerSens.toString().equals(sUpper) ) {
                return timerSens;
            }
            for ( String value : timerSens.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return timerSens;
                }
            }
        }
        throw new DbcException("Invalid Timer Sensor Mode: " + timerSensroMode);
    }

    @Override
    public List<ITimerSensorMode> getTimerSensorModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMotorTachoMode getMotorTachoMode(String motorTachoMode) {
        return null;
    }

    @Override
    public List<IMotorTachoMode> getMotorTachoModes() {
        return null;
    }

    @Override
    public IUltrasonicSensorMode getUltrasonicSensorMode(String ultrasonicSensorMode) {
        return null;
    }

    @Override
    public List<IUltrasonicSensorMode> getUltrasonicSensorModes() {
        return null;
    }

    @Override
    public ITouchSensorMode getTouchSensorMode(String mode) {
        return null;
    }

    @Override
    public List<ITouchSensorMode> getTouchSensorModes() {
        return null;
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return null;
    }

    @Override
    public List<ISensorPort> getSensorPorts() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ILightSensorMode getLightColor(String mode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ILightSensorMode> getLightColors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ILightSensorActionMode getLightActionColor(String mode) {
        return null;
    }

    @Override
    public List<ILightSensorActionMode> getLightActionColors() {
        return null;
    }

    @Override
    public IWorkingState getWorkingState(String mode) {
        return null;
    }

    @Override
    public List<IWorkingState> getWorkingStates() {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.compilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return this.microbitSimCompilerWorkflow;
    }

    @Override
    public String getProgramToolboxBeginner() {
        return this.microbitProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.microbitProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.microbitProperties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.microbitProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.microbitProperties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.microbitProperties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.microbitProperties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.microbitProperties.getProperty("robot.info") != null ? this.microbitProperties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.microbitProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public String getConnectionType() {
        return this.microbitProperties.getProperty("robot.connection");
    }

    @Override
    public RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return new SimulationCheckVisitor(brickConfiguration);
    }

    @Override
    public RobotBrickCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public Boolean hasConfiguration() {
        return this.microbitProperties.getProperty("robot.configuration") != null ? false : true;
    }

    @Override
    public String getGroup() {
        return this.microbitProperties.getProperty("robot.group") != null ? this.microbitProperties.getProperty("robot.group") : this.name;
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
    public List<IJoystickMode> getJoystickMode() {
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
