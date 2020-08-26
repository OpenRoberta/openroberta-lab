package de.fhg.iais.roberta.typecheck;

import java.util.ArrayList;
import java.util.List;

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
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
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
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IAllActorsVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable JAVA code representation of a phrase to a
 * StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public class InfoCollector<T> implements ILanguageVisitor<T>, ISensorVisitor<T>, IAllActorsVisitor<T> {

    private final List<NepoInfo> infos = new ArrayList<>();

    /**
     * initialize the info collector visitor.
     */
    private InfoCollector() {
    }

    /**
     * collects the infos generated during typechecking for an AST. This is done by a visitor, which is an instance of this class<br>
     *
     * @param phrase whose infos should be collected
     * @return list of collected infos
     */
    public static <T> List<NepoInfo> collectInfos(Phrase<T> phrase) //
    {
        InfoCollector<T> astVisitor = new InfoCollector<>();
        phrase.accept(astVisitor);
        return astVisitor.infos;
    }

    @Override
    public T visitNumConst(NumConst<T> numConst) {
        extractInfos(numConst);
        return null;
    }

    @Override
    public T visitMathConst(MathConst<T> mathConst) {
        extractInfos(mathConst);
        return null;
    }

    @Override
    public T visitBoolConst(BoolConst<T> boolConst) {
        extractInfos(boolConst);
        return null;
    }

    @Override
    public T visitStringConst(StringConst<T> stringConst) {
        extractInfos(stringConst);
        return null;
    }

    @Override
    public T visitNullConst(NullConst<T> nullConst) {
        extractInfos(nullConst);
        return null;
    }

    @Override
    public T visitColorConst(ColorConst<T> colorConst) {
        extractInfos(colorConst);
        return null;
    }

    @Override
    public T visitVar(Var<T> var) {
        extractInfos(var);
        return null;
    }

    @Override
    public T visitUnary(Unary<T> unary) {
        extractInfos(unary);
        return null;
    }

    @Override
    public T visitBinary(Binary<T> binary) {
        extractInfos(binary);
        return null;
    }

    @Override
    public T visitMathPowerFunct(MathPowerFunct<T> func) {
        extractInfos(func);
        return null;
    }

    @Override
    public T visitActionExpr(ActionExpr<T> actionExpr) {
        extractInfos(actionExpr);
        return null;
    }

    @Override
    public T visitSensorExpr(SensorExpr<T> sensorExpr) {
        extractInfos(sensorExpr);
        return null;
    }

    @Override
    public T visitEmptyExpr(EmptyExpr<T> emptyExpr) {
        extractInfos(emptyExpr);
        return null;
    }

    @Override
    public T visitExprList(ExprList<T> exprList) {
        extractInfos(exprList);
        return null;
    }

    @Override
    public T visitActionStmt(ActionStmt<T> actionStmt) {
        extractInfos(actionStmt);
        return null;
    }

    @Override
    public T visitAssignStmt(AssignStmt<T> assignStmt) {
        extractInfos(assignStmt);
        return null;
    }

    @Override
    public T visitExprStmt(ExprStmt<T> exprStmt) {
        extractInfos(exprStmt);
        return null;
    }

    @Override
    public T visitIfStmt(IfStmt<T> ifStmt) {
        extractInfos(ifStmt);
        return null;
    }

    @Override
    public T visitNNStepStmt(NNStepStmt<T> nnStepStmt) {
        extractInfos(nnStepStmt);
        return null;
    }

    @Override
    public T visitRepeatStmt(RepeatStmt<T> repeatStmt) {
        extractInfos(repeatStmt);
        return null;
    }

    @Override
    public T visitSensorStmt(SensorStmt<T> sensorStmt) {
        extractInfos(sensorStmt);
        return null;
    }

    @Override
    public T visitStmtFlowCon(StmtFlowCon<T> stmtFlowCon) {
        extractInfos(stmtFlowCon);
        return null;
    }

    @Override
    public T visitStmtList(StmtList<T> stmtList) {
        extractInfos(stmtList);
        return null;
    }

    @Override
    public T visitDriveAction(DriveAction<T> driveAction) {
        extractInfos(driveAction);
        return null;
    }

    @Override
    public T visitTurnAction(TurnAction<T> turnAction) {
        extractInfos(turnAction);
        return null;
    }

    @Override
    public T visitLightAction(LightAction<T> lightAction) {
        extractInfos(lightAction);
        return null;
    }

    @Override
    public T visitLightStatusAction(LightStatusAction<T> lightStatusAction) {
        extractInfos(lightStatusAction);
        return null;
    }

    @Override
    public T visitMotorGetPowerAction(MotorGetPowerAction<T> motorGetPowerAction) {
        extractInfos(motorGetPowerAction);
        return null;
    }

    @Override
    public T visitMotorOnAction(MotorOnAction<T> motorOnAction) {
        extractInfos(motorOnAction);
        return null;
    }

    @Override
    public T visitMotorSetPowerAction(MotorSetPowerAction<T> motorSetPowerAction) {
        extractInfos(motorSetPowerAction);
        return null;
    }

    @Override
    public T visitMotorStopAction(MotorStopAction<T> motorStopAction) {
        extractInfos(motorStopAction);
        return null;
    }

    @Override
    public T visitClearDisplayAction(ClearDisplayAction<T> clearDisplayAction) {
        extractInfos(clearDisplayAction);
        return null;
    }

    @Override
    public T visitVolumeAction(VolumeAction<T> volumeAction) {
        extractInfos(volumeAction);
        return null;
    }

    @Override
    public T visitSetLanguageAction(SetLanguageAction<T> setLanguageAction) {
        extractInfos(setLanguageAction);
        return null;
    }

    @Override
    public T visitSayTextAction(SayTextAction<T> sayTextAction) {
        extractInfos(sayTextAction);
        return null;
    }

    @Override
    public T visitPlayFileAction(PlayFileAction<T> playFileAction) {
        extractInfos(playFileAction);
        return null;
    }

    @Override
    public T visitShowTextAction(ShowTextAction<T> showTextAction) {
        extractInfos(showTextAction);
        return null;
    }

    @Override
    public T visitMotorDriveStopAction(MotorDriveStopAction<T> stopAction) {
        extractInfos(stopAction);
        return null;
    }

    @Override
    public T visitToneAction(ToneAction<T> toneAction) {
        extractInfos(toneAction);
        return null;
    }

    @Override
    public T visitPlayNoteAction(PlayNoteAction<T> playNoteAction) {
        extractInfos(playNoteAction);
        return null;
    }

    @Override
    public T visitKeysSensor(KeysSensor<T> keysSensor) {
        extractInfos(keysSensor);
        return null;
    }

    @Override
    public T visitColorSensor(ColorSensor<T> colorSensor) {
        extractInfos(colorSensor);
        return null;
    }

    @Override
    public T visitRgbColor(RgbColor<T> rgbColor) {
        extractInfos(rgbColor);
        return null;
    }

    @Override
    public T visitEncoderSensor(EncoderSensor<T> encoderSensor) {
        extractInfos(encoderSensor);
        return null;
    }

    @Override
    public T visitGyroSensor(GyroSensor<T> gyroSensor) {
        extractInfos(gyroSensor);
        return null;
    }

    @Override
    public T visitInfraredSensor(InfraredSensor<T> infraredSensor) {
        extractInfos(infraredSensor);
        return null;
    }

    @Override
    public T visitTimerSensor(TimerSensor<T> timerSensor) {
        extractInfos(timerSensor);
        return null;
    }

    @Override
    public T visitTouchSensor(TouchSensor<T> touchSensor) {
        extractInfos(touchSensor);
        return null;
    }

    @Override
    public T visitUltrasonicSensor(UltrasonicSensor<T> ultrasonicSensor) {
        extractInfos(ultrasonicSensor);
        return null;
    }

    @Override
    public T visitMainTask(MainTask<T> mainTask) {
        extractInfos(mainTask);
        return null;
    }

    @Override
    public T visitActivityTask(ActivityTask<T> activityTask) {
        extractInfos(activityTask);
        return null;
    }

    @Override
    public T visitStartActivityTask(StartActivityTask<T> startActivityTask) {
        extractInfos(startActivityTask);
        return null;
    }

    @Override
    public T visitWaitStmt(WaitStmt<T> waitStmt) {
        extractInfos(waitStmt);
        return null;
    }

    private void extractInfos(Phrase<T> phrase) {
        this.infos.addAll(phrase.getInfos().getInfos());
    }

    @Override
    public T visitLocation(Location<T> location) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitEmptyList(EmptyList<T> emptyList) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitGetSampleSensor(GetSampleSensor<T> sensorGetSample) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitTextPrintFunct(TextPrintFunct<T> textPrintFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitFunctionStmt(FunctionStmt<T> functionStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitFunctionExpr(FunctionExpr<T> functionExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitGetSubFunct(GetSubFunct<T> getSubFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitIndexOfFunct(IndexOfFunct<T> indexOfFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<T> lengthOfIsEmptyFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitListCreate(ListCreate<T> listCreate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitListGetIndex(ListGetIndex<T> listGetIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitListRepeat(ListRepeat<T> listRepeat) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitListSetIndex(ListSetIndex<T> listSetIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMathConstrainFunct(MathConstrainFunct<T> mathConstrainFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMathNumPropFunct(MathNumPropFunct<T> mathNumPropFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMathOnListFunct(MathOnListFunct<T> mathOnListFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMathRandomFloatFunct(MathRandomFloatFunct<T> mathRandomFloatFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMathRandomIntFunct(MathRandomIntFunct<T> mathRandomIntFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMathSingleFunct(MathSingleFunct<T> mathSingleFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMathCastStringFunct(MathCastStringFunct<T> mathCastStringFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMathCastCharFunct(MathCastCharFunct<T> mathCastCharFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitTextCharCastNumberFunct(TextCharCastNumberFunct<T> textCharCastNumberFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitTextStringCastNumberFunct(TextStringCastNumberFunct<T> textStringCastNumberFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitTextJoinFunct(TextJoinFunct<T> textJoinFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitWaitTimeStmt(WaitTimeStmt<T> waitTimeStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitVarDeclaration(VarDeclaration<T> var) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMethodVoid(MethodVoid<T> methodVoid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMethodReturn(MethodReturn<T> methodReturn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMethodIfReturn(MethodIfReturn<T> methodIfReturn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMethodStmt(MethodStmt<T> methodStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMethodCall(MethodCall<T> methodCall) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitMethodExpr(MethodExpr<T> methodExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitBluetoothReceiveAction(BluetoothReceiveAction<T> clearDisplayAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitBluetoothConnectAction(BluetoothConnectAction<T> clearDisplayAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitBluetoothSendAction(BluetoothSendAction<T> clearDisplayAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<T> bluetoothWaitForConnection) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitStmtExpr(StmtExpr<T> stmtExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitShadowExpr(ShadowExpr<T> shadowExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitLightSensor(LightSensor<T> lightSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitSoundSensor(SoundSensor<T> lightSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitCurveAction(CurveAction<T> driveAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitCompassSensor(CompassSensor<T> compassSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitConnectConst(ConnectConst<T> connectConst) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<T> bluetoothCheckConnectAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitTemperatureSensor(TemperatureSensor<T> temperatureSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitStmtTextComment(StmtTextComment<T> stmtTextComment) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitSerialWriteAction(SerialWriteAction<T> serialWriteAction) {
        return null;
    }

    @Override
    public T visitAssertStmt(AssertStmt<T> assertStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitDebugAction(DebugAction<T> debugAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T visitPinWriteValueAction(PinWriteValueAction<T> pinWriteValueAction) {
        return null;
    }
}
