package de.fhg.iais.roberta.visitor.lang;

import de.fhg.iais.roberta.syntax.lang.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.EvalExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.visitor.IVisitor;

public interface ILanguageVisitor<V> extends IVisitor<V> {

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
     * visit a {@link RgbColor}.
     *
     * @param rgbColor to be visited
     */
    V visitRgbColor(RgbColor<V> rgbColor);

    /**
     * visit a {@link StmtTextComment}.
     *
     * @param stmtTextComment to be visited
     */
    V visitStmtTextComment(StmtTextComment<V> stmtTextComment);

    /**
     * visit a {@link ConnectConst}.
     *
     * @param connectConst to be visited
     */
    V visitConnectConst(ConnectConst<V> connectConst);

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
     * visit a {@link EmptyList}.
     *
     * @param sensorExpr to be visited
     */
    V visitEmptyList(EmptyList<V> emptyList);

    /**
     * visit a {@link AssignStmt}.
     *
     * @param assignStmt to be visited
     */
    V visitAssignStmt(AssignStmt<V> assignStmt);

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
     * visit a {@link IfStmt}.
     *
     * @param ifStmt to be visited
     */
    V visitIfStmt(IfStmt<V> ifStmt);

    /**
     * visit a {@link NNStepStmt}.
     *
     * @param nnStepStmt to be visited
     */
    V visitNNStepStmt(NNStepStmt<V> nnStepStmt);

    /**
     * visit a {@link RepeatStmt}.
     *
     * @param repeatStmt to be visited
     */
    V visitRepeatStmt(RepeatStmt<V> repeatStmt);

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
     * visit a {@link MainTask}.
     *
     * @param MainTask to be visited
     */
    V visitMainTask(MainTask<V> mainTask);

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
     * visit a {@link TextPrintFunct}.
     *
     * @param textPrintFunct to be visited
     */
    V visitTextPrintFunct(TextPrintFunct<V> textPrintFunct);

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
     * visit a {@link MathCastStringFunct}.
     *
     * @param mathCastStringFunct to be visited
     */
    V visitMathCastStringFunct(MathCastStringFunct<V> mathCastStringFunct);

    /**
     * visit a {@link MathCastCharFunct}.
     *
     * @param mathCastCharFunct to be visited
     */
    V visitMathCastCharFunct(MathCastCharFunct<V> mathCastCharFunct);

    /**
     * visit a {@link TextCharCastNumberFunct}.
     *
     * @param textCharCastNumberFunct to be visited
     */
    V visitTextCharCastNumberFunct(TextCharCastNumberFunct<V> textCharCastNumberFunct);

    /**
     * visit a {@link TextStringCastNumberFunct}.
     *
     * @param textStringCastNumberFunct to be visited
     */
    V visitTextStringCastNumberFunct(TextStringCastNumberFunct<V> textStringCastNumberFunct);

    /**
     * visit a {@link TextJoinFunct}.
     *
     * @param textJoinFunct to be visited
     */
    V visitTextJoinFunct(TextJoinFunct<V> textJoinFunct);

    /**
     * visit a {@link MethodVoid}. This is a declaration of a void method (not a call of the method)
     *
     * @param methodVoid to be visited
     */
    V visitMethodVoid(MethodVoid<V> methodVoid);

    /**
     * visit a {@link MethodReturn}. This is a declaration of a method with a return value (not a call of the method)
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

    default V visitSensorExpr(SensorExpr<V> sensorExpr) {
        sensorExpr.getSens().accept(this);
        return null;
    }

    default V visitMethodExpr(MethodExpr<V> methodExpr) {
        methodExpr.getMethod().accept(this);
        return null;
    }

    default V visitActionStmt(ActionStmt<V> actionStmt) {
        actionStmt.getAction().accept(this);
        return null;
    }

    default V visitActionExpr(ActionExpr<V> actionExpr) {
        actionExpr.getAction().accept(this);
        return null;
    }

    default V visitExprStmt(ExprStmt<V> exprStmt) {
        exprStmt.getExpr().accept(this);
        return null;
    }

    default V visitStmtExpr(StmtExpr<V> stmtExpr) {
        stmtExpr.getStmt().accept(this);
        return null;
    }

    default V visitShadowExpr(ShadowExpr<V> shadowExpr) {
        if ( shadowExpr.getBlock() != null ) {
            shadowExpr.getBlock().accept(this);
        } else {
            shadowExpr.getShadow().accept(this);
        }
        return null;
    }

    default V visitSensorStmt(SensorStmt<V> sensorStmt) {
        sensorStmt.getSensor().accept(this);
        return null;
    }

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
        functionStmt.getFunction().accept(this);
        return null;
    }

    default V visitFunctionExpr(FunctionExpr<V> functionExpr) {
        functionExpr.getFunction().accept(this);
        return null;
    }

    default V visitEvalExpr(EvalExpr<V> evalExpr) {
        if ( evalExpr.getExpr() instanceof ListCreate<?> ) {
            ((ListCreate<V>) evalExpr.getExpr()).accept(this);
        } else {
            evalExpr.getExpr().accept(this);
        }
        return null;
    }

    V visitTimerSensor(TimerSensor<V> timerSensor);

    V visitAssertStmt(AssertStmt<V> assertStmt);

    V visitDebugAction(DebugAction<V> debugAction);
}
