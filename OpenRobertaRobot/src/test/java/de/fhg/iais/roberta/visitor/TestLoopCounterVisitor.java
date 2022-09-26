package de.fhg.iais.roberta.visitor;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.lang.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EvalExpr;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;

public class TestLoopCounterVisitor extends CommonNepoValidatorAndCollectorVisitor {
    public TestLoopCounterVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) //
    {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitActionExpr(ActionExpr actionExpr) {
        return super.visitActionExpr(actionExpr);
    }

    @Override
    public Void visitActionStmt(ActionStmt actionStmt) {
        return super.visitActionStmt(actionStmt);
    }

    @Override
    public Void visitActivityTask(ActivityTask activityTask) {
        return super.visitActivityTask(activityTask);
    }

    @Override
    public Void visitEvalExpr(EvalExpr evalExpr) {
        return super.visitEvalExpr(evalExpr);
    }

    @Override
    public Void visitExprStmt(ExprStmt exprStmt) {
        return super.visitExprStmt(exprStmt);
    }

    @Override
    public Void visitFunctionExpr(FunctionExpr functionExpr) {
        return super.visitFunctionExpr(functionExpr);
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt functionStmt) {
        return super.visitFunctionStmt(functionStmt);
    }

    @Override
    public Void visitLocation(Location location) {
        return super.visitLocation(location);
    }

    @Override
    public Void visitMethodExpr(MethodExpr methodExpr) {
        return super.visitMethodExpr(methodExpr);
    }

    @Override
    public Void visitSensorExpr(SensorExpr sensorExpr) {
        return super.visitSensorExpr(sensorExpr);
    }

    @Override
    public Void visitSensorStmt(SensorStmt sensorStmt) {
        return super.visitSensorStmt(sensorStmt);
    }

    @Override
    public Void visitShadowExpr(ShadowExpr shadowExpr) {
        return super.visitShadowExpr(shadowExpr);
    }

    @Override
    public Void visitStartActivityTask(StartActivityTask startActivityTask) {
        return super.visitStartActivityTask(startActivityTask);
    }

    @Override
    public Void visitStmtExpr(StmtExpr stmtExpr) {
        return super.visitStmtExpr(stmtExpr);
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        return null;
    }
}
