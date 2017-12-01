package de.fhg.iais.roberta.factory;

import java.util.ArrayList;
import java.util.Locale;

import com.google.inject.AbstractModule;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IBlinkMode;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.action.IMotorMoveMode;
import de.fhg.iais.roberta.inter.mode.action.IMotorSide;
import de.fhg.iais.roberta.inter.mode.action.IMotorStopMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.inter.mode.general.IDirection;
import de.fhg.iais.roberta.inter.mode.general.IIndexLocation;
import de.fhg.iais.roberta.inter.mode.general.IListElementOperations;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.general.IPickColor;
import de.fhg.iais.roberta.inter.mode.general.IWorkingState;
import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ICompassSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ICoordinatesMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IMotorTachoMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISoundSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITemperatureSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITimerSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IVoltageSensorMode;
import de.fhg.iais.roberta.mode.action.BlinkMode;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.MoveDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.general.Direction;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.mode.general.PickColor;
import de.fhg.iais.roberta.mode.sensor.CompassSensorMode;
import de.fhg.iais.roberta.mode.sensor.CoordinatesMode;
import de.fhg.iais.roberta.mode.sensor.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.mode.sensor.LightSensorMode;
import de.fhg.iais.roberta.mode.sensor.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.mode.sensor.SoundSensorMode;
import de.fhg.iais.roberta.mode.sensor.TemperatureSensorMode;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.mode.sensor.TouchSensorMode;
import de.fhg.iais.roberta.mode.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.mode.sensor.VoltageSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.util.dbc.DbcException;

public interface IRobotFactory {

    static <E extends IMode> E getModeValue(String modeName, Class<E> modes) {
        if ( (modeName == null) || modeName.isEmpty() ) {
            throw new DbcException("Invalid " + modes.getName() + ": " + modeName);
        }
        final String sUpper = modeName.trim().toUpperCase(Locale.GERMAN);
        for ( final E mode : modes.getEnumConstants() ) {
            if ( mode.toString().equals(sUpper) ) {
                return mode;
            }
            for ( final String value : mode.getValues() ) {
                if ( sUpper.equals(value.toUpperCase()) ) {
                    return mode;
                }
            }
        }
        throw new DbcException("Invalid " + modes.getName() + ": " + modeName);
    }

    /**
     * Get index location enumeration from {@link IIndexLocation} given string parameter. It is possible for one index location to have multiple string
     * mappings. Throws exception if the operator does not exists.
     *
     * @param indexLocation of the function
     * @return index location from the enum {@link IIndexLocation}
     */
    default IIndexLocation getIndexLocation(String indexLocation) {
        return IRobotFactory.getModeValue(indexLocation, IndexLocation.class);
    }

    /**
     * Direction in space enumeration from {@link IDirection} given string parameter. It is possible for one direction to have multiple string
     * mappings. Throws exception if the operator does not exists.
     *
     * @param direction of the function
     * @return direction location from the enum {@link IDirection}
     */
    default IDirection getDirection(String direction) {
        return IRobotFactory.getModeValue(direction, Direction.class);
    }

    /**
     * Get array element operation enumeration from {@link IListElementOperations} given string parameter. It is possible for one operation to have multiple
     * string mappings. Throws exception if the operator does not exists.
     *
     * @param operation string name
     * @return operation from the enum {@link IListElementOperations}
     */
    default IListElementOperations getListElementOpertaion(String operation) {
        return IRobotFactory.getModeValue(operation, ListElementOperations.class);
    }

    /**
     * Get a {@link IPickColor} enumeration given string parameter. It is possible for one color to have multiple string mappings. Throws exception if the color
     * cannot be found.
     *
     * @param name of the color
     * @return enum {@link IPickColor}
     */
    default IPickColor getPickColor(String color) {
        return IRobotFactory.getModeValue(color, PickColor.class);
    }

    /**
     * Get a {@link IPickColor} enumeration given a color id.
     *
     * @param id of the color
     * @return enum {@link IPickColor}
     */
    default IPickColor getPickColor(int colorId) {
        for ( final PickColor sp : PickColor.values() ) {
            if ( sp.getColorID() == colorId ) {
                return sp;
            }
        }
        throw new DbcException("Invalid color: " + colorId);
    }

    /**
     * Get a {@link IBlinkMode} enumeration given string parameter. It is possible for one mode to have multiple string mappings. Throws exception if the mode
     * does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link BlinkMode}
     */
    IBlinkMode getBlinkMode(String mode);

    /**
     * Get a {@link IBrickLedColor} enumeration given string parameter. It is possible for one mode to have multiple string mappings. Throws exception if the
     * mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link IBlinkMode}
     */
    IBrickLedColor getBrickLedColor(String mode);

