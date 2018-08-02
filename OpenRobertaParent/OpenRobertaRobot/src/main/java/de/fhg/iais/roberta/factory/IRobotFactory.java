package de.fhg.iais.roberta.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.google.inject.AbstractModule;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.inter.mode.action.ILightMode;
import de.fhg.iais.roberta.inter.mode.action.IMotorMoveMode;
import de.fhg.iais.roberta.inter.mode.action.IMotorSide;
import de.fhg.iais.roberta.inter.mode.action.IMotorStopMode;
import de.fhg.iais.roberta.inter.mode.action.IRelayMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.inter.mode.general.IDirection;
import de.fhg.iais.roberta.inter.mode.general.IIndexLocation;
import de.fhg.iais.roberta.inter.mode.general.IListElementOperations;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.general.IPickColor;
import de.fhg.iais.roberta.inter.mode.general.IWorkingState;
import de.fhg.iais.roberta.inter.mode.sensor.IBirckKeyPressMode;
import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ICompassSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ICoordinatesMode;
import de.fhg.iais.roberta.inter.mode.sensor.IDropSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGestureSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IHumiditySensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IIRSeekerSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IMoistureSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IMotionSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IMotorTachoMode;
import de.fhg.iais.roberta.inter.mode.sensor.IPinPull;
import de.fhg.iais.roberta.inter.mode.sensor.IPinValue;
import de.fhg.iais.roberta.inter.mode.sensor.IPulseSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IRSeekerSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IRfidSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISlot;
import de.fhg.iais.roberta.inter.mode.sensor.ISoundSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITemperatureSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITimerSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IVoltageSensorMode;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.action.LightMode;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.RelayMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.general.Direction;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.mode.general.PickColor;
import de.fhg.iais.roberta.mode.general.PlaceholderSensorMode;
import de.fhg.iais.roberta.mode.general.WorkingState;
import de.fhg.iais.roberta.mode.sensor.Axis;
import de.fhg.iais.roberta.mode.sensor.BrickKeyPressMode;
import de.fhg.iais.roberta.mode.sensor.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.CompassSensorMode;
import de.fhg.iais.roberta.mode.sensor.DropSensorMode;
import de.fhg.iais.roberta.mode.sensor.GestureSensorMode;
import de.fhg.iais.roberta.mode.sensor.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.HumiditySensorMode;
import de.fhg.iais.roberta.mode.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.mode.sensor.LightSensorMode;
import de.fhg.iais.roberta.mode.sensor.MoistureSensorMode;
import de.fhg.iais.roberta.mode.sensor.MotionSensorMode;
import de.fhg.iais.roberta.mode.sensor.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.PinPull;
import de.fhg.iais.roberta.mode.sensor.PinValue;
import de.fhg.iais.roberta.mode.sensor.PulseSensorMode;
import de.fhg.iais.roberta.mode.sensor.RfidSensorMode;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.mode.sensor.Slot;
import de.fhg.iais.roberta.mode.sensor.SoundSensorMode;
import de.fhg.iais.roberta.mode.sensor.TemperatureSensorMode;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.mode.sensor.TouchSensorMode;
import de.fhg.iais.roberta.mode.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.mode.sensor.VoltageSensorMode;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotCommonCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.sensor.GetSampleType;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;

public interface IRobotFactory {

