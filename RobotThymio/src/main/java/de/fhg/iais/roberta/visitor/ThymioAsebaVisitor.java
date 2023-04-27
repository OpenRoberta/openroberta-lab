package de.fhg.iais.roberta.visitor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
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
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.thymio.TapSensor;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.util.syntax.SC;

public final class ThymioAsebaVisitor extends AbstractAsebaVisitor implements IThymioVisitor<Void> {

    public ThymioAsebaVisitor(
        List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans, ConfigurationAst configurationAst) {
        super(programPhrases, beans);
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        this.sb.append("_A = acc[").append(accelerometerSensor.getSlot()).append("]");
        return null;
    }


    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.sb.append("___color_ = [").append(colorConst.getRedChannelInt()).append(", ").append(colorConst.getGreenChannelInt()).append(", ").append(colorConst.getBlueChannelInt()).append("]");
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList emptyList) {
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        String multiplier = curveAction.direction.toString().equals(SC.FOREWARD) ? "" : "-";
        move(curveAction.paramRight.getDuration(), curveAction.paramLeft.getSpeed(), curveAction.paramRight.getSpeed(), multiplier, multiplier);
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        String multiplier = driveAction.direction.toString().equals(SC.FOREWARD) ? "" : "-";
        move(driveAction.param.getDuration(), driveAction.param.getSpeed(), multiplier, multiplier);
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String mode = infraredSensor.getMode().toLowerCase();
        this.sb.append("_A = ");
        switch ( mode ) {
            case C.DISTANCE:
                this.sb.append("(100 - prox.").append("horizontal").append("[").append(infraredSensor.getSlot()).append("] / 45)");
                break;
            case C.LINE:
                this.sb.append("(1500 - prox.").append("ground.reflected").append("[").append(infraredSensor.getSlot()).append("]) / 1000");
                break;
            case C.LIGHT:
                this.sb.append("prox.").append("ground.reflected").append("[").append(infraredSensor.getSlot()).append("] / 45");
                break;
            case C.AMBIENTLIGHT:
                this.sb.append("prox.").append("ground.ambiant").append("[").append(infraredSensor.getSlot()).append("] / 2");
                break;
            default:
                throw new DbcException("Invalid infrared sensor mode!");
        }
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        this.sb.append("_A = button.").append(keysSensor.getUserDefinedPort().toLowerCase());
        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        lightAction.rgbLedColor.accept(this);
        nlIndent();
        this.sb.append("call leds.").append(lightAction.port.toLowerCase()).append("(___color_[0] / _RGB_DIV, ___color_[1] / _RGB_DIV, ___color_[2] / _RGB_DIV)");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        StmtList variables = mainTask.variables;
        variables.accept(this);
        if ( variables.sl.size() > 0 ) {
            nlIndent();
            nlIndent();
        }
        this.sb.append("timer.period[0] = 10 # ms time between state executions");
        nlIndent();
        nlIndent();
        this.sb.append("onevent timer0");
        incrIndentation();
        nlIndent();
        if ( !(this
            .getBean(UsedHardwareBean.class)
            .getUsedSensors()
            .stream().noneMatch(usedSensor -> usedSensor.getType().equals(SC.TIMER))) ) {
            this.sb.append("_timer += 10");
            nlIndent();
        }
        this.getBean(UsedHardwareBean.class).getUserDefinedMethods().forEach(method -> {
            method.accept(this);
            nlIndent();
        });
        int first = this.funcStart.size() > 0 ? this.funcStart.size() - 1 + 1 : 0;
        this.sb.append(this.getIfElse()).append(" _state == ").append(first).append(" then");
        incrIndentation();
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        this.sb.append("callsub diffdrive_stop");
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        String motorSide = motorOnAction.port.toLowerCase();
        if ( motorOnAction.getDurationValue() != null ) {
            this.sb.append("_time = 0");
            nlIndent();
            motorOnAction.param.getSpeed().accept(this);
            nlIndent();
            this.sb.append("motor.").append(motorSide).append(".target = _M_MAX * _A");
            nlIndent();
            motorOnAction.getDurationValue().accept(this);
            nlIndent();
            this.sb.append("___duration_ = _A / timer.period[0]");
            nlIndent();
            this.stateCounter++;
            this.sb.append("_state = ").append(this.stateCounter);
            decrIndentation();
            nlIndent();
            this.sb.append("elseif _state == ").append(this.stateCounter).append(" then");
            incrIndentation();
            nlIndent();
            this.sb.append("if _time == ___duration_ then");
            incrIndentation();
            nlIndent();
            this.sb.append("motor.").append(motorSide).append(".target = 0");
            addCheckTimeState();
        } else {
            motorOnAction.param.getSpeed().accept(this);
            nlIndent();
            this.sb.append("motor.").append(motorSide).append(".target = _M_MAX * _A");
        }
        return null;
    }

    private void addCheckTimeState() {
        nlIndent();
        this.stateCounter++;
        this.sb.append("_state = ").append(this.stateCounter);
        decrIndentation();
        nlIndent();
        this.sb.append("else");
        incrIndentation();
        nlIndent();
        this.sb.append("_time++");
        decrIndentation();
        nlIndent();
        this.sb.append("end");
        decrIndentation();
        nlIndent();
        this.sb.append("elseif _state == ").append(this.stateCounter).append(" then");
        incrIndentation();
    }


    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String motorSide = motorStopAction.port.toLowerCase();
        this.sb.append("motor.").append(motorSide).append(".target = 0");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        this.sb.append("_time = 0");
        nlIndent();
        this.sb.append("call sound.system(").append(playFileAction.fileName).append(")");
        nlIndent();
        this.sb.append("___duration_ = 1000 / timer.period[0]");
        nlIndent();
        this.stateCounter++;
        this.sb.append("_state = ").append(this.stateCounter);
        decrIndentation();
        nlIndent();
        this.sb.append("elseif _state == ").append(this.stateCounter).append(" then");
        incrIndentation();
        nlIndent();
        this.sb.append("if _time == ").append("___duration_ then");
        incrIndentation();
        addCheckTimeState();
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.sb.append("_time = 0");
        nlIndent();
        this.sb.append("___duration_ = ").append(playNoteAction.duration).append(" / timer.period[0]");
        nlIndent();
        this.sb.append("call sound.freq(").append(playNoteAction.frequency.split("\\.")[0]).append(", ").append(playNoteAction.duration).append("/16)");
        nlIndent();
        this.stateCounter++;
        this.sb.append("_state = ").append(this.stateCounter);
        decrIndentation();
        nlIndent();
        this.sb.append("elseif _state == ").append(this.stateCounter).append(" then");
        incrIndentation();
        nlIndent();
        this.sb.append("if _time == ").append("___duration_ then");
        incrIndentation();
        addCheckTimeState();
        return null;
    }

    @Override
    public Void visitPlayRecordingAction(PlayRecordingAction playRecordingAction) {
        this.sb.append("_time = 0");
        nlIndent();
        playRecordingAction.filename.accept(this);
        nlIndent();
        this.sb.append("call sound.duration(_A, ___duration_)");
        nlIndent();
        this.sb.append("___duration_=___duration_ * 10");
        nlIndent();
        this.sb.append("call sound.replay(_A)");
        nlIndent();
        this.stateCounter++;
        this.sb.append("_state = ").append(this.stateCounter);
        decrIndentation();
        nlIndent();
        this.sb.append("elseif _state == ").append(this.stateCounter).append(" then");
        incrIndentation();
        nlIndent();
        this.sb.append("if _time == ").append("___duration_ then");
        incrIndentation();
        addCheckTimeState();
        return null;
    }

    @Override
    public Void visitLedButtonOnAction(LedButtonOnAction ledButtonOnAction) {
        ledButtonOnAction.led1.accept(this);
        nlIndent();
        this.sb.append("___led_[0] = _A / _LED_DIV");
        nlIndent();
        ledButtonOnAction.led2.accept(this);
        nlIndent();
        this.sb.append("___led_[1] = _A / _LED_DIV");
        nlIndent();
        ledButtonOnAction.led3.accept(this);
        nlIndent();
        this.sb.append("___led_[2] = _A / _LED_DIV");
        nlIndent();
        ledButtonOnAction.led4.accept(this);
        nlIndent();
        this.sb.append("___led_[3] = _A / _LED_DIV");
        nlIndent();
        this.sb.append("call leds.buttons(___led_[0], ___led_[1], ___led_[2], ___led_[3])");
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor rgbColor) {
        rgbColor.R.accept(this);
        nlIndent();
        this.sb.append("___color_[0] = _A");
        nlIndent();
        rgbColor.G.accept(this);
        nlIndent();
        this.sb.append("___color_[1] = _A");
        nlIndent();
        rgbColor.B.accept(this);
        nlIndent();
        this.sb.append("___color_[2] = _A");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        this.sb.append("_A = mic.intensity * 100 / 255");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.sb.append("_A = temperature / 10");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        this.sb.append("_A = _timer");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        this.sb.append("_timer = 0");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.sb.append("_time = 0");
        nlIndent();
        toneAction.duration.accept(this);
        nlIndent();
        this.sb.append("___duration_ = _A");
        nlIndent();
        toneAction.frequency.accept(this);
        nlIndent();
        this.sb.append("call sound.freq(_A, ___duration_ / 16)");
        nlIndent();
        this.sb.append("___duration_ /= timer.period[0]");
        nlIndent();
        this.stateCounter++;
        this.sb.append("_state = ").append(this.stateCounter);
        decrIndentation();
        nlIndent();
        this.sb.append("elseif _state == ").append(this.stateCounter).append(" then");
        incrIndentation();
        nlIndent();
        this.sb.append("if _time == ___duration_ then");
        incrIndentation();
        addCheckTimeState();
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        String multiplierRight = turnAction.direction.toString().equals(SC.RIGHT) ? "-" : "";
        String multiplierLeft = turnAction.direction.toString().equals(SC.LEFT) ? "-" : "";
        move(turnAction.param.getDuration(), turnAction.param.getSpeed(), multiplierRight, multiplierLeft);
        return null;
    }

    private void move(MotorDuration duration, Expr speed, String multRight, String multLeft) {
        if ( duration != null ) {
            this.sb.append("_time = 0");
            nlIndent();
            speed.accept(this);
            nlIndent();
            this.sb.append("motor.left.target = _M_MAX * ").append(multLeft).append("_A");
            nlIndent();
            this.sb.append("motor.right.target = _M_MAX * ").append(multRight).append("_A");
            nlIndent();
            duration.getValue().accept(this);
            nlIndent();
            this.sb.append("___duration_ = _A / timer.period[0]");
            nlIndent();
            this.stateCounter++;
            this.sb.append("_state = ").append(this.stateCounter);
            decrIndentation();
            nlIndent();
            this.sb.append("elseif _state == ").append(this.stateCounter).append(" then");
            incrIndentation();
            nlIndent();
            this.sb.append("if _time == ___duration_ then");
            incrIndentation();
            nlIndent();
            this.sb.append("callsub diffdrive_stop");
            addCheckTimeState();
        } else {
            speed.accept(this);
            nlIndent();
            this.sb.append("motor.left.target = _M_MAX * ").append(multLeft).append("_A");
            nlIndent();
            this.sb.append("motor.right.target = _M_MAX * ").append(multRight).append("_A");
        }
    }

    private void move(MotorDuration duration, Expr speedLeft, Expr speedRight, String multLeft, String multRight) {
        if ( duration != null ) {
            this.sb.append("_time = 0");
            nlIndent();
            speedLeft.accept(this);
            nlIndent();
            this.sb.append("motor.left.target = _M_MAX * ").append(multLeft).append("_A");
            nlIndent();
            speedRight.accept(this);
            nlIndent();
            this.sb.append("motor.right.target = _M_MAX * ").append(multRight).append("_A");
            nlIndent();
            duration.getValue().accept(this);
            nlIndent();
            this.sb.append("___duration_ = _A / timer.period[0]");
            nlIndent();
            this.stateCounter++;
            this.sb.append("_state = ").append(this.stateCounter);
            decrIndentation();
            nlIndent();
            this.sb.append("elseif _state == ").append(this.stateCounter).append(" then");
            incrIndentation();
            nlIndent();
            this.sb.append("if _time == ___duration_ then");
            incrIndentation();
            nlIndent();
            this.sb.append("callsub diffdrive_stop");
            addCheckTimeState();
        } else {
            speedLeft.accept(this);
            nlIndent();
            this.sb.append("motor.left.target = _M_MAX * ").append(multLeft).append("_A");
            nlIndent();
            speedRight.accept(this);
            nlIndent();
            this.sb.append("motor.right.target = _M_MAX * ").append(multRight).append("_A");
        }
    }

    @Override
    public Void visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.sb.append("_time = 0");
        nlIndent();
        waitTimeStmt.time.accept(this);
        nlIndent();
        this.sb.append("___duration_ = _A / timer.period[0]");
        nlIndent();
        this.stateCounter++;
        this.sb.append("_state = ").append(this.stateCounter);
        decrIndentation();
        nlIndent();
        this.sb.append("elseif _state == ").append(this.stateCounter).append(" then");
        incrIndentation();
        nlIndent();
        this.sb.append("if _time == ___duration_ then");
        incrIndentation();
        addCheckTimeState();
        return null;
    }

    @Override
    public Void visitLedCircleOnAction(LedCircleOnAction ledCircleOnAction) {
        ledCircleOnAction.led1.accept(this);
        nlIndent();
        this.sb.append("___led_[0] = _A / _LED_DIV");
        nlIndent();
        ledCircleOnAction.led2.accept(this);
        nlIndent();
        this.sb.append("___led_[1] = _A / _LED_DIV");
        nlIndent();
        ledCircleOnAction.led3.accept(this);
        nlIndent();
        this.sb.append("___led_[2] = _A / _LED_DIV");
        nlIndent();
        ledCircleOnAction.led4.accept(this);
        nlIndent();
        this.sb.append("___led_[3] = _A / _LED_DIV");
        nlIndent();
        ledCircleOnAction.led5.accept(this);
        nlIndent();
        this.sb.append("___led_[4] = _A / _LED_DIV");
        nlIndent();
        ledCircleOnAction.led6.accept(this);
        nlIndent();
        this.sb.append("___led_[5] = _A / _LED_DIV");
        nlIndent();
        ledCircleOnAction.led7.accept(this);
        nlIndent();
        this.sb.append("___led_[6] = _A / _LED_DIV");
        nlIndent();
        ledCircleOnAction.led8.accept(this);
        nlIndent();
        this.sb.append("___led_[7] = _A / _LED_DIV");
        nlIndent();
        this.sb.append("call leds.circle(___led_[0], ___led_[1], ___led_[2], ___led_[3], ___led_[4], ___led_[5], ___led_[6], ___led_[7])");
        return null;
    }

    @Override
    public Void visitLedSoundOnAction(LedSoundOnAction ledSoundOnAction) {
        ledSoundOnAction.led1.accept(this);
        nlIndent();
        this.sb.append("___led_[0] = _A / _LED_DIV");
        nlIndent();
        this.sb.append("call leds.sound(___led_[0])");
        return null;
    }

    @Override
    public Void visitLedTemperatureOnAction(LedTemperatureOnAction ledTemperatureOnAction) {
        ledTemperatureOnAction.led1.accept(this);
        nlIndent();
        this.sb.append("___led_[0] = _A / _LED_DIV");
        nlIndent();
        ledTemperatureOnAction.led2.accept(this);
        nlIndent();
        this.sb.append("___led_[1] = _A / _LED_DIV");
        nlIndent();
        this.sb.append("call leds.temperature(___led_[0], ___led_[1])");
        return null;
    }

    @Override
    public Void visitLedProxHOnAction(LedProxHOnAction ledProxHOnAction) {
        ledProxHOnAction.led1.accept(this);
        nlIndent();
        this.sb.append("___led_[0] = _A / _LED_DIV");
        nlIndent();
        ledProxHOnAction.led2.accept(this);
        nlIndent();
        this.sb.append("___led_[1] = _A / _LED_DIV");
        nlIndent();
        ledProxHOnAction.led3.accept(this);
        nlIndent();
        this.sb.append("___led_[2] = _A / _LED_DIV");
        nlIndent();
        ledProxHOnAction.led4.accept(this);
        nlIndent();
        this.sb.append("___led_[3] = _A / _LED_DIV");
        nlIndent();
        ledProxHOnAction.led5.accept(this);
        nlIndent();
        this.sb.append("___led_[4] = _A / _LED_DIV");
        nlIndent();
        ledProxHOnAction.led6.accept(this);
        nlIndent();
        this.sb.append("___led_[5] = _A / _LED_DIV");
        nlIndent();
        ledProxHOnAction.led7.accept(this);
        nlIndent();
        this.sb.append("___led_[6] = _A / _LED_DIV");
        nlIndent();
        ledProxHOnAction.led8.accept(this);
        nlIndent();
        this.sb.append("___led_[7] = _A / _LED_DIV");
        nlIndent();
        this.sb.append("call leds.prox.h(___led_[0], ___led_[1], ___led_[2], ___led_[3], ___led_[4], ___led_[5], ___led_[6], ___led_[7])");
        return null;
    }

    @Override
    public Void visitLedProxVOnAction(LedProxVOnAction ledProxVOnAction) {
        ledProxVOnAction.led1.accept(this);
        nlIndent();
        this.sb.append("___led_[0] = _A / _LED_DIV");
        nlIndent();
        ledProxVOnAction.led2.accept(this);
        nlIndent();
        this.sb.append("___led_[1] = _A / _LED_DIV");
        nlIndent();
        this.sb.append("call leds.prox.v(___led_[0], ___led_[1])");
        return null;
    }

    @Override
    public Void visitLedsOffAction(LedsOffAction ledsOffAction) {
        this.sb.append("call leds.").append(ledsOffAction.port.toLowerCase()).append("(0, 0, 0)");
        return null;
    }

    @Override
    public Void visitTapSensor(TapSensor tapSensor) {
        this.sb.append("_A = (29 + acc[1]) / 32");
        return null;
    }

    @Override
    public Void visitRecordStartAction(RecordStartAction recordStartAction) {
        recordStartAction.filename.accept(this);
        nlIndent();
        this.sb.append("call sound.record(_A)");
        return null;
    }

    @Override
    public Void visitRecordStopAction(RecordStopAction recordStopAction) {
        this.sb.append("call sound.record(-1)");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("# This file is automatically generated by the Open Roberta Lab.");
        nlIndent();
        this.sb.append("const _M_MAX = 5");
        nlIndent();
        this.sb.append("const _RGB_DIV = 8");
        nlIndent();
        this.sb.append("const _LED_DIV = 3");
        nlIndent();
        this.myMethods = new ArrayList();
        this.getBean(UsedHardwareBean.class).getUserDefinedMethods().forEach(method -> this.myMethods.add(method.getCodeSafeMethodName()));
        this.myMethods.forEach(m -> {
            this.funcStart.add(this.stateCounter);
            this.stateCounter++;
        });
        appendRobotVariables();
        generateVariablesForUsage(this.programPhrases);
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        nlIndent();
        if ( this.getMaxNestedBinaries() > 0 ) {
            String max = "_maxNestedBinaries_";
            int index = this.sb.indexOf(max);
            while ( index != -1 ) {
                this.sb.replace(index, index + max.length(), String.valueOf(this.getMaxNestedBinaries()));
                index += String.valueOf(this.getMaxNestedBinaries()).length();
                index = this.sb.indexOf(max, index);
            }
        }
        if ( this.getMaxNestedMethodCalls() > 0 ) {
            String max = "_maxNestedMethodCalls_";
            int index = this.sb.indexOf(max);
            while ( index != -1 ) {
                this.sb.replace(index, index + max.length(), String.valueOf(this.getMaxNestedMethodCalls()));
                index += String.valueOf(this.getMaxNestedMethodCalls()).length();
                index = this.sb.indexOf(max, index);
            }
        }
        this.sb.append("callsub thymio_close");
        decrIndentation();
        nlIndent();
        this.sb.append("end");
        decrIndentation();
        decrIndentation();
        nlIndent();
        if ( !withWrapping ) {
            return;
        }
        generateUserDefinedMethodsForThymio();
        if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
            String helperMethodImpls = this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.sb.append(helperMethodImpls);
        }
        nlIndent();
        this.sb.append("sub thymio_close");
        incrIndentation();
        nlIndent();
        this.sb.append("timer.period[0] = 0");
        nlIndent();
        this.getBean(UsedHardwareBean.class).getUsedActors().forEach(actuator -> {
            switch ( actuator.getType() ) {
                case "LED_CIRCLE_ON_ACTION":
                    this.sb.append("call leds.circle(0, 0, 0, 0, 0, 0, 0, 0)");
                    nlIndent();
                    break;
                case "LED_BUTTON_ON_ACTION":
                    this.sb.append("call leds.buttons(0, 0, 0, 0)");
                    nlIndent();
                    break;
                case "LED_TEMPERATURE_ON_ACTION":
                    this.sb.append("call leds.temperature(0, 0)");
                    nlIndent();
                    break;
                case "LED_SOUND_ON_ACTION":
                    this.sb.append("call leds.sound(0)");
                    nlIndent();
                    break;
                case "LED_PROXH_ON_ACTION":
                    this.sb.append("call leds.prox.h(0, 0, 0, 0, 0, 0, 0, 0)");
                    nlIndent();
                    break;
                case "LED_PROXV_ON_ACTION":
                    this.sb.append("call leds.prox.v(0, 0)");
                    nlIndent();
                    break;
                case SC.RGBLED:
                    this.sb.append("call leds.").append(actuator.getPort().toLowerCase()).append("(0, 0, 0)");
                    nlIndent();
                    break;
                case SC.MOTOR:
                    this.sb.append("motor.left.target = 0");
                    nlIndent();
                    this.sb.append("motor.right.target = 0");
                    nlIndent();
                    break;
                default:
                    // Nothing to do
            }
        });
        decrIndentation();
       /* this.sb.append("]]></node>");
        nlIndent();
        this.sb.append("</network>");
        nlIndent();*/
    }

    void generateUserDefinedMethodsForThymio() {
        this.getBean(UsedHardwareBean.class).getUserDefinedMethods().forEach(method -> {
            String methodName = method.getCodeSafeMethodName();
            int funcIndex = this.myMethods.indexOf(methodName);
            nlIndent();
            this.sb.append("sub ").append(methodName);
            incrIndentation();
            nlIndent();
            this.sb.append("_state = ").append(this.funcStart.get(funcIndex));
            decrIndentation();
            nlIndent();
        });
    }

    private void appendRobotVariables() {
        nlIndent();
        this.sb.append("var _A               # helper variable to store results of type number");
        nlIndent();
        this.sb.append("var _B[_maxNestedBinaries_]            # helper variable to store results from the left side of a binary expression");
        nlIndent();
        this.sb.append("var _C[_maxNestedBinaries_]            # helper variable to store results from the right side of a binary expression");
        nlIndent();
        this.sb.append("var _state = ").append(this.stateCounter).append("       # current state of the program flow");
        nlIndent();
        this.sb.append("var _time            # current elapsed time");
        if ( !this
            .getBean(UsedHardwareBean.class)
            .getUsedSensors()
            .stream()
            .filter(usedSensor -> usedSensor.getType().equals(SC.TIMER))
            .collect(Collectors.toList()).isEmpty() ) {
            nlIndent();
            this.sb.append("var _timer = 0       # overall elapsed time from timer 1");
        }
        if ( this.stateCounter > 0 ) {
            nlIndent();
            this.sb.append("var _method_count = -1");
            nlIndent();
            this.sb.append("var _return_state[_maxNestedMethodCalls_]");
        }
    }

    private void generateVariablesForUsage(List<Phrase> exprList) {
        List<String> listVariablesWithoutDuplicates = new ArrayList<>(
            new LinkedHashSet<>(this.getBean(UsedHardwareBean.class).getDeclaredVariables()));
        for ( String global : this.getBean(UsedHardwareBean.class).getMarkedVariablesAsGlobal() ) {
            listVariablesWithoutDuplicates.remove(global);
        }
        listVariablesWithoutDuplicates.forEach(var -> {
            nlIndent();
            this.sb.append("var ___").append(var);
            if ( var.equals("color_") ) {
                this.sb.append("[3]");
            } else if ( var.equals("led_") ) {
                this.sb.append("[8]");
            }
        });
    }
}