    /**
     * Get a {@link ILightSensorMode} enumeration given string parameter. It is possible for one mode to have multiple string mappings. Throws exception if the
     * mode does not exists.
     */
    ILightSensorMode getLightColor(String mode);

    ILightSensorActionMode getLightActionColor(String mode);

    IWorkingState getWorkingState(String mode);

    /**
     * Get a {@link IShowPicture} enumeration given string parameter. It is possible for one picture to have multiple string mappings. Throws exception if the
     * mode does not exists.
     *
     * @param name of the picture
     * @return picture from the enum {@link IShowPicture}
     */
    IShowPicture getShowPicture(String picture);

    /**
     * Get a {@link ITurnDirection} enumeration given string parameter. It is possible for one turn direction to have multiple string mappings. Throws exception
     * if the mode does not exists.
     *
     * @param name of the turn direction
     * @return turn direction from the enum {@link ITurnDirection}
     */
    default ITurnDirection getTurnDirection(String direction) {
        return IRobotFactory.getModeValue(direction, TurnDirection.class);
    }

    /**
     * Get a {@link IMotorMoveMode} enumeration given string parameter. It is possible for one motor move mode to have multiple string mappings. Throws
     * exception if the mode does not exists.
     *
     * @param name of the motor move mode
     * @return motor move mode from the enum {@link IMotorMoveMode}
     */
    default IMotorMoveMode getMotorMoveMode(String mode) {
        return IRobotFactory.getModeValue(mode, MotorMoveMode.class);
    }

    /**
     * Get actor port from {@link IActorPort} enumeration given string parameter. It is possible for actor port to have multiple string mappings. Throws
     * exception if the actor port does not exists.
     *
     * @param name of the actor port
     * @return actor port from the enum {@link IActorPort}
     */
    IActorPort getActorPort(String port);

    /**
     * Get stopping mode from {@link IMotorStopMode} from string parameter. It is possible for one stopping mode to have multiple string mappings. Throws
     * exception if the stopping mode does not exists.
     *
     * @param name of the stopping mode
     * @return name of the stopping mode from the enum {@link IMotorStopMode}
     */
    default IMotorStopMode getMotorStopMode(String mode) {
        return IRobotFactory.getModeValue(mode, MotorStopMode.class);
    }

    /**
     * Get motor side from {@link IMotorSide} given string parameter. It is possible for one motor side to have multiple string mappings. Throws exception if
     * the motor side does not exists.
     *
     * @param name of the motor side
     * @return the motor side from the enum {@link IMotorSide}
     */
    default IMotorSide getMotorSide(String motorSide) {
        return IRobotFactory.getModeValue(motorSide, MotorSide.class);
    }

    /**
     * Get drive direction from {@link IDriveDirection} given string parameter. It is possible for one drive direction to have multiple string mappings. Throws
     * exception if the motor side does not exists.
     *
     * @param name of the drive direction
     * @return the drive direction from the enum {@link IDriveDirection}
     */
    default IDriveDirection getDriveDirection(String driveDirection) {
        return IRobotFactory.getModeValue(driveDirection, MoveDirection.class);
    }

    /**
     * Get a robot key from {@link IBrickKey} given string parameter. It is possible for one robot key to have multiple string mappings. Throws exception if the
     * robot key does not exists.
     *
     * @param name of the robot key
     * @return the robot keys from the enum {@link IBrickKey}
     */
    IBrickKey getBrickKey(String brickKey);

    /**
     * Get a color sensor mode from {@link IColorSensorMode} given string parameter. It is possible for one color sensor mode to have multiple string mappings.
     * Throws exception if the color sensor mode does not exists.
     *
     * @param name of the color sensor mode
     * @return the color sensor mode from the enum {@link IColorSensorMode}
     */
    IColorSensorMode getColorSensorMode(String modeName);

    IJoystickMode getJoystickMode(String modeName);

