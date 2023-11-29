package de.fhg.iais.roberta.visitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EvalExpr;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.EvalStmts;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

public abstract class BaseVisitor<V> implements ILanguageVisitor<V> {

    /**
     * Delegates visitable to matching visitT(T t) method from substructure
     *
     * @param visitable meant to be visited
     * @return output of delegated visit-method
     */
    public final V visit(Phrase visitable) {
        try {
            boolean runPost = preVisitCheck(visitable);
            Method m = getVisitMethodFor(visitable.getClass());
            @SuppressWarnings("unchecked")
            V result = (V) m.invoke(this, new Object[] {visitable});
            if ( runPost ) {
                postVisitCheck(visitable);
            }
            return result;
        } catch ( NoSuchMethodException e ) {
            throw new DbcException(String.format("visit Method not found for phrase \"%s\"", visitable.getClass()), e);
        } catch ( IllegalAccessException e ) {
            throw new DbcException(e);
        } catch ( InvocationTargetException e ) {
            // rethrow cause
            Throwable cause = e.getCause();
            if ( cause instanceof RuntimeException ) {
                throw (RuntimeException) cause;
            }
            throw new DbcException(String.format("visit method for phrase \"%s\" threw exception", visitable.getClass()), e);
        }
    }

    private Method getVisitMethodFor(Class<?> clazz) throws NoSuchMethodException {
        Method m = null;
        try {
            return getClass().getMethod("visit", clazz);
        } catch ( NoSuchMethodException e ) {
            String methodName = "visit" + clazz.getSimpleName();
            return getClass().getMethod("visit" + clazz.getSimpleName(), clazz);
        }
    }

    /**
     * hook method to add functionality BEFORE a phrase is visited (e.g. to provide highlighting
     * in a debugger). The standard implementation does nothing.
     *
     * @param visitable the phrase to be visited
     * @return true, if the post check should run;false suppresses the post check
     */
    protected boolean preVisitCheck(Phrase visitable) {
        return true;
    }

    /**
     * hook method to add functionality AFTER a phrase has been visited (e.g. to provide highlighting
     * in a debugger). The standard implementation does nothing.
     *
     * @param visitable the phrase to be visited
     */
    protected void postVisitCheck(Phrase visitable) {
    }

    /**
     * delegates visiting to the embedded sensor.<br>
     * <b>TODO: rework! Cannot be final, because of problems with Edison</b>
     *
     * @param exprStmt
     * @return
     */
    public V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    public final V visitActionExpr(ActionExpr actionExpr) {
        return actionExpr.action.accept(this);
    }

    public final V visitActionStmt(ActionStmt actionStmt) {
        return actionStmt.action.accept(this);
    }

    /**
     * delegates visiting to the embedded expression.<br>
     * <b>TODO: rework! Cannot be final, because typechecking is not yet finalized</b>
     *
     * @param exprStmt
     * @return
     */
    public V visitEvalExpr(EvalExpr evalExpr) {
        return evalExpr.exprAsBlock.accept(this);
    }

    /**
     * delegates visiting to the embedded stmt list.<br>
     * <b>TODO: rework! Cannot be final, because typechecking is not yet finalized</b>
     *
     * @param exprStmt
     * @return
     */
    public V visitEvalStmts(EvalStmts evalStmts) {
        return evalStmts.stmtsAsBlock.accept(this);
    }

    /**
     * delegates visiting to the embedded expression.<br>
     * <b>Cannot be final, because code generators need to suffix the expr with ';' to achieve correct syntax</b>
     *
     * @param exprStmt
     * @return
     */
    public V visitExprStmt(ExprStmt exprStmt) {
        return exprStmt.expr.accept(this);
    }

    public final V visitFunctionExpr(FunctionExpr functionExpr) {
        return functionExpr.getFunction().accept(this);
    }

    /**
     * delegates visiting to the embedded function.<br>
     * <b>Cannot be final, because code generators need to suffix the expr with ';' to achieve correct syntax</b>
     *
     * @param exprStmt
     * @return
     */
    public V visitFunctionStmt(FunctionStmt functionStmt) {
        return functionStmt.function.accept(this);
    }

    public final V visitLocation(Location location) {
        return null;
    }

    public final V visitMethodExpr(MethodExpr methodExpr) {
        return methodExpr.getMethod().accept(this);
    }

    public final V visitSensorExpr(SensorExpr sensorExpr) {
        return sensorExpr.sensor.accept(this);
    }

    public final V visitSensorStmt(SensorStmt sensorStmt) {
        return sensorStmt.sensor.accept(this);
    }

    public final V visitStmtExpr(StmtExpr stmtExpr) {
        return stmtExpr.stmt.accept(this);
    }
}
