package de.fhg.iais.roberta.visitor.lang;

import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
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
import de.fhg.iais.roberta.syntax.lang.expr.NNGetBias;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetOutputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetWeight;
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
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetBiasStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetInputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetWeightStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.visitor.IVisitor;

public interface ILanguageVisitor<V> extends IVisitor<V> {

    default V visitActionExpr(ActionExpr actionExpr) {
        actionExpr.action.accept(this);
        return null;
    }

    default V visitActionStmt(ActionStmt actionStmt) {
        actionStmt.action.accept(this);
        return null;
    }

    default V visitActivityTask(ActivityTask activityTask) {
        return null;
    }

    V visitAssertStmt(AssertStmt assertStmt);

    V visitAssignStmt(AssignStmt assignStmt);

    V visitBinary(Binary binary);

    V visitBoolConst(BoolConst boolConst);

    V visitColorConst(ColorConst colorConst);

    V visitConnectConst(ConnectConst connectConst);

    V visitDebugAction(DebugAction debugAction);

    V visitEmptyExpr(EmptyExpr emptyExpr);

    V visitEmptyList(EmptyList emptyList);

    default V visitEvalExpr(EvalExpr evalExpr) {
        if ( evalExpr.exprBlock instanceof ListCreate ) {
            ((ListCreate) evalExpr.exprBlock).accept(this);
        } else {
            evalExpr.exprBlock.accept(this);
        }
        return null;
    }

    V visitExprList(ExprList exprList);

    default V visitExprStmt(ExprStmt exprStmt) {
        exprStmt.expr.accept(this);
        return null;
    }

    default V visitFunctionExpr(FunctionExpr functionExpr) {
        functionExpr.getFunction().accept(this);
        return null;
    }

    default V visitFunctionStmt(FunctionStmt functionStmt) {
        functionStmt.function.accept(this);
        return null;
    }

    V visitGetSubFunct(GetSubFunct getSubFunct);

    V visitIfStmt(IfStmt ifStmt);

    V visitTernaryExpr(TernaryExpr ternaryExpr);

    V visitIndexOfFunct(IndexOfFunct indexOfFunct);

    V visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct lengthOfIsEmptyFunct);

    V visitListCreate(ListCreate listCreate);

    V visitListGetIndex(ListGetIndex listGetIndex);

    V visitListRepeat(ListRepeat listRepeat);

    V visitListSetIndex(ListSetIndex listSetIndex);

    default V visitLocation(Location location) {
        return null;
    }

    V visitMainTask(MainTask mainTask);

    V visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct);

    V visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct);

    V visitMathConst(MathConst mathConst);

    V visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct);

    V visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct);

    V visitMathOnListFunct(MathOnListFunct mathOnListFunct);

    V visitMathPowerFunct(MathPowerFunct mathPowerFunct);

    V visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct);

    V visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct);

    V visitMathSingleFunct(MathSingleFunct mathSingleFunct);

    V visitMethodCall(MethodCall methodCall);

    default V visitMethodExpr(MethodExpr methodExpr) {
        methodExpr.getMethod().accept(this);
        return null;
    }

    V visitMethodIfReturn(MethodIfReturn methodIfReturn);

    V visitMethodReturn(MethodReturn methodReturn);

    V visitMethodStmt(MethodStmt methodStmt);

    V visitMethodVoid(MethodVoid methodVoid);

    V visitNNStepStmt(NNStepStmt nnStepStmt);

    V visitNNSetInputNeuronVal(NNSetInputNeuronVal nnSetInputNeuronVal);

    V visitNNGetOutputNeuronVal(NNGetOutputNeuronVal nnGetOutputNeuronVal);

    V visitNNSetWeightStmt(NNSetWeightStmt nnSetWeightStmt);

    V visitNNSetBiasStmt(NNSetBiasStmt nnSetBiasStmt);

    V visitNNGetWeight(NNGetWeight nnGetWeight);

    V visitNNGetBias(NNGetBias nnGetBias);

    V visitNullConst(NullConst nullConst);

    V visitNumConst(NumConst numConst);

    V visitRepeatStmt(RepeatStmt repeatStmt);

    V visitRgbColor(RgbColor rgbColor);

    default V visitSensorExpr(SensorExpr sensorExpr) {
        sensorExpr.sensor.accept(this);
        return null;
    }

    default V visitSensorStmt(SensorStmt sensorStmt) {
        sensorStmt.sensor.accept(this);
        return null;
    }

    default V visitShadowExpr(ShadowExpr shadowExpr) {
        if ( shadowExpr.block != null ) {
            shadowExpr.block.accept(this);
        } else {
            shadowExpr.shadow.accept(this);
        }
        return null;
    }

    default V visitStartActivityTask(StartActivityTask startActivityTask) {
        return null;
    }

    default V visitStmtExpr(StmtExpr stmtExpr) {
        stmtExpr.stmt.accept(this);
        return null;
    }

    V visitStmtFlowCon(StmtFlowCon stmtFlowCon);

    V visitStmtList(StmtList stmtList);

    V visitStmtTextComment(StmtTextComment stmtTextComment);

    V visitStringConst(StringConst stringConst);

    V visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct);

    V visitTextJoinFunct(TextJoinFunct textJoinFunct);

    V visitTextPrintFunct(TextPrintFunct textPrintFunct);

    V visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitUnary(Unary unary);

    V visitVar(Var var);

    V visitVarDeclaration(VarDeclaration var);

    V visitWaitStmt(WaitStmt waitStmt);

    V visitWaitTimeStmt(WaitTimeStmt waitTimeStmt);

    V visitSerialWriteAction(SerialWriteAction serialWriteAction);

}
