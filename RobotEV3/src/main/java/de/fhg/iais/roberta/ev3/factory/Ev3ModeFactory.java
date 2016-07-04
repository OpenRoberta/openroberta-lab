package de.fhg.iais.roberta.ev3.factory;

import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.ev3.factory.action.ActorPort;
import de.fhg.iais.roberta.ev3.factory.action.BlinkMode;
import de.fhg.iais.roberta.ev3.factory.action.BrickLedColor;
import de.fhg.iais.roberta.ev3.factory.action.DriveDirection;
import de.fhg.iais.roberta.ev3.factory.action.MotorMoveMode;
import de.fhg.iais.roberta.ev3.factory.action.MotorStopMode;
import de.fhg.iais.roberta.ev3.factory.action.ShowPicture;
import de.fhg.iais.roberta.ev3.factory.action.TurnDirection;
import de.fhg.iais.roberta.ev3.factory.sensor.BrickKey;
import de.fhg.iais.roberta.ev3.factory.sensor.ColorSensorMode;
import de.fhg.iais.roberta.ev3.factory.sensor.GyroSensorMode;
import de.fhg.iais.roberta.ev3.factory.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.ev3.factory.sensor.MotorTachoMode;
import de.fhg.iais.roberta.ev3.factory.sensor.SensorPort;
import de.fhg.iais.roberta.ev3.factory.sensor.TimerSensorMode;
import de.fhg.iais.roberta.ev3.factory.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.factory.IIndexLocation;
import de.fhg.iais.roberta.factory.IRobotFactory;
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
import de.fhg.iais.roberta.generic.factory.IndexLocation;
import de.fhg.iais.roberta.generic.factory.action.MotorSide;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class Ev3ModeFactory implements IRobotFactory {

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
    public ITurnDirection getTurnDirection(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Turn Direction: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( TurnDirection dir : TurnDirection.values() ) {
            if ( dir.toString().equals(sUpper) ) {
                return dir;
            }
            for ( String value : dir.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return dir;
                }
            }
        }
        throw new DbcException("Invalid Turn Direction: " + direction);

    }

    @Override
    public List<ITurnDirection> getTurnDirections() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMotorMoveMode getMotorMoveMode(String mode) {
        if ( mode == null || mode.isEmpty() ) {
            throw new DbcException("Invalid Motor Move Mode: " + mode);
        }
        String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( MotorMoveMode mo : MotorMoveMode.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid Motor Move Mode: " + mode);
    }

    @Override
    public List<IMotorMoveMode> getMotorMoveModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMotorStopMode getMotorStopMode(String mode) {
        if ( mode == null || mode.isEmpty() ) {
            throw new DbcException("Invalid Motor Stop Mode: " + mode);
        }
        String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( MotorStopMode mo : MotorStopMode.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid Stop Move Mode: " + mode);
    }

    @Override
    public List<IMotorStopMode> getMotorStopModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMotorSide getMotorSide(String motorSide) {
        if ( motorSide == null || motorSide.isEmpty() ) {
            throw new DbcException("Invalid Motor Side: " + motorSide);
        }
        String sUpper = motorSide.trim().toUpperCase(Locale.GERMAN);
        for ( MotorSide mo : MotorSide.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid Motor Side: " + motorSide);
    }

    @Override
    public List<IMotorStopMode> getMotorSides() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IDriveDirection getDriveDirection(String driveDirection) {
        if ( driveDirection == null || driveDirection.isEmpty() ) {
            throw new DbcException("Invalid Drive Direction: " + driveDirection);
        }
        String sUpper = driveDirection.trim().toUpperCase(Locale.GERMAN);
        for ( DriveDirection sp : DriveDirection.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid Drive Direction: " + driveDirection);
    }

    @Override
    public List<IDriveDirection> getDriveDirections() {
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
                if ( sUpper.equals(value) ) {
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
                if ( sUpper.equals(value) ) {
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
    public IIndexLocation getIndexLocation(String indexLocation) {
        if ( indexLocation == null || indexLocation.isEmpty() ) {
            throw new DbcException("Invalid Index Location: " + indexLocation);
        }
        String sUpper = indexLocation.trim().toUpperCase(Locale.GERMAN);
        for ( IndexLocation po : IndexLocation.values() ) {
            if ( po.toString().equals(sUpper) ) {
                return po;
            }
            for ( String value : po.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return po;
                }
            }
        }
        throw new DbcException("Invalid Index Location: " + indexLocation);
    }

    @Override
    public List<IIndexLocation> getIndexLocations() {
        // TODO Auto-generated method stub
        return null;
    }

}
