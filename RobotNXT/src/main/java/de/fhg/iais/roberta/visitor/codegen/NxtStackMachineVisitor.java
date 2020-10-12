package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
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
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
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
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.hardware.INxtVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class NxtStackMachineVisitor<V> extends AbstractStackMachineVisitor<V> implements INxtVisitor<V> {

    public NxtStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase<Void>>> phrases) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());
    }

    @Override
    public V visitColorConst(ColorConst<V> colorConst) {
        String color = "";
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
        JSONObject o = mk(C.EXPR, colorConst).put(C.EXPR, C.COLOR_CONST).put(C.VALUE, color);
        return app(o);
    }

    @Override
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        JSONObject o = mk(C.CLEAR_DISPLAY_ACTION, clearDisplayAction);

        return app(o);
    }

    @Override
    public V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        JSONObject o = mk(C.STATUS_LIGHT_ACTION, lightStatusAction).put(C.NAME, "ev3").put(C.PORT, "internal");
        return app(o);
    }

    @Override
    public V visitToneAction(ToneAction<V> toneAction) {
        toneAction.getFrequency().accept(this);
        toneAction.getDuration().accept(this);
        JSONObject o = mk(C.TONE_ACTION, toneAction);
        return app(o);
    }

    @Override
    public V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        String freq = playNoteAction.getFrequency();
        String duration = playNoteAction.getDuration();
        app(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, freq));
        app(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, duration));
        JSONObject o = mk(C.TONE_ACTION, playNoteAction);
        return app(o);
    }

    @Override
    public V visitPlayFileAction(PlayFileAction<V> playFileAction) {
        String image = playFileAction.getFileName().toString();
        JSONObject o = mk(C.PLAY_FILE_ACTION, playFileAction).put(C.FILE, image).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitVolumeAction(VolumeAction<V> volumeAction) {
        JSONObject o;
        if ( volumeAction.getMode() == VolumeAction.Mode.GET ) {
            o = mk(C.GET_VOLUME, volumeAction);
        } else {
            volumeAction.getVolume().accept(this);
            o = mk(C.SET_VOLUME_ACTION, volumeAction);
        }
        return app(o);
    }

    @Override
    public V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        String port = motorGetPowerAction.getUserDefinedPort();
        JSONObject o = mk(C.MOTOR_GET_POWER, motorGetPowerAction).put(C.PORT, port.toLowerCase());
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
        JSONObject o = mk(C.DRIVE_ACTION, driveAction).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "ev3").put(C.SPEED_ONLY, speedOnly).put(C.SET_TIME, false);
        if ( speedOnly ) {
            return app(o);
        } else {
            app(o);
            return app(mk(C.STOP_DRIVE, driveAction).put(C.NAME, "ev3"));
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
            mk(C.TURN_ACTION, turnAction).put(C.TURN_DIRECTION, turnDirection.toString().toLowerCase()).put(C.NAME, "ev3").put(C.SPEED_ONLY, speedOnly).put(C.SET_TIME, false);
        if ( speedOnly ) {
            return app(o);
        } else {
            app(o);
            return app(mk(C.STOP_DRIVE, turnAction).put(C.NAME, "ev3"));
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
        JSONObject o = mk(C.CURVE_ACTION, curveAction).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "ev3").put(C.SPEED_ONLY, speedOnly).put(C.SET_TIME, false);
        if ( speedOnly ) {
            return app(o);
        } else {
            app(o);
            return app(mk(C.STOP_DRIVE, curveAction).put(C.NAME, "ev3"));
        }
    }

    @Override
    public V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction) {
        JSONObject o = mk(C.STOP_DRIVE, stopAction).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        motorOnAction.getParam().getSpeed().accept(this);
        MotorDuration<V> duration = motorOnAction.getParam().getDuration();
        boolean speedOnly = !processOptionalDuration(duration);
        String port = motorOnAction.getUserDefinedPort();
        JSONObject o = mk(C.MOTOR_ON_ACTION, motorOnAction).put(C.PORT, port.toLowerCase()).put(C.NAME, port.toLowerCase()).put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return app(o);
        } else {
            String durationType = duration.getType().toString().toLowerCase();
            o.put(C.MOTOR_DURATION, durationType);
            app(o);
            return app(mk(C.MOTOR_STOP, motorOnAction).put(C.PORT, port.toLowerCase()));
        }
    }

    @Override
    public V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        String port = motorSetPowerAction.getUserDefinedPort();
        motorSetPowerAction.getPower().accept(this);
        JSONObject o = mk(C.MOTOR_SET_POWER, motorSetPowerAction).put(C.PORT, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        JSONObject o = mk(C.MOTOR_STOP, motorStopAction).put(C.PORT, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitShowTextAction(ShowTextAction<V> showTextAction) {
        showTextAction.getY().accept(this);
        showTextAction.getX().accept(this);
        showTextAction.getMsg().accept(this);
        JSONObject o = mk(C.SHOW_TEXT_ACTION, showTextAction).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitLightAction(LightAction<V> lightAction) {
        String mode = lightAction.getMode().toString().toLowerCase();
        String color = lightAction.getColor().toString().toLowerCase();
        String port = lightAction.getPort();
        JSONObject o = mk(C.LIGHT_ACTION, lightAction).put(C.MODE, mode).put(C.PORT, port).put(C.COLOR, color);
        return app(o);
    }

    @Override
    public V visitTouchSensor(TouchSensor<V> touchSensor) {
        String port = touchSensor.getPort();
        JSONObject o = mk(C.GET_SAMPLE, touchSensor).put(C.GET_SAMPLE, C.TOUCH).put(C.PORT, port).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitColorSensor(ColorSensor<V> colorSensor) {
        String mode = colorSensor.getMode();
        String port = colorSensor.getPort();
        JSONObject o = mk(C.GET_SAMPLE, colorSensor).put(C.GET_SAMPLE, C.COLOR).put(C.PORT, port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitEncoderSensor(EncoderSensor<V> encoderSensor) {
        String mode = encoderSensor.getMode().toLowerCase();
        String port = encoderSensor.getPort().toLowerCase();
        JSONObject o;
        if ( mode.equals(C.RESET) ) {
            o = mk(C.ENCODER_SENSOR_RESET, encoderSensor).put(C.PORT, port).put(C.NAME, "ev3");
        } else {
            o = mk(C.GET_SAMPLE, encoderSensor).put(C.GET_SAMPLE, C.ENCODER_SENSOR_SAMPLE).put(C.PORT, port).put(C.MODE, mode).put(C.NAME, "ev3");
        }
        return app(o);
    }

    @Override
    public V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor) {
        String mode = temperatureSensor.getMode();
        JSONObject o = mk(C.GET_SAMPLE, temperatureSensor).put(C.GET_SAMPLE, C.TEMPERATURE).put(C.PORT, mode.toLowerCase()).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitKeysSensor(KeysSensor<V> keysSensor) {
        String mode = keysSensor.getPort().toLowerCase();
        JSONObject o = mk(C.GET_SAMPLE, keysSensor).put(C.GET_SAMPLE, C.BUTTONS).put(C.MODE, mode).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitLightSensor(LightSensor<V> lightSensor) {
        String mode = lightSensor.getMode().toLowerCase();
        String port = lightSensor.getPort().toLowerCase();
        JSONObject o = mk(C.GET_SAMPLE, lightSensor).put(C.GET_SAMPLE, C.COLOR).put(C.MODE, mode).put(C.PORT, port).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitTimerSensor(TimerSensor<V> timerSensor) {
        String port = timerSensor.getPort();
        JSONObject o;
        if ( timerSensor.getMode().equals(SC.DEFAULT) || timerSensor.getMode().equals(SC.VALUE) ) {
            o = mk(C.GET_SAMPLE, timerSensor).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port).put(C.NAME, "ev3");
        } else {
            o = mk(C.TIMER_SENSOR_RESET, timerSensor).put(C.PORT, port).put(C.NAME, "ev3");
        }
        return app(o);
    }

    @Override
    public V visitPinTouchSensor(PinTouchSensor<V> sensorGetSample) {
        String port = sensorGetSample.getPort();
        String mode = sensorGetSample.getMode();

        JSONObject o = mk(C.GET_SAMPLE, sensorGetSample).put(C.GET_SAMPLE, C.PIN + port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitSoundSensor(SoundSensor<V> soundSensor) {
        String port = soundSensor.getPort();
        JSONObject o = mk(C.GET_SAMPLE, soundSensor).put(C.GET_SAMPLE, C.SOUND).put(C.MODE, C.VOLUME).put(C.PORT, port).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitCompassSensor(CompassSensor<V> compassSensor) {
        String mode = compassSensor.getMode();
        JSONObject o = mk(C.GET_SAMPLE, compassSensor).put(C.GET_SAMPLE, C.COMPASS).put(C.MODE, mode.toLowerCase()).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitGyroSensor(GyroSensor<V> gyroSensor) {
        String mode = gyroSensor.getMode().toLowerCase();
        String port = gyroSensor.getPort().toLowerCase();
        JSONObject o;
        if ( mode.equals(C.RESET) ) {
            o = mk(C.GYRO_SENSOR_RESET, gyroSensor).put(C.PORT, port).put(C.NAME, "ev3");
        } else {
            o = mk(C.GET_SAMPLE, gyroSensor).put(C.GET_SAMPLE, C.GYRO).put(C.MODE, mode).put(C.NAME, "ev3");
        }
        return app(o);
    }

    @Override
    public V visitAccelerometer(AccelerometerSensor<V> accelerometerSensor) {
        return null;
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
        String port = ultrasonicSensor.getPort();
        JSONObject o = mk(C.GET_SAMPLE, ultrasonicSensor).put(C.GET_SAMPLE, C.ULTRASONIC).put(C.MODE, mode.toLowerCase()).put(C.PORT, port).put(C.NAME, "ev3");
        return app(o);
    }

    private String getLanguageString(ILanguage language) {
        switch ( (Language) language ) {
            case GERMAN:
                return "de-DE";
            case ENGLISH:
                return "en-US";
            case FRENCH:
                return "fr-FR";
            case SPANISH:
                return "es-ES";
            case ITALIAN:
                return "it-IT";
            case DUTCH:
                return "nl-NL";
            case POLISH:
                return "pl-PL";
            case RUSSIAN:
                return "ru-RU";
            case PORTUGUESE:
                return "pt-BR";
            case JAPANESE:
                return "ja-JP";
            case CHINESE:
                return "zh-CN";
            default:
                return "";
        }
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
