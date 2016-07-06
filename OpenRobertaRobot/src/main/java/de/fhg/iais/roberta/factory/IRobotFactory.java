package de.fhg.iais.roberta.factory;

import java.util.List;

import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IBlinkMode;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.IMotorMoveMode;
import de.fhg.iais.roberta.inter.mode.action.IMotorSide;
import de.fhg.iais.roberta.inter.mode.action.IMotorStopMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.inter.mode.general.IIndexLocation;
import de.fhg.iais.roberta.inter.mode.general.IListElementOperations;
import de.fhg.iais.roberta.inter.mode.general.IPickColor;
import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IMotorTachoMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ITimerSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.robotCommunication.ICompilerWorkflow;

public interface IRobotFactory {
    /**
     * Get index location enumeration from {@link IIndexLocation} given string parameter. It is possible for one index location to have multiple string
     * mappings.
     * Throws exception if the operator does not exists.
     *
     * @param indexLocation of the function
     * @return index location from the enum {@link IIndexLocation}
     */
    public IIndexLocation getIndexLocation(String indexLocation);

    /**
     * @return list of all possible enumeration values
     */
    public List<IIndexLocation> getIndexLocations();

    /**
     * Get array element operation enumeration from {@link IListElementOperations} given string parameter. It is possible for one operation to have multiple
     * string
     * mappings.
     * Throws exception if the operator does not exists.
     *
     * @param operation string name
     * @return operation from the enum {@link IListElementOperations}
     */
    public IListElementOperations getListElementOpertaion(String operation);

    public List<IListElementOperations> getListElementOpertaions();

    /**
     * Get a {@link IPickColor} enumeration given string parameter. It is possible for one color to have multiple string mappings.
     * Throws exception if the color cannot be found.
     *
     * @param name of the color
     * @return enum {@link IPickColor}
     */
    public IPickColor getPickColor(String color);

    /**
     * Get a {@link IPickColor} enumeration given a color id.
     *
     * @param id of the color
     * @return enum {@link IPickColor}
     */
    public IPickColor getPickColor(int colorId);

    public List<IPickColor> getPickColor();

    /**
     * Get a {@link IBlinkMode} enumeration given string parameter. It is possible
     * for one mode to have multiple string mappings. Throws exception if
     * the mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link BlinkMode}
     */
    public IBlinkMode getBlinkMode(String mode);

    public List<IBlinkMode> getBlinkModes();

    /**
     * Get a {@link IBrickLedColor} enumeration given string parameter. It is possible
     * for one mode to have multiple string mappings. Throws exception if
     * the mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link IBlinkMode}
     */
    public IBrickLedColor getBrickLedColor(String mode);

    public List<IBrickLedColor> getBrickLedColors();

    /**
     * Get a {@link IShowPicture} enumeration given string parameter. It is possible
     * for one picture to have multiple string mappings. Throws exception if
     * the mode does not exists.
     *
     * @param name of the picture
     * @return picture from the enum {@link IShowPicture}
     */
    public IShowPicture getShowPicture(String picture);

    public List<IShowPicture> getShowPictures();

    /**
     * Get a {@link ITurnDirection} enumeration given string parameter. It is possible
     * for one turn direction to have multiple string mappings. Throws exception if
     * the mode does not exists.
     *
     * @param name of the turn direction
     * @return turn direction from the enum {@link ITurnDirection}
     */
    public ITurnDirection getTurnDirection(String direction);

    public List<ITurnDirection> getTurnDirections();

    /**
     * Get a {@link IMotorMoveMode} enumeration given string parameter. It is possible
     * for one motor move mode to have multiple string mappings. Throws exception if
     * the mode does not exists.
     *
     * @param name of the motor move mode
     * @return motor move mode from the enum {@link IMotorMoveMode}
     */
    public IMotorMoveMode getMotorMoveMode(String mode);

    public List<IMotorMoveMode> getMotorMoveModes();

    /**
     * Get actor port from {@link IActorPort} enumeration given string parameter. It is possible for actor port to have multiple string mappings.
     * Throws exception if the actor port does not exists.
     *
     * @param name of the actor port
     * @return actor port from the enum {@link IActorPort}
     */
    public IActorPort getActorPort(String port);

    public List<IActorPort> getActorPorts();

    /**
     * Get stopping mode from {@link IMotorStopMode} from string parameter. It is possible for one stopping mode to have multiple string mappings.
     * Throws exception if the stopping mode does not exists.
     *
     * @param name of the stopping mode
     * @return name of the stopping mode from the enum {@link IMotorStopMode}
     */
    public IMotorStopMode getMotorStopMode(String mode);

    public List<IMotorStopMode> getMotorStopModes();

    /**
     * Get motor side from {@link IMotorSide} given string parameter. It is possible for one motor side to have multiple string mappings.
     * Throws exception if the motor side does not exists.
     *
     * @param name of the motor side
     * @return the motor side from the enum {@link IMotorSide}
     */
    public IMotorSide getMotorSide(String motorSide);

