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
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;

public class TestLoopCounterVisitor extends CommonNepoValidatorAndCollectorVisitor {
    public TestLoopCounterVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) //
    {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitActionExpr(ActionExpr<Void> actionExpr) {
        return super.visitActionExpr(actionExpr);
    }

    @Override
    public Void visitActionStmt(ActionStmt<Void> actionStmt) {
        return super.visitActionStmt(actionStmt);
    }

    @Override
    public Void visitActivityTask(ActivityTask<Void> activityTask) {
        return super.visitActivityTask(activityTask);
    }

    @Override
    public Void visitEvalExpr(EvalExpr<Void> evalExpr) {
        return super.visitEvalExpr(evalExpr);
    }

    @Override
    public Void visitExprStmt(ExprStmt<Void> exprStmt) {
        return super.visitExprStmt(exprStmt);
    }

    @Override
    public Void visitFunctionExpr(FunctionExpr<Void> functionExpr) {
        return super.visitFunctionExpr(functionExpr);
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt<Void> functionStmt) {
        return super.visitFunctionStmt(functionStmt);
    }

    @Override
    public Void visitLocation(Location<Void> location) {
        return super.visitLocation(location);
    }

    @Override
    public Void visitMethodExpr(MethodExpr<Void> methodExpr) {
        return super.visitMethodExpr(methodExpr);
    }

    @Override
    public Void visitSensorExpr(SensorExpr<Void> sensorExpr) {
        return super.visitSensorExpr(sensorExpr);
    }

    @Override
    public Void visitSensorStmt(SensorStmt<Void> sensorStmt) {
        return super.visitSensorStmt(sensorStmt);
    }

    @Override
    public Void visitShadowExpr(ShadowExpr<Void> shadowExpr) {
        return super.visitShadowExpr(shadowExpr);
    }

    @Override
    public Void visitStartActivityTask(StartActivityTask<Void> startActivityTask) {
        return super.visitStartActivityTask(startActivityTask);
    }

    @Override
    public Void visitStmtExpr(StmtExpr<Void> stmtExpr) {
        return super.visitStmtExpr(stmtExpr);
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        return null;
    }
}
