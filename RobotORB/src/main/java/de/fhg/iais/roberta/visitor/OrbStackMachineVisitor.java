package de.fhg.iais.roberta.visitor;

import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class OrbStackMachineVisitor<V> extends AbstractStackMachineVisitor<V> implements IOrbVisitor<V> {

    public OrbStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase<Void>>> phrases, ILanguage language) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());

    }

    @Override
    public V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        //String port = motorGetPowerAction.getUserDefinedPort();
        ConfigurationComponent confMotorBlock = getConfigurationComponent(motorGetPowerAction.getUserDefinedPort());
        String port = confMotorBlock.getProperty("CONNECTOR");
        JSONObject o = makeNode(C.MOTOR_GET_POWER).put(C.PORT, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitDriveAction(DriveAction<V> driveAction) {
        driveAction.getParam().getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(driveAction.getParam().getDuration());
        ConfigurationComponent leftMotor = this.configuration.getFirstMotor(SC.LEFT);
        String brickName = "orb";
        //IDriveDirection leftMotorRotationDirection = DriveDirection.get(leftMotor.getProperty(SC.MOTOR_REVERSE));
        IDriveDirection leftMotorRotationDirection = DriveDirection.FOREWARD;
        DriveDirection driveDirection = (DriveDirection) driveAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = getDriveDirection(driveAction.getDirection() == DriveDirection.FOREWARD);
        }
        JSONObject o = makeNode(C.DRIVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, brickName).put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return app(o);
        } else {
            app(o);
            return app(makeNode(C.STOP_DRIVE).put(C.NAME, brickName));
        }
    }

    @Override
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        ConfigurationComponent confMotorBlock = getConfigurationComponent(motorOnAction.getUserDefinedPort());
        //String brickName = confMotorBlock.getProperty("VAR");
        String brickName = "orb";
        String port = confMotorBlock.getProperty("CONNECTOR");
        if ( brickName != null && port != null ) {
            motorOnAction.getParam().getSpeed().accept(this);
            MotorDuration<V> duration = motorOnAction.getParam().getDuration();
            boolean speedOnly = !processOptionalDuration(duration);
            JSONObject o = makeNode(C.MOTOR_ON_ACTION).put(C.NAME, brickName).put(C.PORT, port).put(C.SPEED_ONLY, speedOnly).put(C.SPEED_ONLY, speedOnly);
            if ( speedOnly ) {
                return app(o);
            } else {
                app(o);
                return app(makeNode(C.MOTOR_STOP).put(C.NAME, brickName).put(C.PORT, port));
            }
        } else {
            throw new DbcException("No robot name or no port");
        }
    }

    @Override
    public V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        //String port = motorSetPowerAction.getUserDefinedPort();
        ConfigurationComponent confMotorBlock = getConfigurationComponent(motorSetPowerAction.getUserDefinedPort());
        String port = confMotorBlock.getProperty("CONNECTOR");
        motorSetPowerAction.getPower().accept(this);
        JSONObject o = makeNode(C.MOTOR_SET_POWER).put(C.PORT, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        ConfigurationComponent confMotorBlock = getConfigurationComponent(motorStopAction.getUserDefinedPort());
        String brickName = confMotorBlock.getProperty("VAR");
        String port = confMotorBlock.getProperty("CONNECTOR");
        if ( brickName != null && port != null ) {
            JSONObject o = makeNode(C.MOTOR_STOP).put(C.NAME, brickName).put(C.PORT, port);
            return app(o);
        } else {
            throw new DbcException("No robot name or no port");
        }
    }

    @Override
    public V visitTurnAction(TurnAction<V> turnAction) {
        turnAction.getParam().getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(turnAction.getParam().getDuration());
        //ConfigurationComponent leftMotor = this.configuration.getFirstMotor(SC.LEFT);
        //IDriveDirection leftMotorRotationDirection = DriveDirection.get(leftMotor.getProperty(SC.MOTOR_REVERSE));
        ITurnDirection turnDirection = turnAction.getDirection();
        /*
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            turnDirection = getTurnDirection(turnAction.getDirection() == TurnDirection.LEFT);
        }*/
        JSONObject o = makeNode(C.TURN_ACTION).put(C.TURN_DIRECTION, turnDirection.toString().toLowerCase()).put(C.NAME, "orb").put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return app(o);
        } else {
            app(o);
            return app(makeNode(C.STOP_DRIVE).put(C.NAME, "orb"));
        }
    }

    @Override
    public V visitCurveAction(CurveAction<V> curveAction) {
        curveAction.getParamLeft().getSpeed().accept(this);
        curveAction.getParamRight().getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(curveAction.getParamLeft().getDuration());
        //ConfigurationComponent leftMotor = this.configuration.getFirstMotor(SC.LEFT);
        //IDriveDirection leftMotorRotationDirection = DriveDirection.get(leftMotor.getProperty(SC.MOTOR_REVERSE));
        DriveDirection driveDirection = (DriveDirection) curveAction.getDirection();
        /*
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = getDriveDirection(curveAction.getDirection() == DriveDirection.FOREWARD);
        }*/
        JSONObject o = makeNode(C.CURVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "orb").put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return app(o);
        } else {
            app(o);
            return app(makeNode(C.STOP_DRIVE).put(C.NAME, "orb"));
        }
    }

    @Override
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        JSONObject o = makeNode(C.CLEAR_DISPLAY_ACTION);
        return app(o);
    }

    @Override
    public V visitShowTextAction(ShowTextAction<V> showTextAction) {
        showTextAction.getMsg().accept(this);
        JSONObject o = makeNode(C.SHOW_TEXT_ACTION);
        return app(o);
    }

    /*
    @Override
    public V visitCompassSensor(CompassSensor<V> compassSensor) {
        // TODO check if this is really supported!
        String mode = compassSensor.getMode();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COMPASS).put(C.MODE, mode.toLowerCase()).put(C.NAME, "orb");
        return app(o);
    }*/

    @Override
    public V visitTouchSensor(TouchSensor<V> touchSensor) {
        ConfigurationComponent confSensor = getConfigurationComponent(touchSensor.getPort());
        String port = confSensor.getProperty("CONNECTOR");
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TOUCH).put(C.PORT, port).put(C.NAME, "orb");
        return app(o);
    }

    @Override
    public V visitColorSensor(ColorSensor<V> colorSensor) {
        ConfigurationComponent confSensor = getConfigurationComponent(colorSensor.getPort());
        String mode = colorSensor.getMode();
        String port = confSensor.getProperty("CONNECTOR");
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COLOR).put(C.PORT, port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "orb");
        return app(o);
    }

    @Override
    public V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor) {
        ConfigurationComponent confSensor = getConfigurationComponent(ultrasonicSensor.getPort());
        String mode = ultrasonicSensor.getMode();
        String port = confSensor.getProperty("CONNECTOR");
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.ULTRASONIC).put(C.PORT, port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "orb");//war ev3, hab geändert
        return app(o);
    }

    @Override
    public V visitGyroSensor(GyroSensor<V> gyroSensor) {
        ConfigurationComponent confGyroSensor = getConfigurationComponent(gyroSensor.getPort());
        String mode = gyroSensor.getMode();
        String port = confGyroSensor.getProperty("CONNECTOR");
        String slot = gyroSensor.getSlot().toString(); // the mode is in the slot?
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GYRO).put(C.PORT, port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "orb");//war ev3, hab geändert
        return app(o);
    }

    /*
    @Override
    public V visitGyroSensor(GyroSensor<V> gyroSensor) {
        ConfigurationComponent confGyroSensor = getConfigurationComponent(gyroSensor.getPort());
        String brickName = confGyroSensor.getProperty("VAR");
        String port = confGyroSensor.getProperty("CONNECTOR");
        String slot = gyroSensor.getSlot().toString(); // the mode is in the slot?
        if ( brickName != null && port != null ) {
            JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GYRO).put(C.NAME, brickName).put(C.PORT, port).put(C.MODE, slot);
            return app(o);
        } else {
            throw new DbcException("operation not supported");
        }
    }*/


    @Override
    public V visitTimerSensor(TimerSensor<V> timerSensor) {
        JSONObject o;
        switch ( timerSensor.getMode() ) {
            case "DEFAULT":
            case "VALUE":
                o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, timerSensor.getPort());
                break;
            case "RESET":
                o = makeNode(C.TIMER_SENSOR_RESET).put(C.PORT, timerSensor.getPort());
                break;
            default:
                throw new DbcException("Invalid Timer Mode " + timerSensor.getMode());
        }
        return app(o);
    }

    @Override
    public V visitInfraredSensor(InfraredSensor<V> infraredSensor) {
        ConfigurationComponent confSensor = getConfigurationComponent(infraredSensor.getPort());
        String mode = infraredSensor.getMode();
        String port = confSensor.getProperty("CONNECTOR");
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.INFRARED).put(C.PORT, port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "orb");//war ev3, hab geändert
        return app(o);
    }

    @Override
    public V visitGetSampleSensor(GetSampleSensor<V> sensorGetSample) {
        sensorGetSample.getSensor().accept(this);
        return null;
    }

    @Override
    public V visitAssertStmt(AssertStmt<V> assertStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V visitDebugAction(DebugAction<V> debugAction) {
        // TODO Auto-generated method stub
        return null;
    }
}
