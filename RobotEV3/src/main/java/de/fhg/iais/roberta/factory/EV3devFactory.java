package de.fhg.iais.roberta.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import com.google.inject.AbstractModule;

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
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.mode.action.ev3.ActorPort;
import de.fhg.iais.roberta.mode.action.ev3.BlinkMode;
import de.fhg.iais.roberta.mode.action.ev3.BrickLedColor;
import de.fhg.iais.roberta.mode.action.ev3.ShowPicture;
import de.fhg.iais.roberta.mode.sensor.ev3.BrickKey;
import de.fhg.iais.roberta.mode.sensor.ev3.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.ev3.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.ev3.InfraredSensorMode;
import de.fhg.iais.roberta.mode.sensor.ev3.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.mode.sensor.ev3.TouchSensorMode;
import de.fhg.iais.roberta.mode.sensor.ev3.UltrasonicSensorMode;
import de.fhg.iais.roberta.robotCommunication.ICompilerWorkflow;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.hardware.SimulationProgramCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class EV3devFactory extends AbstractRobotFactory {
    private final Ev3devCompilerWorkflow robotCompilerWorkflow;
    private final Ev3SimCompilerWorkflow simCompilerWorkflow;
    private final Properties ev3Properties;
    private final int robotPropertyNumber;
    private final String name;

    public EV3devFactory(RobotCommunicator robotCommunicator) {
        this.name = "ev3dev";
        this.robotPropertyNumber = RobertaProperties.getRobotNumberFromProperty(this.name);
        this.ev3Properties = Util1.loadProperties("classpath:EV3dev.properties");
        this.robotCompilerWorkflow = new Ev3devCompilerWorkflow(RobertaProperties.getTempDirForUserProjects());
        this.simCompilerWorkflow = new Ev3SimCompilerWorkflow();
        addBlockTypesFromProperties("EV3dev.properties", this.ev3Properties);
    }

    @Override
    public IBlinkMode getBlinkMode(String mode) {
        if ( mode == null || mode.isEmpty() ) {
            throw new DbcException("Invalid Blink Mode: " + mode);
        }
        String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( BlinkMode mo : BlinkMode.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid Blink Mode: " + mode);
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
        if ( color == null || color.isEmpty() ) {
            throw new DbcException("Invalid Brick Led Color: " + color);
        }
        String sUpper = color.trim().toUpperCase(Locale.GERMAN);
        for ( BrickLedColor co : BrickLedColor.values() ) {
            if ( co.toString().equals(sUpper) ) {
                return co;
            }
            for ( String value : co.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return co;
                }
            }
        }
        throw new DbcException("Invalid Brick Led Color: " + color);

    }

    @Override
    public List<IBrickLedColor> getBrickLedColors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        if ( picture == null || picture.isEmpty() ) {
            throw new DbcException("Invalid Picture: " + picture);
        }
        String sUpper = picture.trim().toUpperCase(Locale.GERMAN);
        for ( ShowPicture pic : ShowPicture.values() ) {
            if ( pic.toString().equals(sUpper) ) {
                return pic;
            }
            for ( String value : pic.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return pic;
                }
            }
        }
        throw new DbcException("Invalid Picture: " + picture);
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
        if ( colorSensorMode == null || colorSensorMode.isEmpty() ) {
            throw new DbcException("Invalid Color Sensor Mode: " + colorSensorMode);
        }
        String sUpper = colorSensorMode.trim().toUpperCase(Locale.GERMAN);
        for ( ColorSensorMode sp : ColorSensorMode.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.getValues() ) {
                if ( sUpper.equals(value.toUpperCase()) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid Color Sensor Mode: " + colorSensorMode);
    }

    @Override
    public List<IColorSensorMode> getColorSensorModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ILightSensorMode getLightSensorMode(String lightrSensorMode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ILightSensorMode> getLightSensorModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ISoundSensorMode getSoundSensorMode(String soundSensorMode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ISoundSensorMode> getSoundSensorModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IGyroSensorMode getGyroSensorMode(String gyroSensorMode) {
        if ( gyroSensorMode == null || gyroSensorMode.isEmpty() ) {
            throw new DbcException("Invalid Gyro Sensor Mode: " + gyroSensorMode);
        }
        String sUpper = gyroSensorMode.trim().toUpperCase(Locale.GERMAN);
        for ( GyroSensorMode sp : GyroSensorMode.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid Gyro Sensor Mode: " + gyroSensorMode);
    }

    @Override
    public List<IGyroSensorMode> getGyroSensorModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IInfraredSensorMode getInfraredSensorMode(String infraredSensorMode) {
        if ( infraredSensorMode == null || infraredSensorMode.isEmpty() ) {
            throw new DbcException("Invalid Infrared Sensor Mode: " + infraredSensorMode);
        }
        String sUpper = infraredSensorMode.trim().toUpperCase(Locale.GERMAN);
        for ( InfraredSensorMode inf : InfraredSensorMode.values() ) {
            if ( inf.toString().equals(sUpper) ) {
                return inf;
            }
            for ( String value : inf.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return inf;
                }
            }
        }
        throw new DbcException("Invalid Infrared Sensor Mode: " + infraredSensorMode);
    }

    @Override
    public List<IInfraredSensorMode> getInfraredSensorModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMotorTachoMode getMotorTachoMode(String motorTachoMode) {
        if ( motorTachoMode == null || motorTachoMode.isEmpty() ) {
            throw new DbcException("Invalid Motor Tacho Mode: " + motorTachoMode);
        }
        String sUpper = motorTachoMode.trim().toUpperCase(Locale.GERMAN);
        for ( MotorTachoMode motorTacho : MotorTachoMode.values() ) {
            if ( motorTacho.toString().equals(sUpper) ) {
                return motorTacho;
            }
            for ( String value : motorTacho.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return motorTacho;
                }
            }
        }
        throw new DbcException("Invalid Motor Tacho Mode: " + motorTachoMode);
    }

    @Override
    public List<IMotorTachoMode> getMotorTachoModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IUltrasonicSensorMode getUltrasonicSensorMode(String ultrasonicSensorMode) {
        if ( ultrasonicSensorMode == null || ultrasonicSensorMode.isEmpty() ) {
            throw new DbcException("Invalid Ultrasonic Sensor Mode: " + ultrasonicSensorMode);
        }
        String sUpper = ultrasonicSensorMode.trim().toUpperCase(Locale.GERMAN);
        for ( UltrasonicSensorMode ultra : UltrasonicSensorMode.values() ) {
            if ( ultra.toString().equals(sUpper) ) {
                return ultra;
            }
            for ( String value : ultra.getValues() ) {
                if ( sUpper.equals(value.toUpperCase()) ) {
                    return ultra;
                }
            }
        }
        throw new DbcException("Invalid Ultrasonic Sensor Mode: " + ultrasonicSensorMode);
    }

    @Override
    public List<IUltrasonicSensorMode> getUltrasonicSensorModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ITouchSensorMode getTouchSensorMode(String mode) {
        if ( mode == null || mode.isEmpty() ) {
            throw new DbcException("Invalid Touch Sensor Mode: " + mode);
        }
        String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( TouchSensorMode ultra : TouchSensorMode.values() ) {
            if ( ultra.toString().equals(sUpper) ) {
                return ultra;
            }
            for ( String value : ultra.getValues() ) {
                if ( sUpper.equals(value.toUpperCase()) ) {
                    return ultra;
                }
            }
        }
        throw new DbcException("Invalid Touch Sensor Mode: " + mode);
    }

    @Override
    public List<ITouchSensorMode> getTouchSensorModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        if ( port == null || port.isEmpty() ) {
            throw new DbcException("Invalid Ultrasonic Sensor Mode: " + port);
        }
        String sUpper = port.trim().toUpperCase(Locale.GERMAN);
        for ( SensorPort po : SensorPort.values() ) {
            if ( po.toString().equals(sUpper) ) {
                return po;
            }
            for ( String value : po.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return po;
                }
            }
        }
        throw new DbcException("Invalid Ultrasonic Sensor Mode: " + port);
    }

    @Override
    public List<ISensorPort> getSensorPorts() {
        // TODO Auto-generated method stub
        return null;
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
    public AbstractModule getGuiceModule() {
        return new Ev3GuiceModule(RobertaProperties.getRobertaProperties());
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
        //FIX ME: when the ev3lejos and ev3dev are splitted then we will have different file extension
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
        return this.ev3Properties.getProperty("robot.sim") != null ? true : false;
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
    public Boolean isAutoconnected() {
        return this.ev3Properties.getProperty("robot.connection.server") != null ? true : false;
    }

    @Override
    public SimulationProgramCheckVisitor getProgramCheckVisitor(Configuration brickConfiguration) {
        return new SimulationProgramCheckVisitor(brickConfiguration);
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
        // TODO Auto-generated method stub
        return null;
    }
}
