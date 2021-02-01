package de.fhg.iais.roberta.visitor.lang;

import de.fhg.iais.roberta.syntax.lang.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.lang.expr.*;
import de.fhg.iais.roberta.syntax.lang.functions.*;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.*;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.visitor.IVisitor;

public interface ILanguageVisitor<V> extends IVisitor<V> {

    default V visitActionExpr(ActionExpr<V> actionExpr) {
        actionExpr.getAction().accept(this);
        return null;
    }

    default V visitActionStmt(ActionStmt<V> actionStmt) {
        actionStmt.getAction().accept(this);
        return null;
    }

    default V visitActivityTask(ActivityTask<V> activityTask) {
        return null;
    }

    V visitAssertStmt(AssertStmt<V> assertStmt);

    V visitAssignStmt(AssignStmt<V> assignStmt);

    V visitBinary(Binary<V> binary);

    V visitBoolConst(BoolConst<V> boolConst);

    V visitColorConst(ColorConst<V> colorConst);

    V visitConnectConst(ConnectConst<V> connectConst);

    V visitDebugAction(DebugAction<V> debugAction);

    V visitEmptyExpr(EmptyExpr<V> emptyExpr);

    V visitEmptyList(EmptyList<V> emptyList);

    default V visitEvalExpr(EvalExpr<V> evalExpr) {
        if ( evalExpr.getExpr() instanceof ListCreate<?> ) {
            ((ListCreate<V>) evalExpr.getExpr()).accept(this);
        } else {
            evalExpr.getExpr().accept(this);
        }
        return null;
    }

    V visitExprList(ExprList<V> exprList);

    default V visitExprStmt(ExprStmt<V> exprStmt) {
        exprStmt.getExpr().accept(this);
        return null;
    }

    default V visitFunctionExpr(FunctionExpr<V> functionExpr) {
        functionExpr.getFunction().accept(this);
        return null;
    }

    default V visitFunctionStmt(FunctionStmt<V> functionStmt) {
        functionStmt.getFunction().accept(this);
        return null;
    }

    V visitGetSubFunct(GetSubFunct<V> getSubFunct);

    V visitIfStmt(IfStmt<V> ifStmt);

    V visitIndexOfFunct(IndexOfFunct<V> indexOfFunct);

    V visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<V> lengthOfIsEmptyFunct);

    V visitListCreate(ListCreate<V> listCreate);

    V visitListGetIndex(ListGetIndex<V> listGetIndex);

    V visitListRepeat(ListRepeat<V> listRepeat);

    V visitListSetIndex(ListSetIndex<V> listSetIndex);

    default V visitLocation(Location<V> location) {
        return null;
    }

    V visitMainTask(MainTask<V> mainTask);

    V visitMathCastCharFunct(MathCastCharFunct<V> mathCastCharFunct);

    V visitMathCastStringFunct(MathCastStringFunct<V> mathCastStringFunct);

    V visitMathConst(MathConst<V> mathConst);

    V visitMathConstrainFunct(MathConstrainFunct<V> mathConstrainFunct);

    V visitMathNumPropFunct(MathNumPropFunct<V> mathNumPropFunct);

    V visitMathOnListFunct(MathOnListFunct<V> mathOnListFunct);

    V visitMathPowerFunct(MathPowerFunct<V> mathPowerFunct);

    V visitMathRandomFloatFunct(MathRandomFloatFunct<V> mathRandomFloatFunct);

    V visitMathRandomIntFunct(MathRandomIntFunct<V> mathRandomIntFunct);

    V visitMathSingleFunct(MathSingleFunct<V> mathSingleFunct);

    V visitMethodCall(MethodCall<V> methodCall);

    default V visitMethodExpr(MethodExpr<V> methodExpr) {
        methodExpr.getMethod().accept(this);
        return null;
    }

    V visitMethodIfReturn(MethodIfReturn<V> methodIfReturn);

    V visitMethodReturn(MethodReturn<V> methodReturn);

    V visitMethodStmt(MethodStmt<V> methodStmt);

    V visitMethodVoid(MethodVoid<V> methodVoid);

    V visitNNStepStmt(NNStepStmt<V> nnStepStmt);

    V visitNullConst(NullConst<V> nullConst);

    V visitNumConst(NumConst<V> numConst);

    V visitRepeatStmt(RepeatStmt<V> repeatStmt);

    V visitRgbColor(RgbColor<V> rgbColor);

    default V visitSensorExpr(SensorExpr<V> sensorExpr) {
        sensorExpr.getSens().accept(this);
        return null;
    }

    default V visitSensorStmt(SensorStmt<V> sensorStmt) {
        sensorStmt.getSensor().accept(this);
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

    default V visitStartActivityTask(StartActivityTask<V> startActivityTask) {
        return null;
    }

    default V visitStmtExpr(StmtExpr<V> stmtExpr) {
        stmtExpr.getStmt().accept(this);
        return null;
    }

    V visitStmtFlowCon(StmtFlowCon<V> stmtFlowCon);

    V visitStmtList(StmtList<V> stmtList);

    V visitStmtTextComment(StmtTextComment<V> stmtTextComment);

    V visitStringConst(StringConst<V> stringConst);

    V visitTextCharCastNumberFunct(TextCharCastNumberFunct<V> textCharCastNumberFunct);

    V visitTextJoinFunct(TextJoinFunct<V> textJoinFunct);

    V visitTextPrintFunct(TextPrintFunct<V> textPrintFunct);

    V visitTextStringCastNumberFunct(TextStringCastNumberFunct<V> textStringCastNumberFunct);

    V visitTimerSensor(TimerSensor<V> timerSensor);

    V visitUnary(Unary<V> unary);

    V visitVar(Var<V> var);

    V visitVarDeclaration(VarDeclaration<V> var);

    V visitWaitStmt(WaitStmt<V> waitStmt);

    V visitWaitTimeStmt(WaitTimeStmt<V> waitTimeStmt);

}
