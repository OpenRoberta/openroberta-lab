package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.generic.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.generic.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.CurveAction;
import de.fhg.iais.roberta.syntax.action.generic.DriveAction;
import de.fhg.iais.roberta.syntax.action.generic.LightAction;
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
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
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

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface AstVisitor<V> {
    static final String INDENT = "    ";

    /**
     * visit a {@link NumConst}.
     *
     * @param numConst phrase to be visited
     */
    V visitNumConst(NumConst<V> numConst);

    /**
     * visit a {@link MathConst}.
     *
     * @param mathConst to be visited
     */
    V visitMathConst(MathConst<V> mathConst);

    /**
     * visit a {@link BoolConst}.
     *
     * @param boolConst to be visited
     */
    V visitBoolConst(BoolConst<V> boolConst);

    /**
     * visit a {@link StringConst}.
     *
     * @param stringConst to be visited
     */
    V visitStringConst(StringConst<V> stringConst);

    /**
     * visit a {@link NullConst}.
     *
     * @param nullConst to be visited
     */
    V visitNullConst(NullConst<V> nullConst);

    /**
     * visit a {@link ColorConst}.
     *
     * @param colorConst to be visited
     */
    V visitColorConst(ColorConst<V> colorConst);

    /**
     * visit a {@link Var}.
     *
     * @param var to be visited
     */
    V visitVar(Var<V> var);

    /**
     * visit a {@link VarDeclaration}.
     *
     * @param var to be visited
     */
    V visitVarDeclaration(VarDeclaration<V> var);

    /**
     * visit a {@link Unary}.
     *
     * @param unary to be visited
     */
    V visitUnary(Unary<V> unary);

    /**
     * visit a {@link Binary}.
     *
     * @param binary to be visited
     */

    V visitBinary(Binary<V> binary);

    /**
     * visit a {@link MathPowerFunct}.
     *
     * @param funct to be visited
     */
    V visitMathPowerFunct(MathPowerFunct<V> mathPowerFunct);

    /**
     * visit a {@link ActionExpr}.
     *
     * @param actionExpr to be visited
     */
    default V visitActionExpr(ActionExpr<V> actionExpr) {
        actionExpr.getAction().visit(this);
        return null;
    }

    /**
     * visit a {@link SensorExpr}.
     *
     * @param sensorExpr to be visited
     */
    default V visitSensorExpr(SensorExpr<V> sensorExpr) {
        sensorExpr.getSens().visit(this);
        return null;
    }

    /**
     * visit a {@link MethodExpr}.
     *
     * @param sensorExpr to be visited
     */
    default V visitMethodExpr(MethodExpr<V> methodExpr) {
        methodExpr.getMethod().visit(this);
        return null;
    }

    /**
     * visit a {@link EmptyList}.
     *
     * @param sensorExpr to be visited
     */
    V visitEmptyList(EmptyList<V> emptyList);

    /**
     * visit a {@link EmptyExpr}.
     *
     * @param emptyExpr to be visited
     */
    V visitEmptyExpr(EmptyExpr<V> emptyExpr);

    /**
     * visit a {@link ExprList}.
     *
     * @param exprList to be visited
     */
    V visitExprList(ExprList<V> exprList);

    /**
     * visit a {@link ActionStmt}.
     *
     * @param actionStmt to be visited
     */
    V visitActionStmt(ActionStmt<V> actionStmt);

    /**
     * visit a {@link AssignStmt}.
     *
     * @param assignStmt to be visited
     */
    V visitAssignStmt(AssignStmt<V> assignStmt);

    /**
     * visit a {@link ExprStmt}.
     *
     * @param exprStmt to be visited
     */
    default V visitExprStmt(ExprStmt<V> exprStmt) {
        exprStmt.getExpr().visit(this);
        return null;
    }

    /**
     * visit a {@link IfStmt}.
     *
     * @param ifStmt to be visited
     */
    V visitIfStmt(IfStmt<V> ifStmt);

    /**
     * visit a {@link RepeatStmt}.
     *
     * @param repeatStmt to be visited
     */
    V visitRepeatStmt(RepeatStmt<V> repeatStmt);

    /**
     * visit a {@link SensorStmt}.
     *
     * @param sensorStmt to be visited
     */
    default V visitSensorStmt(SensorStmt<V> sensorStmt) {
        sensorStmt.getSensor().visit(this);
        return null;
    }

    /**
     * visit a {@link StmtFlowCon}.
     *
     * @param stmtFlowCon to be visited
     */
    V visitStmtFlowCon(StmtFlowCon<V> stmtFlowCon);

    /**
     * visit a {@link StmtList}.
     *
     * @param stmtList to be visited
     */

    V visitStmtList(StmtList<V> stmtList);

    /**
     * visit a {@link DriveAction}.
     *
     * @param driveAction to be visited
     */
    V visitDriveAction(DriveAction<V> driveAction);

    /**
     * visit a {@link CurveAction}.
     *
     * @param turnAction to be visited
     */
    V visitCurveAction(CurveAction<V> curveAction);

    /**
     * visit a {@link TurnAction}.
     *
     * @param turnAction to be visited
     */
    V visitTurnAction(TurnAction<V> turnAction);

    /**
     * visit a {@link LightAction}.
     *
     * @param lightAction to be visited
     */
    V visitLightAction(LightAction<V> lightAction);

    /**
     * visit a {@link LightStatusAction}.
     *
     * @param lightStatusAction to be visited
     */
    V visitLightStatusAction(LightStatusAction<V> lightStatusAction);

    /**
     * visit a {@link MotorGetPowerAction}.
     *
     * @param motorGetPowerAction to be visited
     */
    V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction);

    /**
     * visit a {@link MotorOnAction}.
     *
     * @param motorOnAction
     */
    V visitMotorOnAction(MotorOnAction<V> motorOnAction);

    /**
     * visit a {@link MotorSetPowerAction}.
     *
     * @param motorSetPowerAction
     */
    V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction);

    /**
     * visit a {@link MotorStopAction}.
     *
     * @param motorStopAction
     */
    V visitMotorStopAction(MotorStopAction<V> motorStopAction);

    /**
     * visit a {@link ClearDisplayAction}.
     *
     * @param clearDisplayAction to be visited
     */
    V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction);

    /**
     * visit a {@link VolumeAction}.
     *
     * @param volumeAction to be visited
     */
    V visitVolumeAction(VolumeAction<V> volumeAction);

    /**
     * visit a {@link PlayFileAction}.
     *
     * @param playFileAction
     */
    V visitPlayFileAction(PlayFileAction<V> playFileAction);

    /**
     * visit a {@link ShowPictureAction}.
     *
     * @param showPictureAction
     */
    V visitShowPictureAction(ShowPictureAction<V> showPictureAction);

    /**
     * visit a {@link ShowTextAction}.
     *
     * @param showTextAction
     */
    V visitShowTextAction(ShowTextAction<V> showTextAction);

    /**
     * visit a {@link MotorDriveStopAction}.
     *
     * @param stopAction
     */
    V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction);

    /**
     * visit a {@link ToneAction}.
     *
     * @param toneAction to be visited
     */
    V visitToneAction(ToneAction<V> toneAction);

    /**
     * visit a {@link MainTask}.
     *
     * @param MainTask to be visited
     */
    V visitMainTask(MainTask<V> mainTask);

    /**
     * visit a {@link ActivityTask}.
     *
     * @param activityTask to be visited
     */
    default V visitActivityTask(ActivityTask<V> activityTask) {
        return null;
    }

    /**
     * visit a {@link StartActivityTask}.
     *
     * @param startActivityTask to be visited
     */
    default V visitStartActivityTask(StartActivityTask<V> startActivityTask) {
        return null;
    }

    /**
     * visit a {@link WaitStmt}.
     *
     * @param waitStmt to be visited
     */
    V visitWaitStmt(WaitStmt<V> waitStmt);

    /**
     * visit a {@link WaitTimeStmt}.
     *
     * @param waitStmt to be visited
     */
    V visitWaitTimeStmt(WaitTimeStmt<V> waitTimeStmt);

    /**
     * visit a {@link Location}.
     *
     * @param location to be visited
     */
    default V visitLocation(Location<V> location) {
        return null;
    }

    /**
     * visit a {@link TextPrintFunct}.
     *
     * @param textPrintFunct to be visited
     */
    V visitTextPrintFunct(TextPrintFunct<V> textPrintFunct);

    /**
     * visit a {@link FunctionStmt}.
     *
     * @param functionStmt to be visited
     */
    default V visitFunctionStmt(FunctionStmt<V> functionStmt) {
        functionStmt.getFunction().visit(this);
        return null;
    }

    /**
     * visit a {@link FunctionExpr}.
     *
     * @param functionExpr to be visited
     */
    default V visitFunctionExpr(FunctionExpr<V> functionExpr) {
        functionExpr.getFunction().visit(this);
        return null;
    }

    /**
     * visit a {@link GetSubFunct}.
     *
     * @param getSubFunct to be visited
     */
    V visitGetSubFunct(GetSubFunct<V> getSubFunct);

    /**
     * visit a {@link IndexOfFunct}.
     *
     * @param indexOfFunct to be visited
     */
    V visitIndexOfFunct(IndexOfFunct<V> indexOfFunct);

    /**
     * visit a {@link LengthOfIsEmptyFunct}.
     *
     * @param lengthOfIsEmptyFunct to be visited
     */
    V visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<V> lengthOfIsEmptyFunct);

    /**
     * visit a {@link ListCreate}.
     *
     * @param listCreate to be visited
     */
    V visitListCreate(ListCreate<V> listCreate);

    /**
     * visit a {@link ListGetIndex}.
     *
     * @param listGetIndex to be visited
     */
    V visitListGetIndex(ListGetIndex<V> listGetIndex);

    /**
     * visit a {@link ListRepeat}.
     *
     * @param listRepeat to be visited
     */
    V visitListRepeat(ListRepeat<V> listRepeat);

    /**
     * visit a {@link ListSetIndex}.
     *
     * @param listSetIndex to be visited
     */
    V visitListSetIndex(ListSetIndex<V> listSetIndex);

    /**
     * visit a {@link MathConstrainFunct}.
     *
     * @param mathConstrainFunct to be visited
     */
    V visitMathConstrainFunct(MathConstrainFunct<V> mathConstrainFunct);

    /**
     * visit a {@link MathNumPropFunct}.
     *
     * @param mathNumPropFunct to be visited
     */
    V visitMathNumPropFunct(MathNumPropFunct<V> mathNumPropFunct);

    /**
     * visit a {@link MathOnListFunct}.
     *
     * @param mathOnListFunct to be visited
     */
    V visitMathOnListFunct(MathOnListFunct<V> mathOnListFunct);

    /**
     * visit a {@link MathRandomFloatFunct}.
     *
     * @param mathOnListFunct to be visited
     */
    V visitMathRandomFloatFunct(MathRandomFloatFunct<V> mathRandomFloatFunct);

    /**
     * visit a {@link MathRandomIntFunct}.
     *
     * @param mathRandomIntFunct to be visited
     */
    V visitMathRandomIntFunct(MathRandomIntFunct<V> mathRandomIntFunct);

    /**
     * visit a {@link MathSingleFunct}.
     *
     * @param mathSingleFunct to be visited
     */
    V visitMathSingleFunct(MathSingleFunct<V> mathSingleFunct);

    /**
     * visit a {@link TextJoinFunct}.
     *
     * @param textJoinFunct to be visited
     */
    V visitTextJoinFunct(TextJoinFunct<V> textJoinFunct);

    /**
     * visit a {@link MethodVoid}.
     *
     * @param methodVoid to be visited
     */
    V visitMethodVoid(MethodVoid<V> methodVoid);

    /**
     * visit a {@link MethodReturn}.
     *
     * @param methodReturn to be visited
     */
    V visitMethodReturn(MethodReturn<V> methodReturn);

    /**
     * visit a {@link MethodIfReturn}.
     *
     * @param methodIfReturn to be visited
     */
    V visitMethodIfReturn(MethodIfReturn<V> methodIfReturn);

    /**
     * visit a {@link MethodStmt}.
     *
     * @param methodStmt to be visited
     */
    V visitMethodStmt(MethodStmt<V> methodStmt);

    /**
     * visit a {@link MethodCall}.
     *
     * @param methodStmt to be visited
     */
    V visitMethodCall(MethodCall<V> methodCall);

    /**
     * visit a {@link BluetoothRecieveAction}.
     *
     * @param bluetoothReceiveActionbluetoothReceiveAction to be visited
     */
    V visitBluetoothReceiveAction(BluetoothReceiveAction<V> bluetoothReceiveAction);

    /**
     * visit a {@link BluetoothConnectAction}.
     *
     * @param bluetoothConnectAction to be visited
     */
    V visitBluetoothConnectAction(BluetoothConnectAction<V> bluetoothConnectAction);

    /**
     * visit a {@link BluetoothSendAction}.
     *
     * @param bluetoothSendAction to be visited
     */
    V visitBluetoothSendAction(BluetoothSendAction<V> bluetoothSendAction);

    /**
     * visit a {@link BluetoothWaitForConnectionAction}.
     *
     * @param bluetoothWaitForConnection to be visited
     */
    V visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<V> bluetoothWaitForConnection);

    /**
     * visit a {@link StmtExpr}.
     *
     * @param stmtExpr to be visited
     */
    default V visitStmtExpr(StmtExpr<V> stmtExpr) {
        stmtExpr.getStmt().visit(this);
        return null;
    }

    /**
     * visit a {@link ShadowExpr}.
     *
     * @param shadowExpr to be visited
     */
    default V visitShadowExpr(ShadowExpr<V> shadowExpr) {
        if ( shadowExpr.getBlock() != null ) {
            shadowExpr.getBlock().visit(this);
        } else {
            shadowExpr.getShadow().visit(this);
        }
        return null;
    }

    /**
     * visit a {@link CompassSensor}.
     *
     * @param compassSensor to be visited
     */
    V visitCompassSensor(CompassSensor<V> compassSensor);

    /**
     * visit a {@link ConnectConst}.
     *
     * @param connectConst to be visited
     */
    V visitConnectConst(ConnectConst<V> connectConst);

    /**
     * visit a {@link BluetoothCheckConnectAction}.
     *
     * @param bluetoothCheckConnectAction to be visited
     */
    V visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<V> bluetoothCheckConnectAction);
}
