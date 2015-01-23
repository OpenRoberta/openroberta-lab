package de.fhg.iais.roberta.ast.visitor;

import de.fhg.iais.roberta.ast.syntax.action.ClearDisplayAction;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorDriveStopAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.PlayFileAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.syntax.action.ToneAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.action.VolumeAction;
import de.fhg.iais.roberta.ast.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.ColorConst;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyList;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.FunctionExpr;
import de.fhg.iais.roberta.ast.syntax.expr.ListCreate;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.ast.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.ast.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.ast.syntax.functions.LenghtOfIsEmptyFunct;
import de.fhg.iais.roberta.ast.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.ast.syntax.functions.ListRepeat;
import de.fhg.iais.roberta.ast.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.ast.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathSingleFunct;
import de.fhg.iais.roberta.ast.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.ast.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.ast.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.ast.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.EncoderSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GetSampleSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensor;
import de.fhg.iais.roberta.ast.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.FunctionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.ast.syntax.tasks.ActivityTask;
import de.fhg.iais.roberta.ast.syntax.tasks.Location;
import de.fhg.iais.roberta.ast.syntax.tasks.MainTask;
import de.fhg.iais.roberta.ast.syntax.tasks.StartActivityTask;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface AstVisitor<V> {

    /**
     * visit a {@link NumConst}.
     *
     * @param numConst phrase to be visited
     */
    public V visitNumConst(NumConst<V> numConst);

    /**
     * visit a {@link MathConst}.
     *
     * @param mathConst to be visited
     */
    public V visitMathConst(MathConst<V> mathConst);

    /**
     * visit a {@link BoolConst}.
     *
     * @param boolConst to be visited
     */
    public V visitBoolConst(BoolConst<V> boolConst);

    /**
     * visit a {@link StringConst}.
     *
     * @param stringConst to be visited
     */
    public V visitStringConst(StringConst<V> stringConst);

    /**
     * visit a {@link NullConst}.
     *
     * @param nullConst to be visited
     */
    public V visitNullConst(NullConst<V> nullConst);

    /**
     * visit a {@link ColorConst}.
     *
     * @param colorConst to be visited
     */
    public V visitColorConst(ColorConst<V> colorConst);

    /**
     * visit a {@link Var}.
     *
     * @param var to be visited
     */
    public V visitVar(Var<V> var);

    /**
     * visit a {@link VarDeclaration}.
     *
     * @param var to be visited
     */
    public V visitVarDeclaration(VarDeclaration<V> var);

    /**
     * visit a {@link Unary}.
     *
     * @param unary to be visited
     */
    public V visitUnary(Unary<V> unary);

    /**
     * visit a {@link Binary}.
     *
     * @param binary to be visited
     */

    public V visitBinary(Binary<V> binary);

    /**
     * visit a {@link MathPowerFunct}.
     *
     * @param funct to be visited
     */
    public V visitFunc(MathPowerFunct<V> func);

    /**
     * visit a {@link ActionExpr}.
     *
     * @param actionExpr to be visited
     */
    public V visitActionExpr(ActionExpr<V> actionExpr);

    /**
     * visit a {@link SensorExpr}.
     *
     * @param sensorExpr to be visited
     */
    public V visitSensorExpr(SensorExpr<V> sensorExpr);

    /**
     * visit a {@link EmptyList}.
     *
     * @param sensorExpr to be visited
     */
    public V visitEmptyList(EmptyList<V> emptyList);

    /**
     * visit a {@link EmptyExpr}.
     *
     * @param emptyExpr to be visited
     */
    public V visitEmptyExpr(EmptyExpr<V> emptyExpr);

    /**
     * visit a {@link ExprList}.
     *
     * @param exprList to be visited
     */
    public V visitExprList(ExprList<V> exprList);

    /**
     * visit a {@link ActionStmt}.
     *
     * @param actionStmt to be visited
     */
    public V visitActionStmt(ActionStmt<V> actionStmt);

    /**
     * visit a {@link AssignStmt}.
     *
     * @param assignStmt to be visited
     */
    public V visitAssignStmt(AssignStmt<V> assignStmt);

    /**
     * visit a {@link ExprStmt}.
     *
     * @param exprStmt to be visited
     */
    public V visitExprStmt(ExprStmt<V> exprStmt);

    /**
     * visit a {@link IfStmt}.
     *
     * @param ifStmt to be visited
     */
    public V visitIfStmt(IfStmt<V> ifStmt);

    /**
     * visit a {@link RepeatStmt}.
     *
     * @param repeatStmt to be visited
     */
    public V visitRepeatStmt(RepeatStmt<V> repeatStmt);

    /**
     * visit a {@link SensorStmt}.
     *
     * @param sensorStmt to be visited
     */
    public V visitSensorStmt(SensorStmt<V> sensorStmt);

    /**
     * visit a {@link StmtFlowCon}.
     *
     * @param stmtFlowCon to be visited
     */
    public V visitStmtFlowCon(StmtFlowCon<V> stmtFlowCon);

    /**
     * visit a {@link StmtList}.
     *
     * @param stmtList to be visited
     */

    public V visitStmtList(StmtList<V> stmtList);

    /**
     * visit a {@link DriveAction}.
     *
     * @param driveAction to be visited
     */
    public V visitDriveAction(DriveAction<V> driveAction);

    /**
     * visit a {@link TurnAction}.
     *
     * @param turnAction to be visited
     */
    public V visitTurnAction(TurnAction<V> turnAction);

    /**
     * visit a {@link LightAction}.
     *
     * @param lightAction to be visited
     */
    public V visitLightAction(LightAction<V> lightAction);

    /**
     * visit a {@link LightStatusAction}.
     *
     * @param lightStatusAction to be visited
     */
    public V visitLightStatusAction(LightStatusAction<V> lightStatusAction);

    /**
     * visit a {@link MotorGetPowerAction}.
     *
     * @param motorGetPowerAction to be visited
     */
    public V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction);

    /**
     * visit a {@link MotorOnAction}.
     *
     * @param motorOnAction
     */
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction);

    /**
     * visit a {@link MotorSetPowerAction}.
     *
     * @param motorSetPowerAction
     */
    public V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction);

    /**
     * visit a {@link MotorStopAction}.
     *
     * @param motorStopAction
     */
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction);

    /**
     * visit a {@link ClearDisplayAction}.
     *
     * @param clearDisplayAction to be visited
     */
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction);

    /**
     * visit a {@link VolumeAction}.
     *
     * @param volumeAction to be visited
     */
    public V visitVolumeAction(VolumeAction<V> volumeAction);

    /**
     * visit a {@link PlayFileAction}.
     *
     * @param playFileAction
     */
    public V visitPlayFileAction(PlayFileAction<V> playFileAction);

    /**
     * visit a {@link ShowPictureAction}.
     *
     * @param showPictureAction
     */
    public V visitShowPictureAction(ShowPictureAction<V> showPictureAction);

    /**
     * visit a {@link ShowTextAction}.
     *
     * @param showTextAction
     */
    public V visitShowTextAction(ShowTextAction<V> showTextAction);

    /**
     * visit a {@link MotorDriveStopAction}.
     *
     * @param stopAction
     */
    public V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction);

    /**
     * visit a {@link ToneAction}.
     *
     * @param toneAction to be visited
     */
    public V visitToneAction(ToneAction<V> toneAction);

    /**
     * visit a {@link BrickSensor}.
     *
     * @param brickSensor to be visited
     */
    public V visitBrickSensor(BrickSensor<V> brickSensor);

    /**
     * visit a {@link ColorSensor}.
     *
     * @param colorSensor to be visited
     */
    public V visitColorSensor(ColorSensor<V> colorSensor);

    /**
     * visit a {@link EncoderSensor}.
     *
     * @param encoderSensor to be visited
     */
    public V visitEncoderSensor(EncoderSensor<V> encoderSensor);

    /**
     * visit a {@link GyroSensor}.
     *
     * @param gyroSensor to be visited
     */
    public V visitGyroSensor(GyroSensor<V> gyroSensor);

    /**
     * visit a {@link InfraredSensor}.
     *
     * @param infraredSensor to be visited
     */
    public V visitInfraredSensor(InfraredSensor<V> infraredSensor);

    /**
     * visit a {@link TimerSensor}.
     *
     * @param timerSensor to be visited
     */
    public V visitTimerSensor(TimerSensor<V> timerSensor);

    /**
     * visit a {@link TouchSensor}.
     *
     * @param touchSensor to be visited
     */
    public V visitTouchSensor(TouchSensor<V> touchSensor);

    /**
     * visit a {@link UltrasonicSensor}.
     *
     * @param ultrasonicSensor to be visited
     */
    public V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor);

    /**
     * visit a {@link GetSampleSensor}.
     *
     * @param sensorGetSample to be visited
     */
    public V visitGetSampleSensor(GetSampleSensor<V> sensorGetSample);

    /**
     * visit a {@link MainTask}.
     *
     * @param MainTask to be visited
     */
    public V visitMainTask(MainTask<V> mainTask);

    /**
     * visit a {@link ActivityTask}.
     *
     * @param activityTask to be visited
     */
    public V visitActivityTask(ActivityTask<V> activityTask);

    /**
     * visit a {@link StartActivityTask}.
     *
     * @param startActivityTask to be visited
     */
    public V visitStartActivityTask(StartActivityTask<V> startActivityTask);

    /**
     * visit a {@link WaitStmt}.
     *
     * @param waitStmt to be visited
     */
    public V visitWaitStmt(WaitStmt<V> waitStmt);

    /**
     * visit a {@link WaitTimeStmt}.
     *
     * @param waitStmt to be visited
     */
    public V visitWaitTimeStmt(WaitTimeStmt<V> waitTimeStmt);

    /**
     * visit a {@link Location}.
     *
     * @param location to be visited
     */
    public V visitLocation(Location<V> location);

    /**
     * visit a {@link TextPrintFunct}.
     *
     * @param textPrintFunct to be visited
     */
    public V visitTextPrintFunct(TextPrintFunct<V> textPrintFunct);

    /**
     * visit a {@link FunctionStmt}.
     *
     * @param functionStmt to be visited
     */
    public V visitFunctionStmt(FunctionStmt<V> functionStmt);

    /**
     * visit a {@link FunctionExpr}.
     *
     * @param functionExpr to be visited
     */
    public V visitFunctionExpr(FunctionExpr<V> functionExpr);

    /**
     * visit a {@link GetSubFunct}.
     *
     * @param getSubFunct to be visited
     */
    public V visitGetSubFunct(GetSubFunct<V> getSubFunct);

    /**
     * visit a {@link IndexOfFunct}.
     *
     * @param indexOfFunct to be visited
     */
    public V visitIndexOfFunct(IndexOfFunct<V> indexOfFunct);

    /**
     * visit a {@link LenghtOfIsEmptyFunct}.
     *
     * @param lenghtOfIsEmptyFunct to be visited
     */
    public V visitLenghtOfIsEmptyFunct(LenghtOfIsEmptyFunct<V> lenghtOfIsEmptyFunct);

    /**
     * visit a {@link ListCreate}.
     *
     * @param listCreate to be visited
     */
    public V visitListCreate(ListCreate<V> listCreate);

    /**
     * visit a {@link ListGetIndex}.
     *
     * @param listGetIndex to be visited
     */
    public V visitListGetIndex(ListGetIndex<V> listGetIndex);

    /**
     * visit a {@link ListRepeat}.
     *
     * @param listRepeat to be visited
     */
    public V visitListRepeat(ListRepeat<V> listRepeat);

    /**
     * visit a {@link ListSetIndex}.
     *
     * @param listSetIndex to be visited
     */
    public V visitListSetIndex(ListSetIndex<V> listSetIndex);

    /**
     * visit a {@link MathConstrainFunct}.
     *
     * @param mathConstrainFunct to be visited
     */
    public V visitMathConstrainFunct(MathConstrainFunct<V> mathConstrainFunct);

    /**
     * visit a {@link MathNumPropFunct}.
     *
     * @param mathNumPropFunct to be visited
     */
    public V visitMathNumPropFunct(MathNumPropFunct<V> mathNumPropFunct);

    /**
     * visit a {@link MathOnListFunct}.
     *
     * @param mathOnListFunct to be visited
     */
    public V visitMathOnListFunct(MathOnListFunct<V> mathOnListFunct);

    /**
     * visit a {@link MathRandomFloatFunct}.
     *
     * @param mathOnListFunct to be visited
     */
    public V visitMathRandomFloatFunct(MathRandomFloatFunct<V> mathRandomFloatFunct);

    /**
     * visit a {@link MathRandomIntFunct}.
     *
     * @param mathRandomIntFunct to be visited
     */
    public V visitMathRandomIntFunct(MathRandomIntFunct<V> mathRandomIntFunct);

    /**
     * visit a {@link MathSingleFunct}.
     *
     * @param mathSingleFunct to be visited
     */
    public V visitMathSingleFunct(MathSingleFunct<V> mathSingleFunct);

    /**
     * visit a {@link TextJoinFunct}.
     *
     * @param textJoinFunct to be visited
     */
    public V visitTextJoinFunct(TextJoinFunct<V> textJoinFunct);

    /**
     * visit a {@link MethodVoid}.
     *
     * @param textJoinFunct to be visited
     */
    public V visitMethodVoid(MethodVoid<V> methodVoid);

    /**
     * visit a {@link MethodReturn}.
     *
     * @param textJoinFunct to be visited
     */
    public V visitMethodReturn(MethodReturn<V> methodReturn);
}