    public List<IMotorStopMode> getMotorSides();

    /**
     * Get drive direction from {@link IDriveDirection} given string parameter. It is possible for one drive direction to have multiple string mappings.
     * Throws exception if the motor side does not exists.
     *
     * @param name of the drive direction
     * @return the drive direction from the enum {@link IDriveDirection}
     */
    public IDriveDirection getDriveDirection(String driveDirection);

    public List<IDriveDirection> getDriveDirections();

    /**
     * Get a robot key from {@link IBrickKey} given string parameter. It is possible for one robot key to have multiple string mappings.
     * Throws exception if the robot key does not exists.
     *
     * @param name of the robot key
     * @return the robot keys from the enum {@link IBrickKey}
     */
    public IBrickKey getBrickKey(String brickKey);

    public List<IBrickKey> getBrickKeys();

    /**
     * Get a color sensor mode from {@link IColorSensorMode} given string parameter. It is possible for one color sensor mode to have multiple string mappings.
     * Throws exception if the color sensor mode does not exists.
     *
     * @param name of the color sensor mode
     * @return the color sensor mode from the enum {@link IColorSensorMode}
     */
    public IColorSensorMode getColorSensorMode(String colorSensorMode);

    public List<IColorSensorMode> getColorSensorModes();

    /**
     * Get a gyro sensor mode from {@link IGyroSensorMode} given string parameter. It is possible for one gyro sensor mode to have multiple string mappings.
     * Throws exception if the gyro sensor mode does not exists.
     *
     * @param name of the gyro sensor mode
     * @return the gyro sensor mode from the enum {@link IGyroSensorMode}
     */
    public IGyroSensorMode getGyroSensorMode(String gyroSensorMode);

    public List<IGyroSensorMode> getGyroSensorModes();

    /**
     * Get a infrared sensor mode from {@link IInfraredSensorMode} given string parameter. It is possible for one infrared sensor mode to have multiple string
     * mappings.
     * Throws exception if the infrared sensor mode does not exists.
     *
     * @param name of the infrared sensor mode
     * @return the infrared sensor mode from the enum {@link IInfraredSensorMode}
     */
    IInfraredSensorMode getInfraredSensorMode(String infraredSensorMode);

    List<IInfraredSensorMode> getInfraredSensorModes();

    /**
     * Get a timer sensor mode from {@link ITimerSensorMode} given string parameter. It is possible for one timer sensor mode to have multiple string
     * mappings.
     * Throws exception if the timer sensor mode does not exists.
     *
     * @param name of the timer sensor mode
     * @return the timer sensor mode from the enum {@link ITimerSensorMode}
     */
    ITimerSensorMode getTimerSensorMode(String timerSensroMode);

    List<ITimerSensorMode> getTimerSensorModes();

    /**
     * Get a motor tacho sensor mode from {@link IMotorTachoMode} given string parameter. It is possible for one motor tacho sensor mode to have multiple string
     * mappings.
     * Throws exception if the motor tacho sensor mode does not exists.
     *
     * @param name of the motor tacho sensor mode
     * @return the motor tacho sensor mode from the enum {@link IMotorTachoMode}
     */
    IMotorTachoMode getMotorTachoMode(String motorTachoMode);

    List<IMotorTachoMode> getMotorTachoModes();

    /**
     * Get a ultrasonic sensor mode from {@link IUltrasonicSensorMode} given string parameter. It is possible for one ultrasonic sensor mode to have multiple
     * string
     * mappings.
     * Throws exception if the ultrasonic sensor mode does not exists.
     *
     * @param name of the ultrasonic sensor mode
     * @return the ultrasonic sensor mode from the enum {@link IUltrasonicSensorMode}
     */
    IUltrasonicSensorMode getUltrasonicSensorMode(String ultrasonicSensorMode);

    List<IUltrasonicSensorMode> getUltrasonicSensorModes();

    /**
     * Get a touch sensor mode from {@link ITouchSensorMode} given string parameter. It is possible for one touch sensor mode to have multiple
     * string
     * mappings.
     * Throws exception if the touch sensor mode does not exists.
     *
     * @param name of the touch sensor mode
     * @return the touch sensor mode from the enum {@link ITouchSensorMode}
     */
    ITouchSensorMode getTouchSensorMode(String mode);

    List<ITouchSensorMode> getTouchSensorModes();

    /**
     * Get a sensor port from {@link ISensorPort} given string parameter. It is possible for one sensor port to have multiple
     * string mappings.
     * Throws exception if the sensor port does not exists.
     *
     * @param name of the sensor port
     * @return the sensor port from the enum {@link ISensorPort}
     */
    ISensorPort getSensorPort(String port);

    List<ISensorPort> getSensorPorts();

    ICompilerWorkflow getCompilerWorkflow();

}