    static <E extends IMode> E getModeValue(String modeName, Class<E> modes) {
        if ( modeName == null ) {
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

    static Map<String, SensorPort> getSensorPortsFromProperties(Properties properties) {
        Map<String, SensorPort> sensorToPorts = new HashMap<>();
        for ( Entry<Object, Object> property : properties.entrySet() ) {
            SensorPort sensorPort = new SensorPort((String) property.getKey(), (String) property.getValue());
            sensorToPorts.put((String) property.getKey(), sensorPort);
        }
        return sensorToPorts;
    }

    /**
     * Get a sensor port from {@link ISensorPort} given string parameter and mapping from a port to SensorPort. It is possible for one sensor port to have
     * multiple string mappings. Throws exception if the sensor port does not exists. <br>
     * It can only be used by subclasses of IRobotFactory.
     *
     * @param name of the sensor port
     * @param port-SensorPort map
     * @return SensorPort {@link ISensorPort}
     */
    default ISensorPort getSensorPortValue(String port, Map<String, SensorPort> sensorToPorts) {
        if ( port == null ) {
            throw new DbcException("Null sensor port!");
        }
        if ( port.isEmpty() ) {
            return sensorToPorts.get("NO_PORT");
        }
        final String sUpper = port.trim().toUpperCase(Locale.GERMAN);
        SensorPort sensorPort = sensorToPorts.get(sUpper);
        if ( sensorPort != null ) {
            return sensorPort;
        }
        sensorPort = sensorToPorts.get(port);
        if ( sensorPort != null ) {
            return sensorPort;
        }
        for ( Map.Entry<String, SensorPort> portName : sensorToPorts.entrySet() ) {
            if ( portName.getValue().getCodeName().equals(port) ) {
                return portName.getValue();
            }
        }
        throw new DbcException("Invalid sensor port: " + port);
    }

    /**
     * Get a sensor port from {@link ISensorPort} given its name as a string parameter. It is possible for one sensor port to have
     * multiple
     * string mappings. Throws exception
     * if the sensor port does not exists.
     *
     * @param port name of the sensor
     * @return SensorPort {@link ISensorPort}
     */
    ISensorPort getSensorPort(String port);

    static Map<String, ActorPort> getActorPortsFromProperties(Properties properties) {
        Map<String, ActorPort> actorToPorts = new HashMap<>();
        for ( Entry<Object, Object> property : properties.entrySet() ) {
            ActorPort actorPort = new ActorPort((String) property.getKey(), (String) property.getValue());
            actorToPorts.put((String) property.getKey(), actorPort);
        }
        return actorToPorts;
    }

    /**
     * Get a actor port from {@link IActorPort} given string parameter and mapping from a port to ActorPort. It is possible for one sensor port to have
     * multiple string mappings. Throws exception if the sensor port does not exists. <br>
     * It can only be used by subclasses of IRobotFactory.
     *
     * @param name of the sensor port
     * @param port-ActorPort map
     * @return ActorPort {@link IActorPort}
     */
    default IActorPort getActorPortValue(String port, Map<String, ActorPort> actorToPorts) {
        if ( port == null ) {
            throw new DbcException("Null actor port!");
        }
        if ( port.isEmpty() ) {
            return actorToPorts.get("NO_PORT");
        }
        final String sUpper = port.trim().toUpperCase(Locale.GERMAN);
        ActorPort actorPort = actorToPorts.get(sUpper);
        if ( actorPort != null ) {
            return actorPort;
        }
        for ( Map.Entry<String, ActorPort> portName : actorToPorts.entrySet() ) {
            if ( portName.getValue().getCodeName().equals(port) ) {
                return portName.getValue();
            }
        }
        throw new DbcException("Invalid actor port: " + port);
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
     * Direction in space enumeration from {@link IDirection} given string parameter. It is possible for one direction to have multiple string mappings. Throws
     * exception if the operator does not exists.
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
     * Get a {@link ILightMode} enumeration given string parameter. It is possible for one mode to have multiple string mappings. Throws exception if the mode
     * does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link LightMode}
     */
    default ILightMode getBlinkMode(String mode) {
        return IRobotFactory.getModeValue(mode, LightMode.class);
    }

    /**
     * Get a {@link IBrickLedColor} enumeration given string parameter. It is possible for one mode to have multiple string mappings. Throws exception if the
     * mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link ILightMode}
     */
    default IBrickLedColor getBrickLedColor(String mode) {
        return IRobotFactory.getModeValue(mode, BrickLedColor.class);
    }

    /**
     * Get a {@link ISensorMode} enumeration given string parameter. It is possible for one color to have multiple string mappings. Throws exception if the
     * color
     * cannot be found.
     *
     * @param name of the color
     * @return enum {@link ISensorMode}
     */
    default ISensorMode getPlaceholderSensorMode(String color) {
        return IRobotFactory.getModeValue(color, PlaceholderSensorMode.class);
    }

    /**
     * Get a {@link ILightSensorMode} enumeration given string parameter. It is possible for one mode to have multiple string mappings. Throws exception if the
     * mode does not exists.
     */
    default ILightSensorMode getLightColor(String mode) {
        return IRobotFactory.getModeValue(mode, LightSensorMode.class);
    }

    default IWorkingState getWorkingState(String mode) {
        return IRobotFactory.getModeValue(mode, WorkingState.class);
    }

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
        return IRobotFactory.getModeValue(driveDirection, DriveDirection.class);
    }

    /**
     * Get relay mode {@link IRelayMode} given string parameter. Throws exception if the mode does not exists.
     *
     * @param name of the mode
     * @return the drelay mode from the enum {@link IRelayMode}
     */
    default IRelayMode getRelayMode(String relayMode) {
        return IRobotFactory.getModeValue(relayMode, RelayMode.class);
    }

    /**
     * Get a robot key from {@link IBrickKey} given string parameter. It is possible for one robot key to have multiple string mappings. Throws exception if the
     * robot key does not exists.
     *
     * @param name of the robot key
     * @return the robot keys from the enum {@link IBrickKey}
     */
    default IBirckKeyPressMode getBrickKeyPressMode(String brickKey) {
        return IRobotFactory.getModeValue(brickKey, BrickKeyPressMode.class);
    }

    /**
     * Get a color sensor mode from {@link IColorSensorMode} given string parameter. It is possible for one color sensor mode to have multiple string mappings.
     * Throws exception if the color sensor mode does not exists.
     *
     * @param name of the color sensor mode
     * @return the color sensor mode from the enum {@link IColorSensorMode}
     */
    default IColorSensorMode getColorSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, ColorSensorMode.class);
    }

    default IJoystickMode getJoystickMode(String mode) {
        return IRobotFactory.getModeValue(mode, Axis.class);
    }

    default ILightSensorMode getLightSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, LightSensorMode.class);
    }

    default ICompassSensorMode getCompassSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, CompassSensorMode.class);
    }

    default IPinValue getPinGetValueSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, PinValue.class);
    }

    default IPinPull getPinPullMode(String mode) {
        return IRobotFactory.getModeValue(mode, PinPull.class);
    }

    default ITemperatureSensorMode getTemperatureSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, TemperatureSensorMode.class);
    }

    default ICoordinatesMode getAxis(String mode) {
        return IRobotFactory.getModeValue(mode, Axis.class);
    }

    default ISoundSensorMode getSoundSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, SoundSensorMode.class);
    }

    default ILanguage getLanguageMode(String mode) {
        return IRobotFactory.getModeValue(mode, Language.class);
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
     * Get a IRSeeker sensor mode from {@link IIRSeekerSensorMode} given string parameter. It is possible for one IRSeeker sensor mode to have multiple string
     * mappings. Throws exception if the IRSeeker sensor mode does not exists.
     *
     * @param name of the IRSeeker sensor mode
     * @return the IRSeeker sensor mode from the enum {@link IRSeekerSensorMode}
     */
    default IRSeekerSensorMode getIRSeekerSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, IRSeekerSensorMode.class);
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
     * Get a voltage sensor mode from {@link IVoltageSensorMode} given string parameter. It is possible for one voltage sensor mode to have multiple string
     * mappings. Throws exception if the voltage sensor mode does not exists.
     *
     * @param name of the voltage sensor mode
     * @return the volatage sensor mode from the enum {@link IVoltageSensorMode}
     */
    default IVoltageSensorMode getVoltageSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, VoltageSensorMode.class);
    }

    /**
     * Get a moisture sensor mode from {@link IMoistureSensorMode} given string parameter. It is possible for one motion sensor mode to have multiple string
     * mappings. Throws exception if the moisture sensor mode does not exists.
     *
     * @param name of the moisture sensor mode
     * @return the moisture sensor mode from the enum {@link IMoistureSensorMode}
     */
    default IMoistureSensorMode getMoistureSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, MoistureSensorMode.class);
    }

    /**
     * Get a motion sensor mode from {@link IMoistureSensorMode} given string parameter. It is possible for one motion sensor mode to have multiple string
     * mappings. Throws exception if the motion sensor mode does not exists.
     *
     * @param name of the motion sensor mode
     * @return the motion sensor mode from the enum {@link IMoistureSensorMode}
     */
    default IMotionSensorMode getMotionSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, MotionSensorMode.class);
    }

    /**
     * Get a motion sensor mode from {@link IHumiditySensorMode} given string parameter. It is possible for one humidity sensor mode to have multiple string
     * mappings. Throws exception if the humidity sensor mode does not exists.
     *
     * @param name of the humidity sensor mode
     * @return the motion sensor mode from the enum {@link IMoistureSensorMode}
     */
    default IHumiditySensorMode getHumiditySensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, HumiditySensorMode.class);
    }

    /**
     * Get a drop sensor mode from {@link IHumiditySensorMode} given string parameter.
     * It is possible for one drop sensor mode to have multiple string
     * mappings. Throws exception if the drop sensor mode does not exists.
     *
     * @param name of the drop sensor mode
     * @return the drop sensor mode from the enum {@link IHumiditySensorMode}
     */
    default IDropSensorMode getDropSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, DropSensorMode.class);
    }

    /**
     * Get a pulse sensor mode from {@link IPulseSensorMode} given string parameter.
     * It is possible for one pulse sensor mode to have multiple string
     * mappings. Throws exception if the pulse sensor mode does not exists.
     *
     * @param name of the pulse sensor mode
     * @return the pulse sensor mode from the enum {@link IPulseSensorMode}
     */
    default IPulseSensorMode getPulseSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, PulseSensorMode.class);
    }

    /**
     * Get a RFID sensor mode from {@link IRfidSensorMode} given string parameter.
     * It is possible for one RFID sensor mode to have multiple string
     * mappings. Throws exception if the RFID sensor mode does not exists.
     *
     * @param name of the RFID sensor mode
     * @return the RFID sensor mode from the enum {@link IDropSensorMode}
     */
    default IRfidSensorMode getRfidSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, RfidSensorMode.class);
    }

    /**
     * Get a gesture sensor mode from {@link IGestureSensorMode} given string parameter. It is possible for one gesture sensor mode to have multiple string
     * mappings. Throws exception if the gesture sensor mode does not exists.
     *
     * @param name of the gesture sensor mode
     * @return the volatage sensor mode from the enum {@link IGestureSensorMode}
     */
    default IGestureSensorMode getGestureSensorMode(String mode) {
        return IRobotFactory.getModeValue(mode, GestureSensorMode.class);
    }

    /**
     * Get a sensor port from {@link ISensorPort} given string parameter. It is possible for one sensor port to have multiple string mappings. Throws exception
     * if the sensor port does not exists.
     *
     * @param name of the sensor port
     * @return the sensor port from the enum {@link ISensorPort}
     */
    default ISlot getSlot(String slot) {
        return IRobotFactory.getModeValue(slot, Slot.class);
    }

    /**
     * Creates an AST object representing sensor of specific type. If the type of the sensor does not exists it trows an exception.
     *
     * @param sensorType see {@link GetSampleType}
     * @param port on which the sensor is connected
     * @param slot on which the sensor is connected
     * @param properties of the block
     * @param comment of the block
     * @return returns instance of the specific sensor {@link Sensor}
     */
    default Sensor<?> createSensor(
        GetSampleType sensorType,
        String port,
        String slot,
        boolean isPortInMutation,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        SensorMetaDataBean sensorMetaDataBean;
        switch ( sensorType.getSensorType() ) {
            case BlocklyConstants.TOUCH:
                sensorMetaDataBean = new SensorMetaDataBean(getSensorPort(port), getTouchSensorMode("TOUCH"), getSlot(slot), isPortInMutation);
                return TouchSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.PINTOUCH:
                sensorMetaDataBean = new SensorMetaDataBean(getSensorPort(port), getTouchSensorMode("PINTOUCH"), getSlot(slot), isPortInMutation);
                return PinTouchSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.ULTRASONIC:
                sensorMetaDataBean =
                    new SensorMetaDataBean(getSensorPort(port), getUltrasonicSensorMode(sensorType.getSensorMode()), getSlot(slot), isPortInMutation);
                return UltrasonicSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.COLOUR:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getColorSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return ColorSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.INFRARED:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getInfraredSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return InfraredSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.ENCODER:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getActorPort(port),
                        getMotorTachoMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return EncoderSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.KEY_PRESSED:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getBrickKeyPressMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return BrickSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.GYRO:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getGyroSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return GyroSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.TIME:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getTimerSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return TimerSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.SOUND:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getSoundSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return SoundSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.LIGHT_VALUE:
            case BlocklyConstants.LIGHT:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getLightSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return LightSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.COMPASS:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getCompassSensorMode(BlocklyConstants.DEFAULT),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return CompassSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.TEMPERATURE:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getTemperatureSensorMode(BlocklyConstants.DEFAULT),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return TemperatureSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.PIN_ANALOG:
            case BlocklyConstants.PIN_DIGITAL:
            case BlocklyConstants.PIN_PULSE_HIGH:
            case BlocklyConstants.PIN_PULSE_LOW:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getPinGetValueSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return PinGetValueSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.ACCELERATION:
                sensorMetaDataBean =
                    new SensorMetaDataBean(getSensorPort(port), getAxis(BlocklyConstants.DEFAULT), getSlot(BlocklyConstants.EMPTY_SLOT), isPortInMutation);
                return AccelerometerSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.GESTURE:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getGestureSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return GestureSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.MOISTURE:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getMoistureSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return MoistureSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.POTENTIOMETER:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getVoltageSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return VoltageSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.HUMIDITY:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getHumiditySensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return HumiditySensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.MOTION:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getMotionSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return MotionSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.PULSE:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getPulseSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return PulseSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.DROP:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getDropSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return DropSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.RFID:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        getSensorPort(port),
                        getRfidSensorMode(sensorType.getSensorMode()),
                        getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                return RfidSensor.make(sensorMetaDataBean, properties, comment);
            default:
                throw new DbcException("Invalid sensor " + sensorType.getSensorType() + "!");
        }
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
     * Get the file extension of the specific language for this robot. This is used when we want to download locally the source code into a file.
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

    default String getVendorId() {
        return null;
    }

    default String getCommandline() {
        return null;
    }

    default String getSignature() {
        return null;
    }

    Boolean hasConfiguration();

    RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration);

    RobotCommonCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration);

    String getGroup();

    String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping);

    default String getMenuVersion() {
        return null;
    }
}
