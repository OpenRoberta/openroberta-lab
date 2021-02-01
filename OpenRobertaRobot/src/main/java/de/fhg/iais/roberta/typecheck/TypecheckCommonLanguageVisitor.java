package de.fhg.iais.roberta.typecheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;
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
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
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
import de.fhg.iais.roberta.typecheck.NepoInfo.Severity;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * A helper class for unit tests to validate that all ASTs built from blockly XML are using compatible types.
 * <p>
 * This helper can be used in tests to validate the constraints on blockly toolboxes (generic ones and robot specific ones).
 * </p>
 */
public class TypecheckCommonLanguageVisitor implements ILanguageVisitor<BlocklyType> {
    /**
     * typecheck an AST. This is done by a visitor, which is an instance of this class<br>
     *
     * @param phrase to typecheck
     * @return the typecheck visitor (to get information about errors and the derived type)
     */
    public static TypecheckCommonLanguageVisitor makeVisitorAndTypecheck(Phrase<BlocklyType> phrase) //
    {
        Assert.notNull(phrase);

        TypecheckCommonLanguageVisitor astVisitor = new TypecheckCommonLanguageVisitor(phrase);
        astVisitor.resultType = phrase.accept(astVisitor);
        return astVisitor;
    }

    private final int ERROR_LIMIT_FOR_TYPECHECK = 10;

    private final Phrase<BlocklyType> phrase;
    private List<NepoInfo> infos = null;
    private int errorCount = 0;

    private BlocklyType resultType;

    /**
     * initialize the typecheck visitor.
     *
     * @param phrase to typecheck
     */
    TypecheckCommonLanguageVisitor(Phrase<BlocklyType> phrase) {
        this.phrase = phrase;
    }

    private void checkFor(Phrase<BlocklyType> phrase, boolean condition, String message) {
        if ( !condition ) {
            if ( this.errorCount >= this.ERROR_LIMIT_FOR_TYPECHECK ) {
                throw new RuntimeException("aborting typecheck. More than " + this.ERROR_LIMIT_FOR_TYPECHECK + " errors found.");
            } else {
                this.errorCount++;
                NepoInfo error = NepoInfo.error(message);
                phrase.addInfo(error);
            }
        }
    }

    private void checkLookupNotNull(Phrase<BlocklyType> phrase, String name, Object supposedToBeNotNull, String message) {
        if ( supposedToBeNotNull == null ) {
            checkFor(phrase, false, message + ": " + name);
        }
    }

    /**
     * get the number of <b>errors</b>
     *
     * @return the number of <b>errors</b> detected during this type check visit
     */
    public int getErrorCount() {
        if ( this.infos == null ) {
            this.infos = InfoCollector.collectInfos(this.phrase);
            for ( NepoInfo info : this.infos ) {
                if ( info.getSeverity() == Severity.ERROR ) {
                    this.errorCount++;
                }
            }
        }
        return this.errorCount;
    }

    /**
     * get the list of all infos (errors, warnings) generated during this typecheck visit
     *
     * @return the list of all infos
     */
    public List<NepoInfo> getInfos() {
        getErrorCount(); // for the side effect
        return this.infos;
    }

    /**
     * return the type that was inferred by the typechecker for the given phrase
     *
     * @return the resulting type. May be <code>null</code> if type errors occurred
     */
    public BlocklyType getResultType() {
        return this.resultType;
    }

    private List<BlocklyType> typecheckList(List<Expr<BlocklyType>> params) {
        List<BlocklyType> paramTypes = new ArrayList<>(params.size());
        for ( Expr<BlocklyType> param : params ) {
            paramTypes.add(param.accept(this));
        }
        return paramTypes;
    }

    @Override
    public BlocklyType visitActionExpr(ActionExpr<BlocklyType> actionExpr) {
        return null;
    }

    @Override
    public BlocklyType visitActionStmt(ActionStmt<BlocklyType> actionStmt) {
        return null;
    }

