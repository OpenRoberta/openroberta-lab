package de.fhg.iais.roberta.codegen.lejos;

import java.util.ArrayList;
import java.util.List;

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
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
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
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;

/**
 * This class is THE default implementation of {@link AstVisitor}. All methods are implemented empty ... and may be overwritten in subclasses
 */
public abstract class AstDefaultVisitorCombining<V> implements AstVisitor<V> {
    /**
     * initialize the Java code generator visitor.
     * 
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    AstDefaultVisitorCombining() {
    }

    /**
     * a default result must be supplied
     * 
     * @return the default
     */
    abstract public V defaultResult();

    /**
     * the aggregation of results must be supplied
     * 
     * @param vs the results
     * @return aggregation of two results
     * @return
     */
    abstract public V combine(V v1, V v2);

    /**
     * the aggregation of results must be supplied
     * 
     * @param vs the results
     * @return aggregation of two results
     * @return
     */
    abstract public V combine(V v1, V v2, V v3);

    /**
     * the aggregation of results must be supplied
     * 
     * @param vs the results
     * @return aggregation of two results
     * @return
     */
    abstract public V combine(List<V> vs);

    @Override
    public V visitNumConst(NumConst<V> numConst) {
        return defaultResult();
    }

    @Override
    public V visitMathConst(MathConst<V> mathConst) {
        return defaultResult();
    }

    @Override
    public V visitBoolConst(BoolConst<V> boolConst) {
        return defaultResult();
    }

    @Override
    public V visitStringConst(StringConst<V> stringConst) {
        return defaultResult();
    }

    @Override
    public V visitNullConst(NullConst<V> nullConst) {
        return defaultResult();
    }

    @Override
    public V visitColorConst(ColorConst<V> colorConst) {
        return defaultResult();
    }

    @Override
    public V visitVar(Var<V> var) {
        return defaultResult();
    }

    @Override
    public V visitUnary(Unary<V> unary) {
        return unary.getExpr().visit(this);
    }

    @Override
    public V visitBinary(Binary<V> binary) {
        V v1 = binary.getLeft().visit(this);
        V v2 = binary.getRight().visit(this);
        return combine(v1, v2);
    }

    @Override
    public V visitFunc(Func<V> func) {
        return defaultResult();
    }

    @Override
    public V visitActionExpr(ActionExpr<V> actionExpr) {
        return actionExpr.getAction().visit(this);
    }

    @Override
    public V visitSensorExpr(SensorExpr<V> sensorExpr) {
        return sensorExpr.getSens().visit(this);
    }

    @Override
    public V visitEmptyExpr(EmptyExpr<V> emptyExpr) {
        return defaultResult();
    }

    @Override
    public V visitExprList(ExprList<V> exprList) {
        List<V> vs = new ArrayList<>();
        for ( Expr<V> expr : exprList.get() ) {
            vs.add(expr.visit(this));
        }
        return combine(vs);
    }

    @Override
    public V visitActionStmt(ActionStmt<V> actionStmt) {
        return actionStmt.getAction().visit(this);
    }

    @Override
    public V visitAssignStmt(AssignStmt<V> assignStmt) {
        V v1 = assignStmt.getName().visit(this);
        V v2 = assignStmt.getExpr().visit(this);
        return combine(v1, v2);
    }

    @Override
    public V visitExprStmt(ExprStmt<V> exprStmt) {
        return exprStmt.getExpr().visit(this);
    }

    @Override
    public V visitIfStmt(IfStmt<V> ifStmt) {
        List<V> vs = new ArrayList<>();
        for ( Expr<V> expr : ifStmt.getExpr() ) {
            vs.add(expr.visit(this));
        }
        for ( StmtList<V> stmtl : ifStmt.getThenList() ) {
            vs.add(stmtl.visit(this));
        }
        vs.add(ifStmt.getElseList().visit(this));
        return combine(vs);
    }

    @Override
    public V visitRepeatStmt(RepeatStmt<V> repeatStmt) {
        V v1 = repeatStmt.getExpr().visit(this);
        V v2 = repeatStmt.getList().visit(this);
        return combine(v1, v2);
    }

    @Override
    public V visitSensorStmt(SensorStmt<V> sensorStmt) {
        return defaultResult();
    }

    @Override
    public V visitStmtFlowCon(StmtFlowCon<V> stmtFlowCon) {
        return defaultResult();
    }

    @Override
    public V visitStmtList(StmtList<V> stmtList) {
        List<V> vs = new ArrayList<>();
        for ( Stmt<V> stmt : stmtList.get() ) {
            vs.add(stmt.visit(this));
        }
        return combine(vs);
    }

    @Override
    public V visitDriveAction(DriveAction<V> driveAction) {
        V v1 = driveAction.getParam().getDuration().getValue().visit(this);
        V v2 = driveAction.getParam().getSpeed().visit(this);
        return combine(v1, v2);
    }

    @Override
    public V visitTurnAction(TurnAction<V> turnAction) {
        return turnAction.getParam().getDuration().getValue().visit(this);
    }

    @Override
    public V visitLightAction(LightAction<V> lightAction) {
        return defaultResult();
    }

    @Override
    public V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        return defaultResult();
    }

    @Override
    public V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        return defaultResult();
    }

    @Override
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        V v1 = motorOnAction.getParam().getDuration().getValue().visit(this);
        V v2 = motorOnAction.getParam().getSpeed().visit(this);
        return combine(v1, v2);
    }

    @Override
    public V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        return motorSetPowerAction.getPower().visit(this);
    }

    @Override
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        return defaultResult();
    }

    @Override
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        return defaultResult();
    }

    @Override
    public V visitVolumeAction(VolumeAction<V> volumeAction) {
        return volumeAction.getVolume().visit(this);
    }

    @Override
    public V visitPlayFileAction(PlayFileAction<V> playFileAction) {
        return defaultResult();
    }

    @Override
    public V visitShowPictureAction(ShowPictureAction<V> showPictureAction) {
        V v1 = showPictureAction.getX().visit(this);
        V v2 = showPictureAction.getY().visit(this);
        return combine(v1, v2);
    }

    @Override
    public V visitShowTextAction(ShowTextAction<V> showTextAction) {
        V v1 = showTextAction.getX().visit(this);
        V v2 = showTextAction.getY().visit(this);
        V v3 = showTextAction.getMsg().visit(this);
        return combine(v1, v2, v3);
    }

    @Override
    public V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction) {
        return defaultResult();
    }

    @Override
    public V visitToneAction(ToneAction<V> toneAction) {
        return defaultResult();
    }

    @Override
    public V visitBrickSensor(BrickSensor<V> brickSensor) {
        return defaultResult();
    }

    @Override
    public V visitColorSensor(ColorSensor<V> colorSensor) {
        return defaultResult();
    }

    @Override
    public V visitEncoderSensor(EncoderSensor<V> encoderSensor) {
        return defaultResult();
    }

    @Override
    public V visitGyroSensor(GyroSensor<V> gyroSensor) {
        return defaultResult();
    }

    @Override
    public V visitInfraredSensor(InfraredSensor<V> infraredSensor) {
        return defaultResult();
    }

    @Override
    public V visitTimerSensor(TimerSensor<V> timerSensor) {
        return defaultResult();
    }

    @Override
    public V visitTouchSensor(TouchSensor<V> touchSensor) {
        return defaultResult();
    }

    @Override
    public V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor) {
        return defaultResult();
    }
}