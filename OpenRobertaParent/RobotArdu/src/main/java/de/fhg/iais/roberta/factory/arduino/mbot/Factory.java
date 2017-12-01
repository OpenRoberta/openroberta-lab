package de.fhg.iais.roberta.factory.arduino.mbot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;

import de.fag.iais.roberta.mode.sensor.arduino.mbot.GyroSensorMode;
import de.fag.iais.roberta.mode.sensor.arduino.mbot.JoystickMode;
import de.fag.iais.roberta.mode.sensor.arduino.mbot.LightSensorMode;
import de.fag.iais.roberta.mode.sensor.arduino.mbot.SensorPort;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.arduino.MbotConfiguration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IBlinkMode;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.action.IMotorSide;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.general.IWorkingState;
import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IMotorTachoMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.mode.action.BlinkMode;
import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.actors.arduino.mbot.ActorPort;
import de.fhg.iais.roberta.mode.sensor.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.TouchSensorMode;
import de.fhg.iais.roberta.mode.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.mode.sensors.arduino.botnroll.BrickKey;
import de.fhg.iais.roberta.mode.sensors.arduino.botnroll.InfraredSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.codegen.arduino.mbot.CppVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class Factory extends AbstractRobotFactory {
    private final CompilerWorkflow compilerWorkflow;
    private final Properties mbotProperties;
    private final String name;
    private final int robotPropertyNumber;

    public Factory() {
        String os = "linux";
        if ( SystemUtils.IS_OS_WINDOWS ) {
            os = "windows";
        }
        this.mbotProperties = Util1.loadProperties("classpath:mbot.properties");
        this.name = this.mbotProperties.getProperty("robot.name");
        this.robotPropertyNumber = RobertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow =
            new CompilerWorkflow(
                RobertaProperties.getTempDirForUserProjects(),
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"),
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler." + os + ".dir"));

        addBlockTypesFromProperties("mbot.properties", this.mbotProperties);
    }

    @Override
    public IBlinkMode getBlinkMode(String mode) {
        if ( (mode == null) || mode.isEmpty() ) {
            throw new DbcException("Invalid Blink Mode: " + mode);
        }
        final String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( final BlinkMode mo : BlinkMode.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( final String value : mo.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid Blink Mode: " + mode);
    }

    @Override
    public IActorPort getActorPort(String port) {
        if ( (port == null) || port.isEmpty() ) {
            throw new DbcException("Invalid Actor Port: " + port);
        }
        final String sUpper = port.trim().toUpperCase(Locale.GERMAN);
        for ( final ActorPort co : ActorPort.values() ) {
            if ( co.toString().equals(sUpper) ) {
                return co;
            }
            for ( final String value : co.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return co;
                }
            }
        }
        throw new DbcException("Invalid Actor Port: " + port);
    }

    @Override
    public IBrickLedColor getBrickLedColor(String color) {
        if ( (color == null) || color.isEmpty() ) {
            throw new DbcException("Invalid Brick Led Color: " + color);
        }
        final String sUpper = color.trim().toUpperCase(Locale.GERMAN);
        for ( final BrickLedColor co : BrickLedColor.values() ) {
            if ( co.toString().equals(sUpper) ) {
                return co;
            }
            for ( final String value : co.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return co;
                }
            }
        }
        throw new DbcException("Invalid Brick Led Color: " + color);

    }

    @Override
    public IBrickKey getBrickKey(String brickKey) {
        if ( (brickKey == null) || brickKey.isEmpty() ) {
            throw new DbcException("Invalid Brick Key: " + brickKey);
        }
        final String sUpper = brickKey.trim().toUpperCase(Locale.GERMAN);
        for ( final BrickKey sp : BrickKey.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( final String value : sp.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid Brick Key: " + brickKey);
    }

    @Override
    public IColorSensorMode getColorSensorMode(String colorSensorMode) {
        if ( (colorSensorMode == null) || colorSensorMode.isEmpty() ) {
            throw new DbcException("Invalid Color Sensor Mode: " + colorSensorMode);
        }
        final String sUpper = colorSensorMode.trim().toUpperCase(Locale.GERMAN);
        for ( final ColorSensorMode sp : ColorSensorMode.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( final String value : sp.getValues() ) {
                if ( sUpper.equals(value.toUpperCase()) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid Color Sensor Mode: " + colorSensorMode);
    }

    @Override
    public ILightSensorMode getLightSensorMode(String lightSensorMode) {
        return IRobotFactory.getModeValue(lightSensorMode, LightSensorMode.class);
    }

    @Override
    public IGyroSensorMode getGyroSensorMode(String gyroSensorMode) {
        if ( (gyroSensorMode == null) || gyroSensorMode.isEmpty() ) {
            throw new DbcException("Invalid Gyro Sensor Mode: " + gyroSensorMode);
        }
        final String sUpper = gyroSensorMode.trim().toUpperCase(Locale.GERMAN);
        for ( final GyroSensorMode sp : GyroSensorMode.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( final String value : sp.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid Gyro Sensor Mode: " + gyroSensorMode);
    }

    @Override
    public IInfraredSensorMode getInfraredSensorMode(String infraredSensorMode) {
        if ( (infraredSensorMode == null) || infraredSensorMode.isEmpty() ) {
            throw new DbcException("Invalid Infrared Sensor Mode: " + infraredSensorMode);
        }
        final String sUpper = infraredSensorMode.trim().toUpperCase(Locale.GERMAN);
        for ( final InfraredSensorMode inf : InfraredSensorMode.values() ) {
            if ( inf.toString().equals(sUpper) ) {
                return inf;
            }
            for ( final String value : inf.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return inf;
                }
            }
        }
        throw new DbcException("Invalid Infrared Sensor Mode: " + infraredSensorMode);
    }

    @Override
    public IMotorTachoMode getMotorTachoMode(String motorTachoMode) {
        if ( (motorTachoMode == null) || motorTachoMode.isEmpty() ) {
            throw new DbcException("Invalid Motor Tacho Mode: " + motorTachoMode);
        }
        final String sUpper = motorTachoMode.trim().toUpperCase(Locale.GERMAN);
        for ( final MotorTachoMode motorTacho : MotorTachoMode.values() ) {
            if ( motorTacho.toString().equals(sUpper) ) {
                return motorTacho;
            }
            for ( final String value : motorTacho.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return motorTacho;
                }
            }
        }
        throw new DbcException("Invalid Motor Tacho Mode: " + motorTachoMode);
    }

    @Override
    public IUltrasonicSensorMode getUltrasonicSensorMode(String ultrasonicSensorMode) {
        if ( (ultrasonicSensorMode == null) || ultrasonicSensorMode.isEmpty() ) {
            throw new DbcException("Invalid Ultrasonic Sensor Mode: " + ultrasonicSensorMode);
        }
        final String sUpper = ultrasonicSensorMode.trim().toUpperCase(Locale.GERMAN);
        for ( final UltrasonicSensorMode ultra : UltrasonicSensorMode.values() ) {
            if ( ultra.toString().equals(sUpper) ) {
                return ultra;
            }
            for ( final String value : ultra.getValues() ) {
                if ( sUpper.equals(value.toUpperCase()) ) {
                    return ultra;
                }
            }
        }
        throw new DbcException("Invalid Ultrasonic Sensor Mode: " + ultrasonicSensorMode);
    }

    @Override
    public ITouchSensorMode getTouchSensorMode(String mode) {
        if ( (mode == null) || mode.isEmpty() ) {
            throw new DbcException("Invalid Touch Sensor Mode: " + mode);
        }
        final String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( final TouchSensorMode ultra : TouchSensorMode.values() ) {
            if ( ultra.toString().equals(sUpper) ) {
                return ultra;
            }
            for ( final String value : ultra.getValues() ) {
                if ( sUpper.equals(value.toUpperCase()) ) {
                    return ultra;
                }
            }
        }
        throw new DbcException("Invalid Touch Sensor Mode: " + mode);
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        if ( (port == null) || port.isEmpty() ) {
            throw new DbcException("Invalid sensor port: " + port);
        }
        final String sUpper = port.trim().toUpperCase(Locale.GERMAN);
        for ( final SensorPort po : SensorPort.values() ) {
            if ( po.toString().equals(sUpper) ) {
                return po;
            }
            for ( final String value : po.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return po;
                }
            }
        }
        throw new DbcException("Invalid sensor port: " + port);
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
    public ILightSensorMode getLightColor(String mode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ILightSensorActionMode getLightActionColor(String mode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IWorkingState getWorkingState(String mode) {
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
        return RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group") != null
            ? RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group")
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
    public JoystickMode getJoystickMode(String joystickMode) {
        if ( (joystickMode == null) || joystickMode.isEmpty() ) {
            throw new DbcException("Invalid joystick axis: " + joystickMode);
        }
        final String sUpper = joystickMode.trim().toUpperCase(Locale.GERMAN);
        for ( final JoystickMode po : JoystickMode.values() ) {
            if ( po.toString().equals(sUpper) ) {
                return po;
            }
            for ( final String value : po.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return po;
                }
            }
        }
        throw new DbcException("Invalid joystick axis: " + joystickMode);
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
    public RobotBrickCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMotorSide getMotorSide(String motorSide) {
        if ( (motorSide == null) || motorSide.isEmpty() ) {
            throw new DbcException("Invalid Drive Direction: " + motorSide);
        }
        final String sUpper = motorSide.trim().toUpperCase(Locale.GERMAN);
        for ( final MotorSide sp : MotorSide.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( final String value : sp.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid Drive Direction: " + motorSide);
    }
}
