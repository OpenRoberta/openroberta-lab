package de.fhg.iais.roberta.codegen.lejos;

import de.fhg.iais.roberta.ast.syntax.action.ClearDisplayAction;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.PlayFileAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.syntax.action.StopAction;
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
 * All classes implementing {@link Visitor} interface should implement all methods
 * 
 * @author kcvejoski
 */
public interface Visitor {

    public void visit(NumConst numConst);

    public void visit(ActionExpr actionExpr);

    public void visit(Binary binary);

    public void visit(BoolConst boolConst);

    public void visit(ColorConst colorConst);

    public void visit(EmptyExpr emptyExpr);

    public void visit(ExprList exprList);

    public void visit(MathConst mathConst);

    public void visit(NullConst nullConst);

    public void visit(SensorExpr sensorExpr);

    public void visit(StringConst stringConst);

    public void visit(Unary unary);

    public void visit(Var var);

    public void visit(Funct funct);

    public void visit(BrickSensor brickSensor);

    public void visit(ColorSensor colorSensor);

    public void visit(EncoderSensor encoderSensor);

    public void visit(GyroSensor gyroSensor);

    public void visit(InfraredSensor infraredSensor);

    public void visit(TimerSensor timerSensor);

    public void visit(TouchSensor touchSensor);

    public void visit(UltrasonicSensor ultrasonicSensor);

    public void visit(ActionStmt actionStmt);

    public void visit(AssignStmt assignStmt);

    public void visit(ExprStmt exprStmt);

    public void visit(IfStmt ifStmt);

    public void visit(RepeatStmt repeatStmt);

    public void visit(SensorStmt sensorStmt);

    public void visit(StmtFlowCon stmtFlowCon);

    public void visit(StmtList stmtList);

    public void visit(VolumeAction volumeAction);

    public void visit(ClearDisplayAction clearDisplayAction);

    public void visit(DriveAction driveAction);

    public void visit(LightAction lightAction);

    public void visit(LightStatusAction lightStatusAction);

    public void visit(MotorGetPowerAction motorGetPowerAction);

    public void visit(MotorOnAction motorOnAction);

    public void visit(MotorSetPowerAction motorSetPowerAction);

    public void visit(MotorStopAction motorStopAction);

    public void visit(PlayFileAction playFileAction);

    public void visit(ShowPictureAction showPictureAction);

    public void visit(ShowTextAction showTextAction);

    public void visit(StopAction stopAction);

    public void visit(ToneAction toneAction);

    public void visit(TurnAction turnAction);

}
