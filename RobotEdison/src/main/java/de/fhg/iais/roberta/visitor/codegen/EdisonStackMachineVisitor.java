package de.fhg.iais.roberta.visitor.codegen;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LedAction;
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
import de.fhg.iais.roberta.syntax.actors.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.visitor.IEdisonVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public final class EdisonStackMachineVisitor extends AbstractStackMachineVisitor implements IEdisonVisitor<Void> {
    public EdisonStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases, UsedHardwareBean usedHardwareBean, NNBean nnBean) {
        super(configuration, usedHardwareBean, nnBean);
        Assert.isTrue(!phrases.isEmpty());
    }

    @Override
    public final Void visitBinary(Binary binary) {
        switch ( binary.op ) {
            case DIVIDE:
                binary.left.accept(this);
                binary.getRight().accept(this);
                JSONObject o;
                o = makeNode(C.EXPR).put(C.EXPR, C.BINARY).put(C.OP, binary.op).put(C.TYPE, "INTEGER");
                return add(o);
            default:
                return super.visitBinary(binary);
        }

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
        JSONObject o = makeNode(C.CURVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.SPEED_ONLY, speedOnly).put(C.SET_TIME, false);
        if ( speedOnly ) {
            return add(o);
        } else {
            add(o);
            return add(makeNode(C.STOP_DRIVE));
        }
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        driveAction.param.getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(driveAction.param.getDuration());
        DriveDirection driveDirection = (DriveDirection) driveAction.direction;
        JSONObject o = makeNode(C.DRIVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.SPEED_ONLY, speedOnly).put(C.SET_TIME, false);
        if ( speedOnly ) {
            return add(o);
        } else {
            add(o);
            return add(makeNode(C.STOP_DRIVE));
        }
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String port = infraredSensor.getUserDefinedPort();
        String mode = infraredSensor.getMode().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.INFRARED).put(C.MODE, mode).put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        String mode = lightSensor.getMode().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.INFRARED).put(C.MODE, mode);
        return add(o);
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        String mode = keysSensor.getUserDefinedPort().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.MODE, mode);
        return add(o);
    }

    @Override
    public Void visitLedAction(LedAction ledAction) {
        String port = ledAction.port.toLowerCase();
        String mode = ledAction.mode;
        return add(makeNode(C.LED_ACTION).put(C.PORT, port).put(C.MODE, mode));
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
    public Void visitSoundSensor(SoundSensor soundSensor) {
        return add(makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.SOUND).put(C.MODE, C.VOLUME));
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
    public Void visitSendIRAction(SendIRAction sendIRAction) {
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        return null;
    }

    @Override
    public Void visitResetSensor(ResetSensor resetSensor) {
        return null;
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
        JSONObject o = makeNode(C.TURN_ACTION).put(C.TURN_DIRECTION, turnDirection.toString().toLowerCase()).put(C.SPEED_ONLY, speedOnly).put(C.SET_TIME, false);
        if ( speedOnly ) {
            return add(o);
        } else {
            add(o);
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
