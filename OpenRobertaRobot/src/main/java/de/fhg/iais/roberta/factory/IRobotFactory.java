package de.fhg.iais.roberta.factory;

import java.util.List;

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

}
