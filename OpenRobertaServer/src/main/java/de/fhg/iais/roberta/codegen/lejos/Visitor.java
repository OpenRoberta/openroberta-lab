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
import de.fhg.iais.roberta.ast.syntax.functions.Funct;
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
 * Main interface used to implement visitor pattern to traverse AST and generate code.
 * Class which implements this interface should implement all methods to generate correct code in the desired programming language.
 * 
 * @author kcvejoski
 */
public interface Visitor {

    /**
     * append a human-readable code representation of a {@link NumConst} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link NumConst}.
     * 
     * @param numConst phrase from which there will be generated code
     */
    public void visit(NumConst numConst);

    /**
     * append a human-readable code representation of a {@link MathConst} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link MathConst}.
     * 
     * @param mathConst phrase from which there will be generated code
     */
    public void visit(MathConst mathConst);

    /**
     * append a human-readable code representation of a {@link BoolConst} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link BoolConst}.
     * 
     * @param boolConst phrase from which there will be generated code
     */
    public void visit(BoolConst boolConst);

    /**
     * append a human-readable code representation of a {@link StringConst} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link StringConst}.
     * 
     * @param stringConst phrase from which there will be generated code
     */
    public void visit(StringConst stringConst);

    /**
     * append a human-readable code representation of a {@link NullConst} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link NullConst}.
     * 
     * @param nullConst phrase from which there will be generated code
     */
    public void visit(NullConst nullConst);

    /**
     * append a human-readable code representation of a {@link ColorConst} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link ColorConst}.
     * 
     * @param colorConst phrase from which there will be generated code
     */
    public void visit(ColorConst colorConst);

    /**
     * append a human-readable code representation of a {@link Var} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link Var}.
     * 
     * @param var phrase from which there will be generated code
     */
    public void visit(Var var);

    /**
     * append a human-readable code representation of a {@link Unary} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link Unary}.
     * 
     * @param unary phrase from which there will be generated code
     */
    public void visit(Unary unary);

    /**
     * append a human-readable code representation of a {@link Binary} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link Binary}.
     * 
     * @param binary phrase from which there will be generated code
     */

    public void visit(Binary binary);

    /**
     * append a human-readable code representation of a {@link Funct} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link Funct}.
     * 
     * @param funct phrase from which there will be generated code
     */
    public void visit(Funct funct);

    /**
     * append a human-readable code representation of a {@link ActionExpr} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link ActionExpr}.
     * 
     * @param actionExpr phrase from which there will be generated code
     */
    public void visit(ActionExpr actionExpr);

    /**
     * append a human-readable code representation of a {@link SensorExpr} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link SensorExpr}.
     * 
     * @param sensorExpr phrase from which there will be generated code
     */
    public void visit(SensorExpr sensorExpr);

    /**
     * append a human-readable code representation of a {@link EmptyExpr} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link EmptyExpr}.
     * 
     * @param emptyExpr phrase from which there will be generated code
     */
    public void visit(EmptyExpr emptyExpr);

    /**
     * append a human-readable code representation of a {@link ExprList} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link ExprList}.
     * 
     * @param exprList phrase from which there will be generated code
     */
    public void visit(ExprList exprList);

    /**
     * append a human-readable code representation of a {@link ActionStmt} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link ActionStmt}.
     * 
     * @param actionStmt phrase from which there will be generated code
     */
    public void visit(ActionStmt actionStmt);

    /**
     * append a human-readable code representation of a {@link AssignStmt} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link AssignStmt}.
     * 
     * @param assignStmt phrase from which there will be generated code
     */
    public void visit(AssignStmt assignStmt);

    /**
     * append a human-readable code representation of a {@link ExprStmt} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link ExprStmt}.
     * 
     * @param exprStmt phrase from which there will be generated code
     */
    public void visit(ExprStmt exprStmt);

    /**
     * append a human-readable code representation of a {@link IfStmt} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link IfStmt}.
     * 
     * @param ifStmt phrase from which there will be generated code
     */
    public void visit(IfStmt ifStmt);

    /**
     * append a human-readable code representation of a {@link RepeatStmt} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link RepeatStmt}.
     * 
     * @param repeatStmt phrase from which there will be generated code
     */
    public void visit(RepeatStmt repeatStmt);

    /**
     * append a human-readable code representation of a {@link SensorStmt} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link SensorStmt}.
     * 
     * @param sensorStmt phrase from which there will be generated code
     */
    public void visit(SensorStmt sensorStmt);

    /**
     * append a human-readable code representation of a {@link StmtFlowCon} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link StmtFlowCon}.
     * 
     * @param stmtFlowCon phrase from which there will be generated code
     */
    public void visit(StmtFlowCon stmtFlowCon);

    /**
     * append a human-readable code representation of a {@link StmtList} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link StmtList}.
     * 
     * @param stmtList phrase from which there will be generated code
     */

    public void visit(StmtList stmtList);

