package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
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
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ParticleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.hardware.actor.IAllActorsVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * General interface for collectors. Collectors collect different language or hardware aspects needed in the code generation. By default, visits all language
 * and hardware expressions and their children. TODO use this to split {@link de.fhg.iais.roberta.visitor.validate.AbstractCollectorVisitor} and
 * {@link AbstractUsedHardwareCollectorVisitor} into TODO multiple individual visitors instead of the current one in all solution
 */
public interface ICollectorVisitor extends ISensorVisitor<Void>, IAllActorsVisitor<Void>, ILanguageVisitor<Void> {

    /**
     * Collection method which should be run over the program in the collector visitor's constructor. Should effectively be final.
     *
     * @param phrasesSet the phrases of the program
     */
    default void collect(List<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(!phrasesSet.isEmpty());
        for ( List<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.accept(this);
            }
        }
    }

    // Language

    @Override
    default Void visitNumConst(NumConst<Void> numConst) {
        return null;
    }

    @Override
    default Void visitMathConst(MathConst<Void> mathConst) {
        return null;
    }

    @Override
    default Void visitBoolConst(BoolConst<Void> boolConst) {
        return null;
    }

    @Override
    default Void visitStringConst(StringConst<Void> stringConst) {
        return null;
    }

    @Override
    default Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    default Void visitNullConst(NullConst<Void> nullConst) {
        return null;
    }

    @Override
    default Void visitColorConst(ColorConst<Void> colorConst) {
        return null;
    }

    @Override
    default Void visitRgbColor(RgbColor<Void> rgbColor) {
        rgbColor.getR().accept(this);
        rgbColor.getG().accept(this);
        rgbColor.getB().accept(this);
        rgbColor.getA().accept(this);
        return null;
    }

    @Override
    default Void visitVar(Var<Void> var) {
        return null;
    }

    @Override
    default Void visitVarDeclaration(VarDeclaration<Void> var) {
        var.getValue().accept(this);
        return null;
    }

    @Override
    default Void visitUnary(Unary<Void> unary) {
        unary.getExpr().accept(this);
        return null;
    }

    @Override
    default Void visitBinary(Binary<Void> binary) {
        binary.getLeft().accept(this);
        binary.getRight().accept(this);
        return null;
    }

    @Override
    default Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        mathPowerFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitEmptyList(EmptyList<Void> emptyList) {
        return null;
    }

    @Override
    default Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        return null;
    }

    @Override
    default Void visitExprList(ExprList<Void> exprList) {
        exprList.get().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        assignStmt.getExpr().accept(this);
        return null;
    }

    @Override
    default Void visitIfStmt(IfStmt<Void> ifStmt) {
        ifStmt.getExpr().forEach(expr -> expr.accept(this));
        ifStmt.getThenList().forEach(expr -> expr.accept(this));
        ifStmt.getElseList().accept(this);

        // TODO is this needed?
        //ifStmt.getElseList().get().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    default Void visitNNStepStmt(NNStepStmt<Void> nnStepStmt) {
        return null;
    }

    @Override
    default Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        repeatStmt.getExpr().accept(this);
        repeatStmt.getList().accept(this);
        return null;
    }

    @Override
    default Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        return null;
    }

    @Override
    default Void visitStmtList(StmtList<Void> stmtList) {
        stmtList.get().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().accept(this);
        return null;
    }

    @Override
    default Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        waitStmt.getStatements().accept(this);
        return null;
    }

    @Override
    default Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        waitTimeStmt.getTime().accept(this);
        return null;
    }

    @Override
    default Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        textPrintFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitStmtTextComment(StmtTextComment<Void> stmtTextComment) {
        return null;
    }

    @Override
    default Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        getSubFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        indexOfFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        lengthOfIsEmptyFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitListCreate(ListCreate<Void> listCreate) {
        listCreate.getValue().accept(this);
        return null;
    }

    @Override
    default Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        listGetIndex.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitListRepeat(ListRepeat<Void> listRepeat) {
        listRepeat.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        listSetIndex.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        mathConstrainFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        mathNumPropFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        mathOnListFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        return null;
    }

    @Override
    default Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        mathRandomIntFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        mathSingleFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        mathCastStringFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        mathCastCharFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        textCharCastNumberFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        textStringCastNumberFunct.getParam().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    default Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        textJoinFunct.getParam().accept(this);
        return null;
    }

    @Override
    default Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        methodVoid.getParameters().accept(this);
        methodVoid.getBody().accept(this);
        return null;
    }

    @Override
    default Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        methodReturn.getParameters().accept(this);
        methodReturn.getBody().accept(this);
        methodReturn.getReturnValue().accept(this);
        return null;
    }

    @Override
    default Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        methodIfReturn.getCondition().accept(this);
        methodIfReturn.getReturnValue().accept(this);
        return null;
    }

    @Override
    default Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        methodStmt.getMethod().accept(this);
        return null;
    }

    @Override
    default Void visitMethodCall(MethodCall<Void> methodCall) {
        methodCall.getParametersValues().accept(this);
        return null;
    }

    @Override
    default Void visitActivityTask(ActivityTask<Void> activityTask) {
        activityTask.getActivityName().accept(this);
        return null;
    }

    @Override
    default Void visitStartActivityTask(StartActivityTask<Void> startActivityTask) {
        startActivityTask.getActivityName().accept(this);
        return null;
    }

    @Override
    default Void visitAssertStmt(AssertStmt<Void> assertStmt) {
        assertStmt.getAssert().accept(this);
        return null;
    }

    @Override
    default Void visitDebugAction(DebugAction<Void> debugAction) {
        debugAction.getValue().accept(this);
        return null;
    }

    // Hardware

    @Override
    default Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
        bluetoothReceiveAction.getConnection().accept(this);
        return null;
    }

    @Override
    default Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        bluetoothConnectAction.getAddress().accept(this);
        return null;
    }

    @Override
    default Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        bluetoothSendAction.getMsg().accept(this);
        bluetoothSendAction.getConnection().accept(this);
        return null;
    }

    @Override
    default Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        return null;
    }

    @Override
    default Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        bluetoothCheckConnectAction.getConnection().accept(this);
        return null;
    }

    @Override
    default Void visitDriveAction(DriveAction<Void> driveAction) {
        driveAction.getParam().getSpeed().accept(this);
        return null;
    }

    @Override
    default Void visitCurveAction(CurveAction<Void> curveAction) {
        curveAction.getParamLeft().getSpeed().accept(this);
        curveAction.getParamRight().getSpeed().accept(this);
        return null;
    }

    @Override
    default Void visitTurnAction(TurnAction<Void> turnAction) {
        turnAction.getParam().getSpeed().accept(this);
        return null;
    }

    @Override
    default Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        return null;
    }

    @Override
    default Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        return null;
    }

    @Override
    default Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        showTextAction.getMsg().accept(this);
        showTextAction.getX().accept(this);
        showTextAction.getY().accept(this);
        return null;
    }

    @Override
    default Void visitLightAction(LightAction<Void> lightAction) {
        lightAction.getRgbLedColor().accept(this);
        return null;
    }

    @Override
    default Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return null;
    }

    @Override
    default Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return null;
    }

    @Override
    default Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        Expr<Void> durationValue = motorOnAction.getDurationValue();
        if ( durationValue != null ) { // TODO why is this necessary?
            motorOnAction.getDurationValue().accept(this);
        }
        MotionParam<Void> param = motorOnAction.getParam();
        MotorDuration<Void> duration = param.getDuration();
        if ( duration != null ) { // TODO why is this necessary?
            duration.getValue().accept(this);
        }
        param.getSpeed().accept(this);
        return null;
    }

    @Override
    default Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        motorSetPowerAction.getPower().accept(this);
        return null;
    }

    @Override
    default Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        return null;
    }

    @Override
    default Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        serialWriteAction.getValue().accept(this);
        return null;
    }

    @Override
    default Void visitToneAction(ToneAction<Void> toneAction) {
        toneAction.getFrequency().accept(this);
        toneAction.getDuration().accept(this);
        return null;
    }

    @Override
    default Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return null;
    }

    @Override
    default Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        volumeAction.getVolume().accept(this);
        return null;
    }

    @Override
    default Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    default Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        return null;
    }

    @Override
    default Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        sayTextAction.getMsg().accept(this);
        sayTextAction.getPitch().accept(this);
        sayTextAction.getSpeed().accept(this);
        return null;
    }

    @Override
    default Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        return null;
    }

    @Override
    default Void visitColorSensor(ColorSensor<Void> colorSensor) {
        return null;
    }

    @Override
    default Void visitLightSensor(LightSensor<Void> lightSensor) {
        return null;
    }

    @Override
    default Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        return null;
    }

    @Override
    default Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        return null;
    }

    @Override
    default Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        return null;
    }

    @Override
    default Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        return null;
    }

    @Override
    default Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        return null;
    }

    @Override
    default Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        return null;
    }

    @Override
    default Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        return null;
    }

    @Override
    default Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        return null;
    }

    @Override
    default Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    default Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        return null;
    }

    @Override
    default Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        return null;
    }

    @Override
    default Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        return null;
    }

    @Override
    default Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        return null;
    }

    @Override
    default Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        return null;
    }

    @Override
    default Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        return null;
    }

    @Override
    default Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        return null;
    }

    @Override
    default Void visitMoistureSensor(MoistureSensor<Void> moistureSensor) {
        return null;
    }

    @Override
    default Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        return null;
    }

    @Override
    default Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        return null;
    }

    @Override
    default Void visitDropSensor(DropSensor<Void> dropSensor) {
        return null;
    }

    @Override
    default Void visitPulseSensor(PulseSensor<Void> pulseSensor) {
        return null;
    }

    @Override
    default Void visitRfidSensor(RfidSensor<Void> rfidSensor) {
        return null;
    }

    @Override
    default Void visitVemlLightSensor(VemlLightSensor<Void> vemlLightSensor) {
        return null;
    }

    @Override
    default Void visitParticleSensor(ParticleSensor<Void> particleSensor) {
        return null;
    }

    @Override
    default Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueAction) {
        pinWriteValueAction.getValue().accept(this);
        return null;
    }
}
