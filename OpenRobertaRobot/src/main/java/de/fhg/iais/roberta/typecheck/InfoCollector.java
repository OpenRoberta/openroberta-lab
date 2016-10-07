package de.fhg.iais.roberta.typecheck;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.generic.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.CurveAction;
import de.fhg.iais.roberta.syntax.action.generic.DriveAction;
import de.fhg.iais.roberta.syntax.action.generic.LightAction;
import de.fhg.iais.roberta.syntax.action.generic.LightSensorAction;
import de.fhg.iais.roberta.syntax.action.generic.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.generic.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.ToneAction;
import de.fhg.iais.roberta.syntax.action.generic.TurnAction;
import de.fhg.iais.roberta.syntax.action.generic.VolumeAction;
import de.fhg.iais.roberta.syntax.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.BoolConst;
import de.fhg.iais.roberta.syntax.expr.ColorConst;
import de.fhg.iais.roberta.syntax.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.expr.EmptyList;
import de.fhg.iais.roberta.syntax.expr.ExprList;
import de.fhg.iais.roberta.syntax.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.expr.ListCreate;
import de.fhg.iais.roberta.syntax.expr.MathConst;
import de.fhg.iais.roberta.syntax.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.expr.NullConst;
import de.fhg.iais.roberta.syntax.expr.NumConst;
import de.fhg.iais.roberta.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.expr.StringConst;
import de.fhg.iais.roberta.syntax.expr.Unary;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.methods.MethodCall;
import de.fhg.iais.roberta.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.stmt.StmtList;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they
 * append a human-readable JAVA code representation of a phrase to a StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public class InfoCollector<T> implements AstVisitor<T> {

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
        InfoCollector<T> astVisitor = new InfoCollector<T>();
        phrase.visit(astVisitor);
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
    public T visitPlayFileAction(PlayFileAction<T> playFileAction) {
        extractInfos(playFileAction);
        return null;
    }

    @Override
    public T visitShowPictureAction(ShowPictureAction<T> showPictureAction) {
        extractInfos(showPictureAction);
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
    public T visitBrickSensor(BrickSensor<T> brickSensor) {
        extractInfos(brickSensor);
        return null;
    }

    @Override
    public T visitColorSensor(ColorSensor<T> colorSensor) {
        extractInfos(colorSensor);
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

    private void extractInfos(Phrase<T> numConst) {
        this.infos.addAll(numConst.getInfos().getInfos());
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
    public T visitLightSensorAction(LightSensorAction<T> lightSensorAction) {
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
    public T visitVoltageSensor(VoltageSensor<T> voltageSensor) {
        // TODO Auto-generated method stub
        return null;
    }
}