    default ILightSensorMode getLightSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, LightSensorMode.class);
    }

    default ICompassSensorMode getCompassSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, CompassSensorMode.class);
    }

    default ITemperatureSensorMode getTemperatureSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, TemperatureSensorMode.class);
    }

    default ICoordinatesMode getAccelerometerSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, CoordinatesMode.class);
    }

    default ISoundSensorMode getSoundSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, SoundSensorMode.class);
    }

    /**
     * Get a gyro sensor mode from {@link IGyroSensorMode} given string parameter. It is possible for one gyro sensor mode to have multiple string mappings.
     * Throws exception if the gyro sensor mode does not exists.
     *
     * @param name of the gyro sensor mode
     * @return the gyro sensor mode from the enum {@link IGyroSensorMode}
     */
    default IGyroSensorMode getGyroSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, GyroSensorMode.class);
    }

    /**
     * Get a infrared sensor mode from {@link IInfraredSensorMode} given string parameter. It is possible for one infrared sensor mode to have multiple string
     * mappings. Throws exception if the infrared sensor mode does not exists.
     *
     * @param name of the infrared sensor mode
     * @return the infrared sensor mode from the enum {@link IInfraredSensorMode}
     */
    default IInfraredSensorMode getInfraredSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, InfraredSensorMode.class);
    }

    /**
     * Get a timer sensor mode from {@link ITimerSensorMode} given string parameter. It is possible for one timer sensor mode to have multiple string mappings.
     * Throws exception if the timer sensor mode does not exists.
     *
     * @param name of the timer sensor mode
     * @return the timer sensor mode from the enum {@link ITimerSensorMode}
     */
    default ITimerSensorMode getTimerSensorMode(String modeName) {
        return IRobotFactory.getModeValue(modeName, TimerSensorMode.class);
    }

    /**
     * Get a motor tacho sensor mode from {@link IMotorTachoMode} given string parameter. It is possible for one motor tacho sensor mode to have multiple string
     * mappings. Throws exception if the motor tacho sensor mode does not exists.
     *
     * @param name of the motor tacho sensor mode
     * @return the motor tacho sensor mode from the enum {@link IMotorTachoMode}
     */
    default IMotorTachoMode getMotorTachoMode(String mode) {
        return IRobotFactory.getModeValue(mode, MotorTachoMode.class);
    }

    /**
     * Get a ultrasonic sensor mode from {@link IUltrasonicSensorMode} given string parameter. It is possible for one ultrasonic sensor mode to have multiple
     * string mappings. Throws exception if the ultrasonic sensor mode does not exists.
     *
     * @param name of the ultrasonic sensor mode
     * @return the ultrasonic sensor mode from the enum {@link IUltrasonicSensorMode}
     */
    default IUltrasonicSensorMode getUltrasonicSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, UltrasonicSensorMode.class);
    }

    /**
     * Get a touch sensor mode from {@link ITouchSensorMode} given string parameter. It is possible for one touch sensor mode to have multiple string mappings.
     * Throws exception if the touch sensor mode does not exists.
     *
     * @param name of the touch sensor mode
     * @return the touch sensor mode from the enum {@link ITouchSensorMode}
     */
    default ITouchSensorMode getTouchSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, TouchSensorMode.class);
    }

    /**
     * Get a touch sensor mode from {@link IVoltageSensorMode} given string parameter. It is possible for one voltage sensor mode to have multiple string
     * mappings.
     * Throws exception if the voltage sensor mode does not exists.
     *
     * @param name of the voltage sensor mode
     * @return the volatage sensor mode from the enum {@link IVoltageSensorMode}
     */
    default IVoltageSensorMode getVoltageSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, VoltageSensorMode.class);
    }

    /**
     * Get a sensor port from {@link ISensorPort} given string parameter. It is possible for one sensor port to have multiple string mappings. Throws exception
     * if the sensor port does not exists.
     *
     * @param name of the sensor port
     * @return the sensor port from the enum {@link ISensorPort}
     */
    default ISensorPort getSensorPort(String port) {
        return IRobotFactory.getModeValue(port, SensorPort.class);
    }

    /**
     * Get the compiler workflow object for this robot.
     *
     * @return
     */
    ICompilerWorkflow getRobotCompilerWorkflow();

    /**
     * Get the compiler workflow object for this simulation.
     *
     * @return
     */
    ICompilerWorkflow getSimCompilerWorkflow();

    /**
     * Get the guice module for this robot. This is used to add bindings to guice. It should not be used often.<br>
     * Example: the /download REST resource needs access to the directory, in which binaries are stored (EV3 lejos robot)
     *
     * @return the guice module for this robot or <code>null</code>, if this robot doesn't need to inject resources
     */
    default AbstractModule getGuiceModule() {
        return null;
    }

    /**
     * Get the file extension of the specific language for this robot. This is used when we want to download
     * locally the source code into a file.
     *
     * @return
     */
    String getFileExtension();

    String getProgramToolboxBeginner();

    String getProgramToolboxExpert();

    String getProgramDefault();

    String getConfigurationToolbox();

    String getConfigurationDefault();

    String getRealName();

    Boolean hasSim();

    String getInfo();

    Boolean isBeta();

    String getConnectionType();

    String getVendorId();

    String getCommandline();

    String getSignature();

    Boolean hasConfiguration();

    RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration);

    RobotBrickCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration);

    String getGroup();

    String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping);

}
