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
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
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
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
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
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class Ev3StackMachineVisitor<V> extends AbstractStackMachineVisitor<V> implements IEv3Visitor<V> {

    public Ev3StackMachineVisitor(ConfigurationAst configuration, List<List<Phrase<Void>>> phrases, ILanguage language) {
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
    public V visitShowPictureAction(ShowPictureAction<V> showPictureAction) {
        String image = showPictureAction.getPicture().toString();
        JSONObject o = makeNode(C.SHOW_IMAGE_ACTION).put(C.IMAGE, image).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        JSONObject o = makeNode(C.CLEAR_DISPLAY_ACTION);

        return app(o);
    }

    @Override
    public V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        JSONObject o = makeNode(C.STATUS_LIGHT_ACTION).put(C.NAME, "ev3").put(C.PORT, "internal");
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
        String image = playFileAction.getFileName().toString();
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
    public V visitSetLanguageAction(SetLanguageAction<V> setLanguageAction) {
        String language = getLanguageString(setLanguageAction.getLanguage());
        JSONObject o = makeNode(C.SET_LANGUAGE_ACTION).put(C.LANGUAGE, language);
        return app(o);
    }

    @Override
    public V visitSayTextAction(SayTextAction<V> sayTextAction) {
        sayTextAction.getMsg().accept(this);
        BlockType emptyBlock = BlockTypeContainer.getByName("EMPTY_EXPR");
        if ( sayTextAction.getSpeed().getKind().equals(emptyBlock) ) {
            NumConst<V> n = NumConst.make("30");
            n.accept(this);
        } else {
            sayTextAction.getSpeed().accept(this);
        }
        if ( sayTextAction.getPitch().getKind().equals(emptyBlock) ) {
            NumConst<V> n = NumConst.make("50");
            n.accept(this);
        } else {
            sayTextAction.getPitch().accept(this);
        }
        JSONObject o = makeNode(C.SAY_TEXT_ACTION);

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
        JSONObject o = makeNode(C.LIGHT_ACTION).put(C.MODE, mode).put(C.COLOR, color);
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
    public V visitHTColorSensor(HTColorSensor<V> htColorSensor) {
        return null;
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
    public V visitKeysSensor(KeysSensor<V> keysSensor) {
        String mode = keysSensor.getUserDefinedPort().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.MODE, mode).put(C.NAME, "ev3");
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
    public V visitSoundSensor(SoundSensor<V> soundSensor) {
        // TODO check if this is really supported!
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.SOUND).put(C.MODE, C.VOLUME).put(C.NAME, "ev3");
        return app(o);
    }

    @Override
    public V visitCompassSensor(CompassSensor<V> compassSensor) {
        // TODO check if this is really supported!
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
            o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GYRO).put(C.MODE, mode).put(C.PORT, port).put(C.NAME, "ev3");
        }
        return app(o);
    }

    @Override
    public V visitIRSeekerSensor(IRSeekerSensor<V> irSeekerSensor) {
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
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.ULTRASONIC).put(C.PORT, port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "ev3");
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
    public V visitBluetoothReceiveAction(BluetoothReceiveAction<V> bluetoothReceiveAction) {
        return null;
    }

    @Override
    public V visitBluetoothConnectAction(BluetoothConnectAction<V> bluetoothConnectAction) {
        return null;
    }

    @Override
    public V visitBluetoothSendAction(BluetoothSendAction<V> bluetoothSendAction) {
        return null;
    }

    @Override
    public V visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<V> bluetoothWaitForConnection) {
        return null;
    }

    @Override
    public V visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<V> bluetoothCheckConnectAction) {
        return null;
    }
}
