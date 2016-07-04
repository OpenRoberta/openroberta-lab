package de.fhg.iais.roberta.factory;

import java.util.List;

import de.fhg.iais.roberta.factory.action.IActorPort;
import de.fhg.iais.roberta.factory.action.IBlinkMode;
import de.fhg.iais.roberta.factory.action.IBrickLedColor;
import de.fhg.iais.roberta.factory.action.IDriveDirection;
import de.fhg.iais.roberta.factory.action.IMotorMoveMode;
import de.fhg.iais.roberta.factory.action.IMotorSide;
import de.fhg.iais.roberta.factory.action.IMotorStopMode;
import de.fhg.iais.roberta.factory.action.IShowPicture;
import de.fhg.iais.roberta.factory.action.ITurnDirection;
import de.fhg.iais.roberta.factory.sensor.IBrickKey;
import de.fhg.iais.roberta.factory.sensor.IColorSensorMode;
import de.fhg.iais.roberta.factory.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.factory.sensor.IInfraredSensorMode;
import de.fhg.iais.roberta.factory.sensor.IMotorTachoMode;
import de.fhg.iais.roberta.factory.sensor.ISensorPort;
import de.fhg.iais.roberta.factory.sensor.ITimerSensorMode;
import de.fhg.iais.roberta.factory.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.generic.factory.action.BlinkMode;
import de.fhg.iais.roberta.generic.factory.action.MotorStopMode;

public interface IRobotFactory {
    /**
     * get mode from {@link BlinkMode} from string parameter. It is possible
     * for one mode to have multiple string mappings. Throws exception if
     * the mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link BlinkMode}
     */
    public IBlinkMode getBlinkMode(String mode);

    public List<IBlinkMode> getBlinkModes();

    public IBrickLedColor getBrickLedColor(String mode);

    public List<IBrickLedColor> getBrickLedColors();

    public IShowPicture getShowPicture(String picture);

    public List<IShowPicture> getShowPictures();

    public ITurnDirection getTurnDirection(String direction);

    public List<ITurnDirection> getTurnDirections();

    public IMotorMoveMode getMotorMoveMode(String mode);

    public List<IMotorMoveMode> getMotorMoveModes();

    public IActorPort getActorPort(String port);

    public List<IActorPort> getActorPorts();

    /**
     * Get stopping mode from {@link MotorStopMode} from string parameter. It is possible for one stopping mode to have multiple string mappings.
     * Throws exception if the stopping mode does not exists.
     *
     * @param name of the stopping mode
     * @return name of the stopping mode from the enum {@link MotorStopMode}
     */
    public IMotorStopMode getMotorStopMode(String mode);

    public List<IMotorStopMode> getMotorStopModes();

    public IMotorSide getMotorSide(String motorSide);

    public List<IMotorStopMode> getMotorSides();

    public IDriveDirection getDriveDirection(String driveDirection);

    public List<IDriveDirection> getDriveDirections();

    public IBrickKey getBrickKey(String brickKey);

    public List<IBrickKey> getBrickKeys();

    public IColorSensorMode getColorSensorMode(String colorSensorMode);

    public List<IColorSensorMode> getColorSensorModes();

    public IGyroSensorMode getGyroSensorMode(String gyroSensorMode);

    public List<IGyroSensorMode> getGyroSensorModes();

    public IInfraredSensorMode getInfraredSensorMode(String infraredSensorMode);

    public List<IInfraredSensorMode> getInfraredSensorModes();

    public ITimerSensorMode getTimerSensorMode(String timerSensroMode);

    public List<ITimerSensorMode> getTimerSensorModes();

    public IMotorTachoMode getMotorTachoMode(String motorTachoMode);

    public List<IMotorTachoMode> getMotorTachoModes();

    public IUltrasonicSensorMode getUltrasonicSensorMode(String ultrasonicSensorMode);

    public List<IUltrasonicSensorMode> getUltrasonicSensorModes();

    public ISensorPort getSensorPort(String port);

    public List<ISensorPort> getSensorPorts();

    public IIndexLocation getIndexLocation(String indexLocation);

    public List<IIndexLocation> getIndexLocations();

}
