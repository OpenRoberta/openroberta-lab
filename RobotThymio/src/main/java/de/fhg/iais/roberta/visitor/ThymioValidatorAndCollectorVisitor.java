package de.fhg.iais.roberta.visitor;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
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
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.thymio.TapSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;


public class ThymioValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IThymioVisitor<Void> {

    public ThymioValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitRgbColor(RgbColor rgbColor) {
        super.visitRgbColor(rgbColor);
        usedHardwareBuilder.addDeclaredVariable("color_");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        super.visitWaitTimeStmt(waitTimeStmt);
        //requiredComponentVisited(waitTimeStmt, waitTimeStmt.time);
        usedHardwareBuilder.addDeclaredVariable("duration_");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        super.visitColorConst(colorConst);
        usedHardwareBuilder.addDeclaredVariable("color_");
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        addErrorToPhrase(indexOfFunct, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex listGetIndex) {
        super.visitListGetIndex(listGetIndex);
        IndexLocation location = (IndexLocation) listGetIndex.location;
        switch ( location ) {
            case FIRST:
            case RANDOM:
            case FROM_END:
            case LAST:
                addErrorToPhrase(listGetIndex, "MODE_NOT_SUPPORTED");
                break;
            default:
                break;
        }
        if ( listGetIndex.getElementOperation() != ListElementOperations.GET ) {
            addErrorToPhrase(listGetIndex, "MODE_NOT_SUPPORTED");
        }
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate listCreate) {
        requiredComponentVisited(listCreate, listCreate.exprList);
        int listSize = listCreate.exprList.get().size();
        for ( int i = 0; i < listSize; i++ ) {
            if ( (listCreate.typeVar.toString().equals("NUMBER") && !listCreate.exprList.get().get(i).getKind().hasName("NUM_CONST")) || (listCreate.typeVar.toString().equals("COLOR") && !listCreate.exprList.get().get(i).getKind().hasName("COLOR_CONST")) ) {
                addErrorToPhrase(listCreate.exprList.get().get(i), "NO_CONST_NOT_SUPPORTED");
            }
        }
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat listRepeat) {
        requiredComponentVisited(listRepeat, listRepeat.param);
        if ( !listRepeat.getCounter().getClass().equals(NumConst.class) ) {
            addErrorToPhrase(listRepeat.param.get(1), "NO_CONST_NOT_SUPPORTED");
        }
        if ( (listRepeat.typeVar == BlocklyType.NUMBER && !listRepeat.param.get(0).getClass().equals(NumConst.class)) || (listRepeat.typeVar == BlocklyType.COLOR && !listRepeat.param.get(0).getClass().equals(ColorConst.class)) ) {
            addErrorToPhrase(listRepeat.getElement(), "NO_CONST_NOT_SUPPORTED");
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex listSetIndex) {
        super.visitListSetIndex(listSetIndex);
        IndexLocation location = (IndexLocation) listSetIndex.location;
        switch ( location ) {
            case FIRST:
            case RANDOM:
            case FROM_END:
            case LAST:
                addErrorToPhrase(listSetIndex, "MODE_NOT_SUPPORTED");
                break;
            default:
                break;
        }
        if ( listSetIndex.mode != ListElementOperations.SET ) {
            addErrorToPhrase(listSetIndex, "MODE_NOT_SUPPORTED");
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration varDeclaration) {
        super.visitVarDeclaration(varDeclaration);
        if ( (varDeclaration.typeVar == BlocklyType.NUMBER && !varDeclaration.value.getKind().hasName("NUM_CONST")) || (varDeclaration.typeVar == BlocklyType.COLOR && !varDeclaration.value.getKind().hasName("COLOR_CONST")) ) {
            addErrorToPhrase(varDeclaration.value, "NO_CONST_NOT_SUPPORTED");
        }
        usedHardwareBuilder.addMarkedVariableAsGlobal(varDeclaration.name);
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        requiredComponentVisited(curveAction, curveAction.paramLeft.getSpeed(), curveAction.paramRight.getSpeed());
        if ( curveAction.paramRight.getDuration() != null ) {
            requiredComponentVisited(curveAction, curveAction.paramRight.getDuration().getValue());
            usedMethodBuilder.addUsedMethod(ThymioMethods.STOP);
            usedHardwareBuilder.addDeclaredVariable("duration_");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(SC.BOTH, SC.MOTOR));
        }
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        requiredComponentVisited(driveAction, driveAction.param.getSpeed());
        if ( driveAction.param.getDuration() != null ) {
            requiredComponentVisited(driveAction, driveAction.param.getDuration().getValue());
            usedMethodBuilder.addUsedMethod(ThymioMethods.STOP);
            usedHardwareBuilder.addDeclaredVariable("duration_");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(SC.BOTH, SC.MOTOR));
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        requiredComponentVisited(lightAction, lightAction.rgbLedColor);
        usedHardwareBuilder.addUsedActor(new UsedActor(lightAction.port, SC.RGBLED));
        usedHardwareBuilder.addDeclaredVariable("color_");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        requiredComponentVisited(mainTask, mainTask.variables);
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        usedMethodBuilder.addUsedMethod(ThymioMethods.STOP);
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        requiredComponentVisited(motorOnAction, motorOnAction.param.getSpeed());
        if ( motorOnAction.param.getDuration() != null ) {
            requiredComponentVisited(motorOnAction, motorOnAction.param.getDuration().getValue());
            usedHardwareBuilder.addDeclaredVariable("duration_");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(SC.BOTH, SC.MOTOR));
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        usedHardwareBuilder.addDeclaredVariable("duration_");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        usedHardwareBuilder.addDeclaredVariable("duration_");
        return null;
    }

    @Override
    public Void visitPlayRecordingAction(PlayRecordingAction playRecordingAction) {
        requiredComponentVisited(playRecordingAction, playRecordingAction.filename);
        usedHardwareBuilder.addDeclaredVariable("duration_");
        return null;
    }

    @Override
    public Void visitLedButtonOnAction(LedButtonOnAction ledButtonOnAction) {
        usedHardwareBuilder.addDeclaredVariable("led_");
        usedHardwareBuilder.addUsedActor(new UsedActor(ledButtonOnAction.getKind().getName(), ledButtonOnAction.getKind().getName()));
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getMode(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerReset.sensorPort, SC.TIMER, SC.DEFAULT));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        requiredComponentVisited(toneAction, toneAction.duration, toneAction.frequency);
        usedHardwareBuilder.addUsedActor(new UsedActor(toneAction.port, SC.BUZZER));
        usedHardwareBuilder.addDeclaredVariable("duration_");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        requiredComponentVisited(turnAction, turnAction.param.getSpeed());
        if ( turnAction.param.getDuration() != null ) {
            requiredComponentVisited(turnAction, turnAction.param.getDuration().getValue());
            usedMethodBuilder.addUsedMethod(ThymioMethods.STOP);
            usedHardwareBuilder.addDeclaredVariable("duration_");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(SC.BOTH, SC.MOTOR));
        }
        return null;
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
    public Void visitLedCircleOnAction(LedCircleOnAction ledCircleOnAction) {
        requiredComponentVisited(ledCircleOnAction, ledCircleOnAction.led1, ledCircleOnAction.led2, ledCircleOnAction.led3, ledCircleOnAction.led4, ledCircleOnAction.led5, ledCircleOnAction.led6, ledCircleOnAction.led7, ledCircleOnAction.led8);
        usedHardwareBuilder.addDeclaredVariable("led_");
        usedHardwareBuilder.addUsedActor(new UsedActor(ledCircleOnAction.getKind().getName(), ledCircleOnAction.getKind().getName()));
        return null;
    }

    @Override
    public Void visitLedSoundOnAction(LedSoundOnAction ledSoundOnAction) {
        requiredComponentVisited(ledSoundOnAction, ledSoundOnAction.led1);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledSoundOnAction.getKind().getName(), ledSoundOnAction.getKind().getName()));
        usedHardwareBuilder.addDeclaredVariable("led_");
        return null;
    }

    @Override
    public Void visitLedTemperatureOnAction(LedTemperatureOnAction ledTemperatureOnAction) {
        requiredComponentVisited(ledTemperatureOnAction, ledTemperatureOnAction.led1, ledTemperatureOnAction.led2);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledTemperatureOnAction.getKind().getName(), ledTemperatureOnAction.getKind().getName()));
        usedHardwareBuilder.addDeclaredVariable("led_");
        return null;
    }

    @Override
    public Void visitLedProxHOnAction(LedProxHOnAction ledProxHOnAction) {
        requiredComponentVisited(ledProxHOnAction, ledProxHOnAction.led1, ledProxHOnAction.led2, ledProxHOnAction.led3, ledProxHOnAction.led4, ledProxHOnAction.led5, ledProxHOnAction.led6, ledProxHOnAction.led7, ledProxHOnAction.led8);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledProxHOnAction.getKind().getName(), ledProxHOnAction.getKind().getName()));
        usedHardwareBuilder.addDeclaredVariable("led_");
        return null;
    }

    @Override
    public Void visitLedProxVOnAction(LedProxVOnAction ledProxVOnAction) {
        requiredComponentVisited(ledProxVOnAction, ledProxVOnAction.led1, ledProxVOnAction.led2);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledProxVOnAction.getKind().getName(), ledProxVOnAction.getKind().getName()));
        usedHardwareBuilder.addDeclaredVariable("led_");
        return null;
    }

    public Void visitLedsOffAction(LedsOffAction led_sOffAction) {
        return null;
    }

    @Override
    public Void visitTapSensor(TapSensor tapSensor) {
        return null;
    }

    @Override
    public Void visitRecordStartAction(RecordStartAction recordStartAction) {
        requiredComponentVisited(recordStartAction, recordStartAction.filename);
        return null;
    }

    @Override
    public Void visitRecordStopAction(RecordStopAction recordStopAction) {
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        super.visitMathSingleFunct(mathSingleFunct);
        switch ( mathSingleFunct.functName ) {
            case ROOT:
            case SIN:
            case COS:
            case SQUARE:
            case ABS:
                break;
            default:
                addErrorToPhrase(mathSingleFunct, "MODE_NOT_SUPPORTED");
        }
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        super.visitMathNumPropFunct(mathNumPropFunct);
        switch ( mathNumPropFunct.functName ) {
            case EVEN:
            case ODD:
            case WHOLE:
            case POSITIVE:
            case NEGATIVE:
            case DIVISIBLE_BY:
                break;
            case PRIME:
                addErrorToPhrase(mathNumPropFunct, "MODE_NOT_SUPPORTED");
                break;
            default:
                throw new DbcException("Statement not supported by Aseba!");
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        super.visitMathOnListFunct(mathOnListFunct);
        switch ( mathOnListFunct.functName ) {
            case MIN:
            case MAX:
            case AVERAGE:
                // supported
                break;
            default:
                addErrorToPhrase(mathOnListFunct, "MODE_NOT_SUPPORTED");
                break;
        }
        return null;
    }

    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        throw new DbcException("Block not supported by Aseba!");
    }

    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        throw new DbcException("Block not supported by Aseba!");
    }

    public Void visitAssertStmt(AssertStmt assertStmt) {
        addErrorToPhrase(assertStmt, "BLOCK_NOT_SUPPORTED");
        return null;
    }
}