    /**
     * append a human-readable code representation of a {@link DriveAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link DriveAction}.
     * 
     * @param driveAction phrase from which there will be generated code
     */
    public void visit(DriveAction driveAction);

    /**
     * append a human-readable code representation of a {@link TurnAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link TurnAction}.
     * 
     * @param turnAction phrase from which there will be generated code
     */
    public void visit(TurnAction turnAction);

    /**
     * append a human-readable code representation of a {@link LightAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link LightAction}.
     * 
     * @param lightAction phrase from which there will be generated code
     */
    public void visit(LightAction lightAction);

    /**
     * append a human-readable code representation of a {@link LightStatusAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link LightStatusAction}.
     * 
     * @param lightStatusAction phrase from which there will be generated code
     */
    public void visit(LightStatusAction lightStatusAction);

    /**
     * append a human-readable code representation of a {@link MotorGetPowerAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link MotorGetPowerAction}.
     * 
     * @param motorGetPowerAction phrase from which there will be generated code
     */
    public void visit(MotorGetPowerAction motorGetPowerAction);

    /**
     * append a human-readable code representation of a {@link MotorOnAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link MotorOnAction}.
     * 
     * @param motorOnAction
     */
    public void visit(MotorOnAction motorOnAction);

    /**
     * append a human-readable code representation of a {@link MotorSetPowerAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link MotorSetPowerAction}.
     * 
     * @param motorSetPowerAction
     */
    public void visit(MotorSetPowerAction motorSetPowerAction);

    /**
     * append a human-readable code representation of a {@link MotorStopAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link MotorStopAction}.
     * 
     * @param motorStopAction
     */
    public void visit(MotorStopAction motorStopAction);

    /**
     * append a human-readable code representation of a {@link ClearDisplayAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link ClearDisplayAction}.
     * 
     * @param clearDisplayAction phrase from which there will be generated code
     */
    public void visit(ClearDisplayAction clearDisplayAction);

    /**
     * append a human-readable code representation of a {@link VolumeAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link VolumeAction}.
     * 
     * @param volumeAction phrase from which there will be generated code
     */
    public void visit(VolumeAction volumeAction);

    /**
     * append a human-readable code representation of a {@link PlayFileAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link PlayFileAction}.
     * 
     * @param playFileAction
     */
    public void visit(PlayFileAction playFileAction);

    /**
     * append a human-readable code representation of a {@link ShowPictureAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link ShowPictureAction}.
     * 
     * @param showPictureAction
     */
    public void visit(ShowPictureAction showPictureAction);

    /**
     * append a human-readable code representation of a {@link ShowTextAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link ShowTextAction}.
     * 
     * @param showTextAction
     */
    public void visit(ShowTextAction showTextAction);

    /**
     * append a human-readable code representation of a {@link MotorDriveStopAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link MotorDriveStopAction}.
     * 
     * @param stopAction
     */
    public void visit(MotorDriveStopAction stopAction);

    /**
     * append a human-readable code representation of a {@link ToneAction} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link ToneAction}.
     * 
     * @param toneAction phrase from which there will be generated code
     */
    public void visit(ToneAction toneAction);

    /**
     * append a human-readable code representation of a {@link BrickSensor} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link BrickSensor}.
     * 
     * @param brickSensor phrase from which there will be generated code
     */
    public void visit(BrickSensor brickSensor);

    /**
     * append a human-readable code representation of a {@link ColorSensor} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link ColorSensor}.
     * 
     * @param colorSensor phrase from which there will be generated code
     */
    public void visit(ColorSensor colorSensor);

    /**
     * append a human-readable code representation of a {@link EncoderSensor} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link EncoderSensor}.
     * 
     * @param encoderSensor phrase from which there will be generated code
     */
    public void visit(EncoderSensor encoderSensor);

    /**
     * append a human-readable code representation of a {@link GyroSensor} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link GyroSensor}.
     * 
     * @param gyroSensor phrase from which there will be generated code
     */
    public void visit(GyroSensor gyroSensor);

    /**
     * append a human-readable code representation of a {@link InfraredSensor} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link InfraredSensor}.
     * 
     * @param infraredSensor phrase from which there will be generated code
     */
    public void visit(InfraredSensor infraredSensor);

    /**
     * append a human-readable code representation of a {@link TimerSensor} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link TimerSensor}.
     * 
     * @param timerSensor phrase from which there will be generated code
     */
    public void visit(TimerSensor timerSensor);

    /**
     * append a human-readable code representation of a {@link TouchSensor} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link TouchSensor}.
     * 
     * @param touchSensor phrase from which there will be generated code
     */
    public void visit(TouchSensor touchSensor);

    /**
     * append a human-readable code representation of a {@link UltrasonicSensor} phrase to a StringBuilder. <b>This representation MUST be correct code.</b> <br>
     * <br>
     * Client must provide valid object of the class {@link UltrasonicSensor}.
     * 
     * @param ultrasonicSensor phrase from which there will be generated code
     */
    public void visit(UltrasonicSensor ultrasonicSensor);

}
