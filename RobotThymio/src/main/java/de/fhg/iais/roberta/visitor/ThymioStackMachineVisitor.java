package de.fhg.iais.roberta.visitor;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LedsOffAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
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
import de.fhg.iais.roberta.syntax.action.thymio.LedButtonOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.LedCircleOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.LedProxHOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.LedProxVOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.LedSoundOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.LedTemperatureOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.PlayRecordingAction;
import de.fhg.iais.roberta.syntax.action.thymio.RecordStartAction;
import de.fhg.iais.roberta.syntax.action.thymio.RecordStopAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.thymio.TapSensor;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public final class ThymioStackMachineVisitor extends AbstractStackMachineVisitor implements IThymioVisitor<Void> {
    public ThymioStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        int r = colorConst.getRedChannelInt();
        int g = colorConst.getGreenChannelInt();
        int b = colorConst.getBlueChannelInt();

        JSONObject o = makeNode(C.EXPR).put(C.EXPR, "COLOR_CONST").put(C.VALUE, new JSONArray(Arrays.asList(r, g, b)));
        return add(o);
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        curveAction.paramLeft.getSpeed().accept(this);
        curveAction.paramRight.getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(curveAction.paramLeft.getDuration());
        DriveDirection driveDirection = (DriveDirection) curveAction.direction;

        JSONObject o = makeNode(C.CURVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return add(o.put(C.SET_TIME, false));
        } else {
            add(o.put(C.SET_TIME, true));
            return add(makeNode(C.STOP_DRIVE));
        }
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        driveAction.param.getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(driveAction.param.getDuration());
        DriveDirection driveDirection = (DriveDirection) driveAction.direction;
        JSONObject o = makeNode(C.DRIVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return add(o.put(C.SET_TIME, false));
        } else {
            add(o.put(C.SET_TIME, true));
            return add(makeNode(C.STOP_DRIVE));
        }
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String mode = infraredSensor.getMode().toLowerCase();
        String slot = infraredSensor.getSlot().toLowerCase();
        switch ( mode ) {
            case C.DISTANCE:
                break;
            case C.LINE:
            case C.LIGHT:
            case C.AMBIENTLIGHT:
                if ( slot.equals("0") ) {
                    slot = C.LEFT;
                } else if ( slot.equals("1") ) {
                    slot = C.RIGHT;
                }
                break;
            default:
                throw new DbcException("Invalid infrared sensor mode!");
        }
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.INFRARED).put(C.MODE, mode).put(C.SLOT, slot);
        return add(o);
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        String mode = keysSensor.getUserDefinedPort().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.MODE, mode);
        return add(o);
    }

    @Override
    public Void visitLedButtonOnAction(LedButtonOnAction ledButtonOnAction) {
        ledButtonOnAction.led1.accept(this);
        ledButtonOnAction.led2.accept(this);
        ledButtonOnAction.led3.accept(this);
        ledButtonOnAction.led4.accept(this);
        return add(makeNode(C.BUTTON_LED_ACTION));
    }

    @Override
    public Void visitLedCircleOnAction(LedCircleOnAction ledCircleOnAction) {
        ledCircleOnAction.led1.accept(this);
        ledCircleOnAction.led2.accept(this);
        ledCircleOnAction.led3.accept(this);
        ledCircleOnAction.led4.accept(this);
        ledCircleOnAction.led5.accept(this);
        ledCircleOnAction.led6.accept(this);
        ledCircleOnAction.led7.accept(this);
        ledCircleOnAction.led8.accept(this);
        return add(makeNode(C.CIRCLE_LED_ACTION));
    }

    @Override
    public Void visitLedProxHOnAction(LedProxHOnAction ledProxHOnAction) {
        ledProxHOnAction.led1.accept(this);
        ledProxHOnAction.led2.accept(this);
        ledProxHOnAction.led3.accept(this);
        ledProxHOnAction.led4.accept(this);
        ledProxHOnAction.led5.accept(this);
        ledProxHOnAction.led6.accept(this);
        ledProxHOnAction.led7.accept(this);
        ledProxHOnAction.led8.accept(this);
        return add(makeNode(C.PROXH_LED_ACTION));
    }

    @Override
    public Void visitLedProxVOnAction(LedProxVOnAction ledProxVOnAction) {
        return null;
    }

    @Override
    public Void visitLedSoundOnAction(LedSoundOnAction ledSoundOnAction) {
        ledSoundOnAction.led1.accept(this);
        return add(makeNode(C.SOUND_LED_ACTION));
    }

    @Override
    public Void visitLedTemperatureOnAction(LedTemperatureOnAction ledTemperatureOnAction) {
        ledTemperatureOnAction.led1.accept(this);
        ledTemperatureOnAction.led2.accept(this);
        return add(makeNode(C.TEMPERATURE_LED_ACTION));
    }

    @Override
    public Void visitLedsOffAction(LedsOffAction ledsOffAction) {
        String port = ledsOffAction.port.toLowerCase();
        JSONObject o = makeNode(C.STATUS_LIGHT_ACTION).put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        lightAction.rgbLedColor.accept(this);
        String port = lightAction.port.toLowerCase();
        JSONObject o = makeNode(C.LED_ON_ACTION).put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        JSONObject o = makeNode(C.STOP_DRIVE);
        return add(o);
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        motorOnAction.param.getSpeed().accept(this);
        MotorDuration<?> duration = motorOnAction.param.getDuration();
        boolean speedOnly = !processOptionalDuration(duration);
        String port = motorOnAction.getUserDefinedPort();

        JSONObject o = makeNode(C.MOTOR_ON_ACTION).put(C.PORT, port.toLowerCase()).put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return add(o.put(C.SET_TIME, false));
        } else {
            add(o.put(C.SET_TIME, true));
            if ( duration.getType() == null ) {
                o.put(C.MOTOR_DURATION, C.TIME);

            } else {
                String durationType = duration.getType().toString().toLowerCase();
                o.put(C.MOTOR_DURATION, durationType);
            }
            add(o);
            return add(makeNode(C.MOTOR_STOP).put(C.PORT, port.toLowerCase()));
        }
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        JSONObject o = makeNode(C.MOTOR_STOP).put(C.PORT, port.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        return null;
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
    public Void visitPlayRecordingAction(PlayRecordingAction playRecordingAction) {
        return null;
    }

    @Override
    public Void visitRecordStartAction(RecordStartAction recordStartAction) {
        return null;
    }

    @Override
    public Void visitRecordStopAction(RecordStopAction recordStopAction) {
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        return add(makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.SOUND).put(C.MODE, C.VOLUME));
    }

    @Override
    public Void visitTapSensor(TapSensor tapSensor) {
        return add(makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TOUCH));
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String port = timerSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        String port = timerReset.sensorPort;
        JSONObject o = makeNode(C.TIMER_SENSOR_RESET).put(C.PORT, port);
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
    public Void visitTurnAction(TurnAction turnAction) {
        turnAction.param.getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(turnAction.param.getDuration());
        ITurnDirection turnDirection = turnAction.direction;
        JSONObject o =
            makeNode(C.TURN_ACTION).put(C.TURN_DIRECTION, turnDirection.toString().toLowerCase()).put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return add(o.put(C.SET_TIME, false));
        } else {
            add(o.put(C.SET_TIME, true));
            return add(makeNode(C.STOP_DRIVE));
        }
    }

    @Override
    public Void visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        throw new DbcException("Not supported!");
    }
}
