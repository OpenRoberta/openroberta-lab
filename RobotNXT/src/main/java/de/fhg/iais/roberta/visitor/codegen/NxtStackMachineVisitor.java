package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.INxtVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class NxtStackMachineVisitor<V> extends AbstractStackMachineVisitor<V> implements INxtVisitor<V> {

    public NxtStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase<Void>>> phrases) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());
    }

    @Override
    public V visitColorConst(ColorConst<V> colorConst) {
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
                colorConst.addInfo(NepoInfo.error("SIM_BLOCK_NOT_SUPPORTED"));
                throw new DbcException("Invalid color constant: " + colorConst.getHexValueAsString());
        }
        JSONObject o = makeNode(C.EXPR).put(C.EXPR, C.COLOR_CONST).put(C.VALUE, color);
        return app(o);
    }

    @Override
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        JSONObject o = makeNode(C.CLEAR_DISPLAY_ACTION);
        return app(o);
    }

    @Override
    public V visitToneAction(ToneAction<V> toneAction) {
        toneAction.getFrequency().accept(this);
        toneAction.getDuration().accept(this);
        JSONObject o = makeNode(C.TONE_ACTION);
        return app(o);
    }

    @Override
    public V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        String freq = playNoteAction.getFrequency();
        String duration = playNoteAction.getDuration();
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, freq));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, duration));
        JSONObject o = makeNode(C.TONE_ACTION);
        return app(o);
    }

    @Override
    public V visitPlayFileAction(PlayFileAction<V> playFileAction) {
        String image = playFileAction.getFileName();
        JSONObject o = makeNode(C.PLAY_FILE_ACTION).put(C.FILE, image).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitVolumeAction(VolumeAction<V> volumeAction) {
        JSONObject o;
        if ( volumeAction.getMode() == VolumeAction.Mode.GET ) {
            o = makeNode(C.GET_VOLUME);
        } else {
            volumeAction.getVolume().accept(this);
            o = makeNode(C.SET_VOLUME_ACTION);
        }
        return app(o);
    }

    @Override
    public V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        String port = motorGetPowerAction.getUserDefinedPort();
        JSONObject o = makeNode(C.MOTOR_GET_POWER).put(C.PORT, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitDriveAction(DriveAction<V> driveAction) {
        driveAction.getParam().getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(driveAction.getParam().getDuration());
        ConfigurationComponent leftMotor = this.configuration.getFirstMotor(SC.LEFT);
        IDriveDirection leftMotorRotationDirection = DriveDirection.get(leftMotor.getProperty(SC.MOTOR_REVERSE));
        DriveDirection driveDirection = (DriveDirection) driveAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = getDriveDirection(driveAction.getDirection() == DriveDirection.FOREWARD);
        }
        JSONObject o =
            makeNode(C.DRIVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "ev3").put(C.SPEED_ONLY, speedOnly).put(C.SET_TIME, false);
        if ( speedOnly ) {
            return app(o);
        } else {
            app(o);
            return app(makeNode(C.STOP_DRIVE).put(C.NAME, "ev3"));
        }
    }

    @Override
    public V visitTurnAction(TurnAction<V> turnAction) {
        turnAction.getParam().getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(turnAction.getParam().getDuration());
        ConfigurationComponent leftMotor = this.configuration.getFirstMotor(SC.LEFT);
        IDriveDirection leftMotorRotationDirection = DriveDirection.get(leftMotor.getProperty(SC.MOTOR_REVERSE));
        ITurnDirection turnDirection = turnAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            turnDirection = getTurnDirection(turnAction.getDirection() == TurnDirection.LEFT);
        }
        JSONObject o =
            makeNode(C.TURN_ACTION)
                .put(C.TURN_DIRECTION, turnDirection.toString().toLowerCase())
                .put(C.NAME, "ev3")
                .put(C.SPEED_ONLY, speedOnly)
                .put(C.SET_TIME, false);
        if ( speedOnly ) {
            return app(o);
        } else {
            app(o);
            return app(makeNode(C.STOP_DRIVE).put(C.NAME, "ev3"));
        }
    }

    @Override
    public V visitCurveAction(CurveAction<V> curveAction) {
        curveAction.getParamLeft().getSpeed().accept(this);
        curveAction.getParamRight().getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(curveAction.getParamLeft().getDuration());
        ConfigurationComponent leftMotor = this.configuration.getFirstMotor(SC.LEFT);
        IDriveDirection leftMotorRotationDirection = DriveDirection.get(leftMotor.getProperty(SC.MOTOR_REVERSE));
        DriveDirection driveDirection = (DriveDirection) curveAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = getDriveDirection(curveAction.getDirection() == DriveDirection.FOREWARD);
        }
        JSONObject o =
            makeNode(C.CURVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "ev3").put(C.SPEED_ONLY, speedOnly).put(C.SET_TIME, false);
        if ( speedOnly ) {
            return app(o);
        } else {
            app(o);
            return app(makeNode(C.STOP_DRIVE).put(C.NAME, "ev3"));
        }
    }

    @Override
    public V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction) {
        JSONObject o = makeNode(C.STOP_DRIVE).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        motorOnAction.getParam().getSpeed().accept(this);
        MotorDuration<V> duration = motorOnAction.getParam().getDuration();
        boolean speedOnly = !processOptionalDuration(duration);
        String port = motorOnAction.getUserDefinedPort();
        JSONObject o = makeNode(C.MOTOR_ON_ACTION).put(C.PORT, port.toLowerCase()).put(C.NAME, port.toLowerCase()).put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return app(o);
        } else {
            String durationType = duration.getType().toString().toLowerCase();
            o.put(C.MOTOR_DURATION, durationType);
            app(o);
            return app(makeNode(C.MOTOR_STOP).put(C.PORT, port.toLowerCase()));
        }
    }

    @Override
    public V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        String port = motorSetPowerAction.getUserDefinedPort();
        motorSetPowerAction.getPower().accept(this);
        JSONObject o = makeNode(C.MOTOR_SET_POWER).put(C.PORT, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        JSONObject o = makeNode(C.MOTOR_STOP).put(C.PORT, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitShowTextAction(ShowTextAction<V> showTextAction) {
        showTextAction.y.accept(this);
        showTextAction.x.accept(this);
        showTextAction.msg.accept(this);
        JSONObject o = makeNode(C.SHOW_TEXT_ACTION).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitLightAction(LightAction<V> lightAction) {
        String mode = lightAction.getMode().toString().toLowerCase();
        String color = lightAction.getColor().toString().toLowerCase();
        String port = lightAction.getPort();
        JSONObject o = makeNode(C.LIGHT_ACTION).put(C.MODE, mode).put(C.PORT, port).put(C.COLOR, color);
        return app(o);
    }

    @Override
    public V visitTouchSensor(TouchSensor<V> touchSensor) {
        String port = touchSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TOUCH).put(C.PORT, port).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitColorSensor(ColorSensor<V> colorSensor) {
        String mode = colorSensor.getMode();
        String port = colorSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COLOR).put(C.PORT, port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitEncoderSensor(EncoderSensor<V> encoderSensor) {
        String mode = encoderSensor.getMode().toLowerCase();
        String port = encoderSensor.getUserDefinedPort().toLowerCase();
        JSONObject o;
        if ( mode.equals(C.RESET) ) {
            o = makeNode(C.ENCODER_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "ev3");
        } else {
            o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.ENCODER_SENSOR_SAMPLE).put(C.PORT, port).put(C.MODE, mode).put(C.NAME, "ev3");
        }
        return app(o);
    }

    @Override
    public V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor) {
        String mode = temperatureSensor.getMode();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TEMPERATURE).put(C.PORT, mode.toLowerCase()).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitKeysSensor(KeysSensor<V> keysSensor) {
        String mode = keysSensor.getUserDefinedPort().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.MODE, mode).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitLightSensor(LightSensor<V> lightSensor) {
        String mode = lightSensor.getMode().toLowerCase();
        String port = lightSensor.getUserDefinedPort().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COLOR).put(C.MODE, mode).put(C.PORT, port).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitTimerSensor(TimerSensor<V> timerSensor) {
        String port = timerSensor.getUserDefinedPort();
        JSONObject o;
        if ( timerSensor.getMode().equals(SC.DEFAULT) || timerSensor.getMode().equals(SC.VALUE) ) {
            o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port).put(C.NAME, "ev3");
        } else {
            o = makeNode(C.TIMER_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "ev3");
        }
        return app(o);
    }

    @Override
    public V visitPinTouchSensor(PinTouchSensor<V> sensorGetSample) {
        String port = sensorGetSample.getUserDefinedPort();
        String mode = sensorGetSample.getMode();

        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.PIN + port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitSoundSensor(SoundSensor<V> soundSensor) {
        String port = soundSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.SOUND).put(C.MODE, C.VOLUME).put(C.PORT, port).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitCompassSensor(CompassSensor<V> compassSensor) {
        String mode = compassSensor.getMode();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COMPASS).put(C.MODE, mode.toLowerCase()).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitGyroSensor(GyroSensor<V> gyroSensor) {
        String mode = gyroSensor.getMode().toLowerCase();
        String port = gyroSensor.getUserDefinedPort().toLowerCase();
        JSONObject o;
        if ( mode.equals(C.RESET) ) {
            o = makeNode(C.GYRO_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "ev3");
        } else {
            o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GYRO).put(C.MODE, mode).put(C.NAME, "ev3");
        }
        return app(o);
    }

    @Override
    public V visitHumiditySensor(HumiditySensor<V> humiditySensor) {
        return null;
    }

    @Override
    public V visitInfraredSensor(InfraredSensor<V> infraredSensor) {
        return null;
    }

    @Override
    public V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor) {
        String mode = ultrasonicSensor.getMode();
        String port = ultrasonicSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.ULTRASONIC).put(C.MODE, mode.toLowerCase()).put(C.PORT, port).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitBluetoothSendAction(BluetoothSendAction<V> bluetoothSendAction) {
        // Overrides default implementation so that server error is not produced
        return null;
    }

    @Override
    public V visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<V> bluetoothCheckConnectAction) {
        // Overrides default implementation so that server error is not produced
        return null;
    }

    @Override
    public V visitBluetoothReceiveAction(BluetoothReceiveAction<V> bluetoothReceiveAction) {
        // Overrides default implementation so that server error is not produced
        return null;
    }

    @Override
    public V visitConnectConst(ConnectConst<V> connectConst) {
        // Overrides default implementation so that server error is not produced
        return null;
    }

    @Override
    public V visitHTColorSensor(HTColorSensor<V> htColorSensor) {
        return null;
    }
}
