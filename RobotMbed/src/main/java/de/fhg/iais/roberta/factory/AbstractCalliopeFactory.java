package de.fhg.iais.roberta.factory;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import de.fhg.iais.roberta.components.Configuration;
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
import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IMotorTachoMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISoundSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITimerSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.mode.action.mbed.ActorPort;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.mode.sensor.mbed.BrickKey;
import de.fhg.iais.roberta.robotCommunication.ICompilerWorkflow;
import de.fhg.iais.roberta.syntax.check.hardware.SimulationProgramCheckVisitor;
import de.fhg.iais.roberta.syntax.hardwarecheck.mbed.CalliopeSimProgramCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.dbc.DbcException;

public abstract class AbstractCalliopeFactory extends AbstractRobotFactory {

    protected CalliopeCompilerWorkflow compilerWorkflow;
    protected MbedSimCompilerWorkflow calliopeSimCompilerWorkflow;
    protected Properties calliopeProperties;
    protected String name;
    protected int robotPropertyNumber;

    public AbstractCalliopeFactory() {
        super();
    }

    @Override
    public IBlinkMode getBlinkMode(String mode) {
        return null;
    }

    @Override
    public List<IBlinkMode> getBlinkModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IActorPort getActorPort(String port) {
        if ( port == null || port.isEmpty() ) {
            throw new DbcException("Invalid Actor Port: " + port);
        }
        String sUpper = port.trim().toUpperCase(Locale.GERMAN);
        for ( ActorPort co : ActorPort.values() ) {
            if ( co.toString().equals(sUpper) ) {
                return co;
            }
            for ( String value : co.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return co;
                }
            }
        }
        throw new DbcException("Invalid Actor Port: " + port);
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
        String sUpper = brickKey.trim().toUpperCase(Locale.GERMAN);
        for ( BrickKey sp : BrickKey.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.getValues() ) {
                if ( sUpper.equals(value) ) {
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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IGyroSensorMode getGyroSensorMode(String gyroSensorMode) {
        return null;
    }

    @Override
    public List<IGyroSensorMode> getGyroSensorModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IInfraredSensorMode getInfraredSensorMode(String infraredSensorMode) {
        return null;
    }

    @Override
    public List<IInfraredSensorMode> getInfraredSensorModes() {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IUltrasonicSensorMode getUltrasonicSensorMode(String ultrasonicSensorMode) {
        return null;
    }

    @Override
    public List<IUltrasonicSensorMode> getUltrasonicSensorModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ITouchSensorMode getTouchSensorMode(String mode) {
        return null;
    }

    @Override
    public List<ITouchSensorMode> getTouchSensorModes() {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ILightSensorActionMode> getLightActionColors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IWorkingState getWorkingState(String mode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IWorkingState> getWorkingStates() {
        // TODO Auto-generated method stub
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
    public String getProgramToolboxBeginner() {
        return this.calliopeProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.calliopeProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.calliopeProperties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.calliopeProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.calliopeProperties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.calliopeProperties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.calliopeProperties.getProperty("robot.sim") != null ? true : false;
    }

    @Override
    public String getInfo() {
        return this.calliopeProperties.getProperty("robot.info") != null ? this.calliopeProperties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.calliopeProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public Boolean isAutoconnected() {
        return this.calliopeProperties.getProperty("robot.connection.server") != null ? true : false;
    }

    @Override
    public SimulationProgramCheckVisitor getProgramCheckVisitor(Configuration brickConfiguration) {
        return new CalliopeSimProgramCheckVisitor(brickConfiguration);
    }

    @Override
    public Boolean hasConfiguration() {
        return this.calliopeProperties.getProperty("robot.configuration") != null ? false : true;
    }

    @Override
    public String getGroup() {
        return RobertaProperties.getStringProperty("robot.plugin." + robotPropertyNumber + ".group") != null
            ? RobertaProperties.getStringProperty("robot.plugin." + robotPropertyNumber + ".group")
            : this.name;
    }

}