package de.fhg.iais.roberta.codegen.lejos;

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
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.functions.Func;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.EncoderSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensor;
import de.fhg.iais.roberta.ast.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface Visitor<T> {

    /**
     * visit a {@link NumConst}.
     * 
     * @param numConst phrase to be visited
     */
    public T visitNumConst(NumConst numConst);

    /**
     * visit a {@link MathConst}.
     * 
     * @param mathConst to be visited
     */
    public T visitMathConst(MathConst mathConst);

    /**
     * visit a {@link BoolConst}.
     * 
     * @param boolConst to be visited
     */
    public T visitBoolConst(BoolConst boolConst);

    /**
     * visit a {@link StringConst}.
     * 
     * @param stringConst to be visited
     */
    public T visitStringConst(StringConst stringConst);

    /**
     * visit a {@link NullConst}.
     * 
     * @param nullConst to be visited
     */
    public T visitNullConst(NullConst nullConst);

    /**
     * visit a {@link ColorConst}.
     * 
     * @param colorConst to be visited
     */
    public T visitColorConst(ColorConst colorConst);

    /**
     * visit a {@link Var}.
     * 
     * @param var to be visited
     */
    public T visitVar(Var var);

    /**
     * visit a {@link Unary}.
     * 
     * @param unary to be visited
     */
    public T visitUnary(Unary unary);

    /**
     * visit a {@link Binary}.
     * 
     * @param binary to be visited
     */

    public T visitBinary(Binary binary);

    /**
     * visit a {@link Func}.
     * 
     * @param funct to be visited
     */
    public T visitFunc(Func func);

    /**
     * visit a {@link ActionExpr}.
     * 
     * @param actionExpr to be visited
     */
    public T visitActionExpr(ActionExpr actionExpr);

    /**
     * visit a {@link SensorExpr}.
     * 
     * @param sensorExpr to be visited
     */
    public T visitSensorExpr(SensorExpr sensorExpr);

    /**
     * visit a {@link EmptyExpr}.
     * 
     * @param emptyExpr to be visited
     */
    public T visitEmptyExpr(EmptyExpr emptyExpr);

    /**
     * visit a {@link ExprList}.
     * 
     * @param exprList to be visited
     */
    public T visitExprList(ExprList exprList);

    /**
     * visit a {@link ActionStmt}.
     * 
     * @param actionStmt to be visited
     */
    public T visitActionStmt(ActionStmt actionStmt);

    /**
     * visit a {@link AssignStmt}.
     * 
     * @param assignStmt to be visited
     */
    public T visitAssignStmt(AssignStmt assignStmt);

    /**
     * visit a {@link ExprStmt}.
     * 
     * @param exprStmt to be visited
     */
    public T visitExprStmt(ExprStmt exprStmt);

    /**
     * visit a {@link IfStmt}.
     * 
     * @param ifStmt to be visited
     */
    public T visitIfStmt(IfStmt ifStmt);

    /**
     * visit a {@link RepeatStmt}.
     * 
     * @param repeatStmt to be visited
     */
    public T visitRepeatStmt(RepeatStmt repeatStmt);

    /**
     * visit a {@link SensorStmt}.
     * 
     * @param sensorStmt to be visited
     */
    public T visitSensorStmt(SensorStmt sensorStmt);

    /**
     * visit a {@link StmtFlowCon}.
     * 
     * @param stmtFlowCon to be visited
     */
    public T visitStmtFlowCon(StmtFlowCon stmtFlowCon);

    /**
     * visit a {@link StmtList}.
     * 
     * @param stmtList to be visited
     */

    public T visitStmtList(StmtList stmtList);

    /**
     * visit a {@link DriveAction}.
     * 
     * @param driveAction to be visited
     */
    public T visitDriveAction(DriveAction driveAction);

    /**
     * visit a {@link TurnAction}.
     * 
     * @param turnAction to be visited
     */
    public T visitTurnAction(TurnAction turnAction);

    /**
     * visit a {@link LightAction}.
     * 
     * @param lightAction to be visited
     */
    public T visitLightAction(LightAction lightAction);

    /**
     * visit a {@link LightStatusAction}.
     * 
     * @param lightStatusAction to be visited
     */
    public T visitLightStatusAction(LightStatusAction lightStatusAction);

    /**
     * visit a {@link MotorGetPowerAction}.
     * 
     * @param motorGetPowerAction to be visited
     */
    public T visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction);

    /**
     * visit a {@link MotorOnAction}.
     * 
     * @param motorOnAction
     */
    public T visitMotorOnAction(MotorOnAction motorOnAction);

    /**
     * visit a {@link MotorSetPowerAction}.
     * 
     * @param motorSetPowerAction
     */
    public T visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction);

    /**
     * visit a {@link MotorStopAction}.
     * 
     * @param motorStopAction
     */
    public T visitMotorStopAction(MotorStopAction motorStopAction);

    /**
     * visit a {@link ClearDisplayAction}.
     * 
     * @param clearDisplayAction to be visited
     */
    public T visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    /**
     * visit a {@link VolumeAction}.
     * 
     * @param volumeAction to be visited
     */
    public T visitVolumeAction(VolumeAction volumeAction);

    /**
     * visit a {@link PlayFileAction}.
     * 
     * @param playFileAction
     */
    public T visitPlayFileAction(PlayFileAction playFileAction);

    /**
     * visit a {@link ShowPictureAction}.
     * 
     * @param showPictureAction
     */
    public T visitShowPictureAction(ShowPictureAction showPictureAction);

    /**
     * visit a {@link ShowTextAction}.
     * 
     * @param showTextAction
     */
    public T visitShowTextAction(ShowTextAction showTextAction);

    /**
     * visit a {@link MotorDriveStopAction}.
     * 
     * @param stopAction
     */
    public T visitMotorDriveStopAction(MotorDriveStopAction stopAction);

    /**
     * visit a {@link ToneAction}.
     * 
     * @param toneAction to be visited
     */
    public T visitToneAction(ToneAction toneAction);

    /**
     * visit a {@link BrickSensor}.
     * 
     * @param brickSensor to be visited
     */
    public T visitBrickSensor(BrickSensor brickSensor);

    /**
     * visit a {@link ColorSensor}.
     * 
     * @param colorSensor to be visited
     */
    public T visitColorSensor(ColorSensor colorSensor);

    /**
     * visit a {@link EncoderSensor}.
     * 
     * @param encoderSensor to be visited
     */
    public T visitEncoderSensor(EncoderSensor encoderSensor);

    /**
     * visit a {@link GyroSensor}.
     * 
     * @param gyroSensor to be visited
     */
    public T visitGyroSensor(GyroSensor gyroSensor);

    /**
     * visit a {@link InfraredSensor}.
     * 
     * @param infraredSensor to be visited
     */
    public T visitInfraredSensor(InfraredSensor infraredSensor);

    /**
     * visit a {@link TimerSensor}.
     * 
     * @param timerSensor to be visited
     */
    public T visitTimerSensor(TimerSensor timerSensor);

    /**
     * visit a {@link TouchSensor}.
     * 
     * @param touchSensor to be visited
     */
    public T visitTouchSensor(TouchSensor touchSensor);

    /**
     * visit a {@link UltrasonicSensor}.
     * 
     * @param ultrasonicSensor to be visited
     */
    public T visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

}
