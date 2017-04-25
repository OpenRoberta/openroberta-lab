package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.syntax.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.stmt.SensorStmt;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface AstVisitor<V> {
    static final String INDENT = "    ";

    default V visitSensorExpr(SensorExpr<V> sensorExpr) {
        sensorExpr.getSens().visit(this);
        return null;
    }

    default V visitMethodExpr(MethodExpr<V> methodExpr) {
        methodExpr.getMethod().visit(this);
        return null;
    }

    default V visitActionStmt(ActionStmt<V> actionStmt) {
        actionStmt.getAction().visit(this);
        return null;
    }

    /**
     * visit a {@link ActionExpr}.
     *
     * @param actionExpr to be visited
     */
    default V visitActionExpr(ActionExpr<V> actionExpr) {
        actionExpr.getAction().visit(this);
        return null;
    }

    default V visitExprStmt(ExprStmt<V> exprStmt) {
        exprStmt.getExpr().visit(this);
        return null;
    }

    default V visitStmtExpr(StmtExpr<V> stmtExpr) {
        stmtExpr.getStmt().visit(this);
        return null;
    }

    default V visitShadowExpr(ShadowExpr<V> shadowExpr) {
        if ( shadowExpr.getBlock() != null ) {
            shadowExpr.getBlock().visit(this);
        } else {
            shadowExpr.getShadow().visit(this);
        }
        return null;
    }

    default V visitSensorStmt(SensorStmt<V> sensorStmt) {
        sensorStmt.getSensor().visit(this);
        return null;
    }

    /**
     * visit a {@link ActivityTask}.
     *
     * @param activityTask to be visited
     */
    default V visitActivityTask(ActivityTask<V> activityTask) {
        return null;
    }

    default V visitStartActivityTask(StartActivityTask<V> startActivityTask) {
        return null;
    }

    default V visitLocation(Location<V> location) {
        return null;
    }

    default V visitFunctionStmt(FunctionStmt<V> functionStmt) {
        functionStmt.getFunction().visit(this);
        return null;
    }

    default V visitFunctionExpr(FunctionExpr<V> functionExpr) {
        functionExpr.getFunction().visit(this);
        return null;
    }
}