    @Override
    public BlocklyType visitActivityTask(ActivityTask<BlocklyType> activityTask) {
        return null;
    }

    @Override
    public BlocklyType visitAssertStmt(AssertStmt<BlocklyType> assertStmt) {
        return null;
    }

    @Override
    public BlocklyType visitAssignStmt(AssignStmt<BlocklyType> assignStmt) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitBinary(Binary<BlocklyType> binary) {
        BlocklyType left = binary.getLeft().accept(this);
        BlocklyType right = binary.getRight().accept(this);
        //        Sig signature = TypeTransformations.getBinarySignature(binary.getOp().getOpSymbol());
        Sig signature = binary.getOp().getSignature();
        return signature.typeCheck(binary, Arrays.asList(left, right));
    }

    @Override
    public BlocklyType visitBoolConst(BoolConst<BlocklyType> boolConst) {
        Assert.isTrue(boolConst.getKind().hasName("BOOL_CONST"));
        return BlocklyType.BOOLEAN;
    }

    @Override
    public BlocklyType visitColorConst(ColorConst<BlocklyType> colorConst) {
        Assert.isTrue(colorConst.getKind().hasName("COLOR_CONST"));
        return BlocklyType.COLOR;
    }

    @Override
    public BlocklyType visitConnectConst(ConnectConst<BlocklyType> connectConst) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitDebugAction(DebugAction<BlocklyType> debugAction) {
        return null;
    }

    @Override
    public BlocklyType visitEmptyExpr(EmptyExpr<BlocklyType> emptyExpr) {
        return null;
    }

    @Override
    public BlocklyType visitEmptyList(EmptyList<BlocklyType> emptyList) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitExprList(ExprList<BlocklyType> exprList) {
        return null;
    }

