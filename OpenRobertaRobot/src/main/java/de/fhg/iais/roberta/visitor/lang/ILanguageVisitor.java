package de.fhg.iais.roberta.visitor.lang;

import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
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
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IsListEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathModuloFunct;
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
import de.fhg.iais.roberta.syntax.lang.stmt.EvalStmts;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MathChangeStmt;
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
import de.fhg.iais.roberta.syntax.lang.stmt.TextAppendStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.visitor.IVisitor;

public interface ILanguageVisitor<V> extends IVisitor<V> {

    V visitGetSampleSensor(GetSampleSensor sensorGetSample);

    V visitActionExpr(ActionExpr actionExpr);

    V visitActionStmt(ActionStmt actionStmt);

    V visitEvalExpr(EvalExpr evalExpr);

    V visitEvalStmts(EvalStmts stmtEvalExpr);

    V visitExprStmt(ExprStmt exprStmt);

    V visitFunctionExpr(FunctionExpr functionExpr);

    V visitFunctionStmt(FunctionStmt functionStmt);

    V visitLocation(Location location);

    V visitMethodExpr(MethodExpr methodExpr);

    V visitSensorExpr(SensorExpr sensorExpr);

    V visitSensorStmt(SensorStmt sensorStmt);

    V visitStmtExpr(StmtExpr stmtExpr);

    V visitAssertStmt(AssertStmt assertStmt);

    V visitAssignStmt(AssignStmt assignStmt);

    V visitBinary(Binary binary);

    V visitBoolConst(BoolConst boolConst);

    V visitColorConst(ColorConst colorConst);

    V visitDebugAction(DebugAction debugAction);

    V visitEmptyExpr(EmptyExpr emptyExpr);

    V visitEmptyList(EmptyList emptyList);

    V visitExprList(ExprList exprList);

    V visitGetSubFunct(GetSubFunct getSubFunct);

    V visitIfStmt(IfStmt ifStmt);

    V visitTernaryExpr(TernaryExpr ternaryExpr);

    V visitIndexOfFunct(IndexOfFunct indexOfFunct);

    V visitLengthOfListFunct(LengthOfListFunct lengthOfListFunct);

    V visitIsListEmptyFunct(IsListEmptyFunct isListEmptyFunct);

    V visitListCreate(ListCreate listCreate);

    V visitListGetIndex(ListGetIndex listGetIndex);

    V visitListRepeat(ListRepeat listRepeat);

    V visitListSetIndex(ListSetIndex listSetIndex);

    V visitMainTask(MainTask mainTask);

    V visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct);

    V visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct);

    V visitMathChangeStmt(MathChangeStmt mathChangeStmt);

    V visitMathConst(MathConst mathConst);

    V visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct);

    V visitMathModuloFunct(MathModuloFunct mathModuloFunct);

    V visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct);

    V visitMathOnListFunct(MathOnListFunct mathOnListFunct);

    V visitMathPowerFunct(MathPowerFunct mathPowerFunct);

    V visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct);

    V visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct);

    V visitMathSingleFunct(MathSingleFunct mathSingleFunct);

    V visitMethodCall(MethodCall methodCall);

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

    V visitStmtFlowCon(StmtFlowCon stmtFlowCon);

    V visitStmtList(StmtList stmtList);

    V visitStmtTextComment(StmtTextComment stmtTextComment);

    V visitStringConst(StringConst stringConst);

    V visitTextAppendStmt(TextAppendStmt textAppendStmt);

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
