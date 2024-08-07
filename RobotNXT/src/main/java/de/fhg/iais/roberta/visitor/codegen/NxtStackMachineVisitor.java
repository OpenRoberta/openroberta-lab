package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.NxtRgbLedOnAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.INxtVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class NxtStackMachineVisitor extends AbstractStackMachineVisitor implements INxtVisitor<Void> {

    public NxtStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases, UsedHardwareBean usedHardwareBean, NNBean nnBean) {
        super(configuration, usedHardwareBean, nnBean);
        Assert.isTrue(!phrases.isEmpty());
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        String color;
        switch ( colorConst.getHexValueAsString().toUpperCase() ) {
            case "#000000":
                color = "BLACK";
                break;
            case "#0057A6":
                color = "BLUE";
                break;
            case "#00642E":
                color = "GREEN";
                break;
            case "#F7D117":
                color = "YELLOW";
                break;
            case "#B30006":
                color = "RED";
                break;
            case "#FFFFFF":
                color = "WHITE";
                break;
            case "#532115":
                color = "BROWN";
                break;
            case "#EE82EE":
                color = "VIOLET";
                break;
            case "#800080":
                color = "PURPLE";
                break;
            case "#00FF00":
                color = "LIME";
                break;
            case "#FFA500":
                color = "ORANGE";
                break;
            case "#FF00FF":
                color = "MAGENTA";
                break;
            case "#DC143C":
                color = "CRIMSON";
            case "#585858":
                color = "NONE";
                break;
            default:
                throw new DbcException("Invalid color constant: " + colorConst.getHexValueAsString());
        }
        JSONObject o = makeNode(C.EXPR).put(C.EXPR, C.COLOR_CONST).put(C.VALUE, color);
        return add(o);
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        JSONObject o = makeNode(C.CLEAR_DISPLAY_ACTION);
        return add(o);
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        toneAction.frequency.accept(this);
        toneAction.duration.accept(this);
        JSONObject o = makeNode(C.TONE_ACTION);
        return add(o);
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        String freq = playNoteAction.frequency;
        String duration = playNoteAction.duration;
        add(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, freq));
        add(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, duration));
        JSONObject o = makeNode(C.TONE_ACTION);
        return add(o);
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        String image = playFileAction.fileName;
        JSONObject o = makeNode(C.PLAY_FILE_ACTION).put(C.FILE, image).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        JSONObject o;
        o = makeNode(C.GET_VOLUME);
        return add(o);
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        JSONObject o;
        setVolumeAction.volume.accept(this);
        o = makeNode(C.SET_VOLUME_ACTION);
        return add(o);
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        String port = motorGetPowerAction.getUserDefinedPort();
        JSONObject o = makeNode(C.MOTOR_GET_POWER).put(C.PORT, port.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        driveAction.param.getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(driveAction.param.getDuration());
        ConfigurationComponent leftMotor = this.configuration.getFirstMotor(SC.LEFT);
        IDriveDirection leftMotorRotationDirection = DriveDirection.get(leftMotor.getProperty(SC.MOTOR_REVERSE));
        DriveDirection driveDirection = (DriveDirection) driveAction.direction;
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = getDriveDirection(driveAction.direction == DriveDirection.FOREWARD);
        }
        JSONObject o =
            makeNode(C.DRIVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "ev3").put(C.SPEED_ONLY, speedOnly).put(C.SET_TIME, false);
        if ( speedOnly ) {
            return add(o);
        } else {
            add(o);
            return add(makeNode(C.STOP_DRIVE).put(C.NAME, "ev3"));
        }
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        turnAction.param.getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(turnAction.param.getDuration());
        ConfigurationComponent leftMotor = this.configuration.getFirstMotor(SC.LEFT);
        IDriveDirection leftMotorRotationDirection = DriveDirection.get(leftMotor.getProperty(SC.MOTOR_REVERSE));
        ITurnDirection turnDirection = turnAction.direction;
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            turnDirection = getTurnDirection(turnAction.direction == TurnDirection.LEFT);
        }
        JSONObject o =
            makeNode(C.TURN_ACTION)
                .put(C.TURN_DIRECTION, turnDirection.toString().toLowerCase())
                .put(C.NAME, "ev3")
                .put(C.SPEED_ONLY, speedOnly)
                .put(C.SET_TIME, false);
        if ( speedOnly ) {
            return add(o);
        } else {
            add(o);
            return add(makeNode(C.STOP_DRIVE).put(C.NAME, "ev3"));
        }
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        curveAction.paramLeft.getSpeed().accept(this);
        curveAction.paramRight.getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(curveAction.paramLeft.getDuration());
        ConfigurationComponent leftMotor = this.configuration.getFirstMotor(SC.LEFT);
        IDriveDirection leftMotorRotationDirection = DriveDirection.get(leftMotor.getProperty(SC.MOTOR_REVERSE));
        DriveDirection driveDirection = (DriveDirection) curveAction.direction;
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = getDriveDirection(curveAction.direction == DriveDirection.FOREWARD);
        }
        JSONObject o =
            makeNode(C.CURVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "ev3").put(C.SPEED_ONLY, speedOnly).put(C.SET_TIME, false);
        if ( speedOnly ) {
            return add(o);
        } else {
            add(o);
            return add(makeNode(C.STOP_DRIVE).put(C.NAME, "ev3"));
        }
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        JSONObject o = makeNode(C.STOP_DRIVE).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        motorOnAction.param.getSpeed().accept(this);
        MotorDuration duration = motorOnAction.param.getDuration();
        boolean speedOnly = !processOptionalDuration(duration);
        String port = motorOnAction.getUserDefinedPort();
        JSONObject o = makeNode(C.MOTOR_ON_ACTION).put(C.PORT, port.toLowerCase()).put(C.NAME, port.toLowerCase()).put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return add(o);
        } else {
            String durationType = duration.getType().toString().toLowerCase();
            o.put(C.MOTOR_DURATION, durationType);
            add(o);
            return add(makeNode(C.MOTOR_STOP).put(C.PORT, port.toLowerCase()));
        }
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        String port = motorSetPowerAction.getUserDefinedPort();
        motorSetPowerAction.power.accept(this);
        JSONObject o = makeNode(C.MOTOR_SET_POWER).put(C.PORT, port.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        JSONObject o = makeNode(C.MOTOR_STOP).put(C.PORT, port.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        showTextAction.y.accept(this);
        showTextAction.x.accept(this);
        showTextAction.msg.accept(this);
        JSONObject o = makeNode(C.SHOW_TEXT_ACTION).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitNxtRgbLedOnAction(NxtRgbLedOnAction nxtRgbLedOnAction) {
        String color = nxtRgbLedOnAction.colour.toString().toLowerCase();
        String port = nxtRgbLedOnAction.port;
        JSONObject o = makeNode(C.LIGHT_ACTION).put(C.MODE, "on").put(C.PORT, port).put(C.COLOR, color);
        return add(o);
    }

    @Override
    public Void visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction) {
        String port = rgbLedOffAction.port;
        JSONObject o = makeNode(C.LIGHT_ACTION).put(C.MODE, "off").put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        String port = touchSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TOUCH).put(C.PORT, port).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        String mode = colorSensor.getMode();
        String port = colorSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COLOR).put(C.PORT, port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        String mode = encoderSensor.getMode().toLowerCase();
        String port = encoderSensor.getUserDefinedPort().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.ENCODER_SENSOR_SAMPLE).put(C.PORT, port).put(C.MODE, mode).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        String port = encoderReset.sensorPort.toLowerCase();
        JSONObject o = makeNode(C.ENCODER_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        String mode = keysSensor.getUserDefinedPort().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.MODE, mode).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        String mode = lightSensor.getMode().toLowerCase();
        String port = lightSensor.getUserDefinedPort().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COLOR).put(C.MODE, mode).put(C.PORT, port).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String port = timerSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        String port = timerReset.sensorPort;
        JSONObject o = makeNode(C.TIMER_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        String port = soundSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.SOUND).put(C.MODE, C.VOLUME).put(C.PORT, port).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String mode = ultrasonicSensor.getMode();
        String port = ultrasonicSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.ULTRASONIC).put(C.MODE, mode.toLowerCase()).put(C.PORT, port).put(C.NAME, "ev3");
        return add(o);
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction) {
        // Overrides default implementation so that server error is not produced
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction) {
        // Overrides default implementation so that server error is not produced
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReceiveAction) {
        // Overrides default implementation so that server error is not produced
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst connectConst) {
        // Overrides default implementation so that server error is not produced
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor htColorSensor) {
        return null;
    }
}