    @Override
    public BlocklyType visitExprStmt(ExprStmt<BlocklyType> exprStmt) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitFunctionExpr(FunctionExpr<BlocklyType> functionExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitFunctionStmt(FunctionStmt<BlocklyType> functionStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitGetSubFunct(GetSubFunct<BlocklyType> getSubFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitIfStmt(IfStmt<BlocklyType> ifStmt) {
        return null;
    }

    @Override
    public BlocklyType visitIndexOfFunct(IndexOfFunct<BlocklyType> indexOfFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<BlocklyType> lengthOfIsEmptyFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitListCreate(ListCreate<BlocklyType> listCreate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitListGetIndex(ListGetIndex<BlocklyType> listGetIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitListRepeat(ListRepeat<BlocklyType> listRepeat) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitListSetIndex(ListSetIndex<BlocklyType> listSetIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitLocation(Location<BlocklyType> location) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMainTask(MainTask<BlocklyType> mainTask) {
        return null;
    }

    @Override
    public BlocklyType visitMathCastCharFunct(MathCastCharFunct<BlocklyType> mathCastCharFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathCastStringFunct(MathCastStringFunct<BlocklyType> mathCastStringFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathConst(MathConst<BlocklyType> mathConst) {
        Assert.isTrue(mathConst.getKind().hasName("MATH_CONST"));
        String name = mathConst.getMathConst().name();
        BlocklyType type = TypeTransformations.getConstantSignature(name);
        checkLookupNotNull(mathConst, name, type, "invalid mathematical constant");
        return type;
    }

    @Override
    public BlocklyType visitMathConstrainFunct(MathConstrainFunct<BlocklyType> mathConstrainFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathNumPropFunct(MathNumPropFunct<BlocklyType> mathNumPropFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathOnListFunct(MathOnListFunct<BlocklyType> mathOnListFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathPowerFunct(MathPowerFunct<BlocklyType> func) {
        List<BlocklyType> paramTypes = typecheckList(func.getParam());
        Sig signature = TypeTransformations.getFunctionSignature(func.getFunctName().name());
        BlocklyType resultType = signature.typeCheck(func, paramTypes);
        return resultType;
    }

    @Override
    public BlocklyType visitMathRandomFloatFunct(MathRandomFloatFunct<BlocklyType> mathRandomFloatFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathRandomIntFunct(MathRandomIntFunct<BlocklyType> mathRandomIntFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathSingleFunct(MathSingleFunct<BlocklyType> mathSingleFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodCall(MethodCall<BlocklyType> methodCall) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodExpr(MethodExpr<BlocklyType> methodExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodIfReturn(MethodIfReturn<BlocklyType> methodIfReturn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodReturn(MethodReturn<BlocklyType> methodReturn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodStmt(MethodStmt<BlocklyType> methodStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodVoid(MethodVoid<BlocklyType> methodVoid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitNNStepStmt(NNStepStmt<BlocklyType> nnStepStmt) {
        return null;
    }

    @Override
    public BlocklyType visitNullConst(NullConst<BlocklyType> nullConst) {
        Assert.isTrue(nullConst.getKind().hasName("NULL_CONST"));
        return BlocklyType.NULL;
    }

    @Override
    public BlocklyType visitNumConst(NumConst<BlocklyType> numConst) {
        Assert.isTrue(numConst.getKind().hasName("NUM_CONST"));
        return BlocklyType.NUMBER;
    }

    @Override
    public BlocklyType visitRepeatStmt(RepeatStmt<BlocklyType> repeatStmt) {
        return null;
    }

    @Override
    public BlocklyType visitRgbColor(RgbColor<BlocklyType> rgbColor) {
        Assert.isTrue(rgbColor.getKind().hasName("RGB_COLOR"));
        return rgbColor.getVarType();
    }

    @Override
    public BlocklyType visitSensorExpr(SensorExpr<BlocklyType> sensorExpr) {
        return null;
    }

    @Override
    public BlocklyType visitSensorStmt(SensorStmt<BlocklyType> sensorStmt) {
        return null;
    }

    @Override
    public BlocklyType visitShadowExpr(ShadowExpr<BlocklyType> shadowExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitStartActivityTask(StartActivityTask<BlocklyType> startActivityTask) {
        return null;
    }

    @Override
    public BlocklyType visitStmtExpr(StmtExpr<BlocklyType> stmtExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitStmtFlowCon(StmtFlowCon<BlocklyType> stmtFlowCon) {
        return null;
    }

    @Override
    public BlocklyType visitStmtList(StmtList<BlocklyType> stmtList) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitStmtTextComment(StmtTextComment<BlocklyType> stmtTextComment) {
        return null;
    }

    @Override
    public BlocklyType visitStringConst(StringConst<BlocklyType> stringConst) {
        Assert.isTrue(stringConst.getKind().hasName("STRING_CONST"));
        return BlocklyType.STRING;
    }

    @Override
    public BlocklyType visitTextCharCastNumberFunct(TextCharCastNumberFunct<BlocklyType> textCharCastNumberFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitTextJoinFunct(TextJoinFunct<BlocklyType> textJoinFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitTextPrintFunct(TextPrintFunct<BlocklyType> textPrintFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitTextStringCastNumberFunct(TextStringCastNumberFunct<BlocklyType> textStringCastNumberFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitTimerSensor(TimerSensor<BlocklyType> timerSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitUnary(Unary<BlocklyType> unary) {
        return null;
    }

    @Override
    public BlocklyType visitVar(Var<BlocklyType> var) {
        return null;
    }

    @Override
    public BlocklyType visitVarDeclaration(VarDeclaration<BlocklyType> var) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitWaitStmt(WaitStmt<BlocklyType> waitStmt) {
        return null;
    }

    @Override
    public BlocklyType visitWaitTimeStmt(WaitTimeStmt<BlocklyType> waitTimeStmt) {
        // TODO Auto-generated method stub
        return null;
    }
}
