package de.fhg.iais.roberta.generic.factory;

import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.factory.IActorPort;
import de.fhg.iais.roberta.factory.IBlinkMode;
import de.fhg.iais.roberta.factory.IBrickLedColor;
import de.fhg.iais.roberta.factory.IDriveDirection;
import de.fhg.iais.roberta.factory.IMotorMoveMode;
import de.fhg.iais.roberta.factory.IMotorSide;
import de.fhg.iais.roberta.factory.IMotorStopMode;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.factory.IShowPicture;
import de.fhg.iais.roberta.factory.ITurnDirection;
import de.fhg.iais.roberta.generic.factory.action.ActorPort;
import de.fhg.iais.roberta.generic.factory.action.BlinkMode;
import de.fhg.iais.roberta.generic.factory.action.BrickLedColor;
import de.fhg.iais.roberta.generic.factory.action.DriveDirection;
import de.fhg.iais.roberta.generic.factory.action.MotorMoveMode;
import de.fhg.iais.roberta.generic.factory.action.MotorSide;
import de.fhg.iais.roberta.generic.factory.action.MotorStopMode;
import de.fhg.iais.roberta.generic.factory.action.ShowPicture;
import de.fhg.iais.roberta.generic.factory.action.TurnDirection;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class RobotModeFactory implements IRobotFactory {

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
}
