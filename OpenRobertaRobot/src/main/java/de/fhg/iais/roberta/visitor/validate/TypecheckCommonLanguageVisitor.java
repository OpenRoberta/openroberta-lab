package de.fhg.iais.roberta.visitor.validate;

import java.util.Arrays;
import java.util.List;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.EvalExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetBias;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetOutputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetWeight;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
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
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MathChangeStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetBiasStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetInputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetWeightStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.TextAppendStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.InfoCollector;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.typecheck.Sig;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.FunctionNames;
import de.fhg.iais.roberta.visitor.BaseVisitor;

/**
 * A helper class for unit tests to validate that all ASTs built from blockly XML are using compatible types.
 * <p>
 * This helper can be used in tests to validate the constraints on blockly toolboxes (generic ones and robot specific ones).
 * </p>
 */
public abstract class TypecheckCommonLanguageVisitor extends BaseVisitor<BlocklyType> {
    private final UsedHardwareBean usedHardwareBean;

    /**
     * initialize the typecheck visitor.
     *
     * @param phrase to typecheck
     */
    public TypecheckCommonLanguageVisitor(UsedHardwareBean usedHardwareBean) {
        this.usedHardwareBean = usedHardwareBean;
    }

    private void checkFor(Phrase phrase, boolean condition, String message) {
        if ( !condition ) {
            phrase.addInfo(NepoInfo.error(message));
        }
    }

    private void checkLookupNotNull(Phrase phrase, String name, Object supposedToBeNotNull, String message) {
        if ( supposedToBeNotNull == null ) {
            checkFor(phrase, false, message + ": " + name);
        }
    }

    public BlocklyType visitEvalExpr(EvalExpr evalExpr) {
        // temporary until the typechecker is available for the whole program
        evalExpr.exprAsBlock.accept(this);
        for ( NepoInfo info : InfoCollector.collectInfos(evalExpr.exprAsBlock) ) {
            evalExpr.addInfo(info);
        }
        // temporary until the typechecker is available for the whole program

        BlocklyType typeOfEvalBlock = evalExpr.exprAsBlock.accept(this);
        evalExpr.elevateNepoInfos();
        checkFor(evalExpr, evalExpr.getBlocklyType().equals(typeOfEvalBlock), "type of eval block doesn't match the expression");
        return evalExpr.getBlocklyType();
    }

    @Override
    public BlocklyType visitAssertStmt(AssertStmt assertStmt) {
        // alternative:
        // return Sig.of(BlocklyType.VOID, BlocklyType.BOOLEAN).typeCheckExprs(assertStmt, this, assertStmt.asserts);
        typeCheckPhrase(assertStmt, assertStmt.asserts, BlocklyType.BOOLEAN);
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitAssignStmt(AssignStmt assignStmt) {
        BlocklyType lhs = this.usedHardwareBean.getTypeOfDeclaredVariable(assignStmt.name.name);
        BlocklyType rhs = assignStmt.expr.accept(this);
        if ( !lhs.equalAsTypes(rhs) ) {
            String message = "assignment fails. Type of lhs is " + lhs + ", but rhs is " + rhs + assignStmt.getProperty().getTextPosition();
            assignStmt.addInfo(NepoInfo.error(message));
        }
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitBinary(Binary binary) {
        return binary.op.getSignature().typeCheckExprs(binary, this, binary.left, binary.right);
    }

    @Override
    public BlocklyType visitBoolConst(BoolConst boolConst) {
        Assert.isTrue(boolConst.getKind().hasName("BOOL_CONST"));
        return BlocklyType.BOOLEAN;
    }

    @Override
    public BlocklyType visitColorConst(ColorConst colorConst) {
        return BlocklyType.COLOR;
    }

    @Override
    public BlocklyType visitDebugAction(DebugAction debugAction) {
        typeCheckPhrase(debugAction, debugAction.value, BlocklyType.BOOLEAN);
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitEmptyExpr(EmptyExpr emptyExpr) {
        return BlocklyType.NOTHING;
    }

    @Override
    public BlocklyType visitEmptyList(EmptyList emptyList) {
        return emptyList.typeVar;
    }

    @Override
    public BlocklyType visitExprList(ExprList exprList) {
        for ( Expr expr : exprList.el ) {
            expr.accept(this);
        }
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitGetSubFunct(GetSubFunct getSubFunct) {
        return getSubFunct.functName.signature.typeCheckExprList(getSubFunct, this, getSubFunct.param);
    }

    @Override
    public BlocklyType visitIfStmt(IfStmt ifStmt) {
        ifStmt.expr.stream().forEach(expr -> typeCheckPhrase(ifStmt, expr, BlocklyType.BOOLEAN));
        ifStmt.thenList.stream().forEach(sl -> typeCheckPhrase(ifStmt, sl, BlocklyType.VOID));
        typeCheckPhrase(ifStmt, ifStmt.elseList, BlocklyType.VOID);
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitTernaryExpr(TernaryExpr ternaryExpr) {
        return Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.BOOLEAN, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE).
            typeCheckExprs(ternaryExpr, this, ternaryExpr.condition, ternaryExpr.thenPart, ternaryExpr.elsePart);
    }

    @Override
    public BlocklyType visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        return FunctionNames.INDEXOF.signature.typeCheckExprs(indexOfFunct, this, indexOfFunct.value, indexOfFunct.find);

    }

    @Override
    public BlocklyType visitLengthOfListFunct(LengthOfListFunct lengthOfListFunct) {
        return FunctionNames.LIST_LENGTH.signature.typeCheckExprs(lengthOfListFunct, this, lengthOfListFunct.value);
    }

    @Override
    public BlocklyType visitIsListEmptyFunct(IsListEmptyFunct isListEmptyFunct) {
        BlocklyType actualType = isListEmptyFunct.value.accept(this);
        if ( !actualType.isArray() ) {
            String message = "expected was a list, but found was: " + actualType;
            isListEmptyFunct.addInfo(NepoInfo.error(message));
        }
        return BlocklyType.BOOLEAN;
    }

    @Override
    public BlocklyType visitListCreate(ListCreate listCreate) {
        BlocklyType expectedType = BlocklyType.CAPTURED_TYPE;
        for ( Phrase phrase : listCreate.exprList.get() ) {
            expectedType = this.typeCheckPhrase(listCreate, phrase, expectedType);
        }
        return expectedType.getMatchingArrayTypeForElementType();
    }

    @Override
    public BlocklyType visitListGetIndex(ListGetIndex listGetIndex) {
        if ( listGetIndex.location == IndexLocation.FIRST || listGetIndex.location == IndexLocation.LAST ) {
            return FunctionNames.GETFIRST.signature.typeCheckExprList(listGetIndex, this, listGetIndex.param);
        } else {
            return FunctionNames.GETLISTELEMENT.signature.typeCheckExprList(listGetIndex, this, listGetIndex.param);
        }
    }

    @Override
    public BlocklyType visitListRepeat(ListRepeat listRepeat) {
        // TODO Auto-generated method stub
        return FunctionNames.LISTS_REPEAT.signature.typeCheckExprList(listRepeat, this, listRepeat.param);

    }

    @Override
    public BlocklyType visitListSetIndex(ListSetIndex listSetIndex) {
        typeCheckPhrase(listSetIndex, listSetIndex.param.get(0), BlocklyType.CAPTURED_TYPE);
        typeCheckPhrase(listSetIndex, listSetIndex.param.get(1), BlocklyType.NUMBER);
        typeCheckPhrase(listSetIndex, listSetIndex.param.get(2), BlocklyType.CAPTURED_TYPE_ARRAY_ITEM);
        return BlocklyType.VOID;

    }

    @Override
    public BlocklyType visitMainTask(MainTask mainTask) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        return BlocklyType.STRING;
    }

    @Override
    public BlocklyType visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        return BlocklyType.STRING;
    }

    @Override
    public BlocklyType visitMathChangeStmt(MathChangeStmt mathChangeStmt) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitMathConst(MathConst mathConst) {
        Assert.isTrue(mathConst.getKind().hasName("MATH_CONST"));
        String name = mathConst.mathConst.name();
        BlocklyType type = FunctionNames.get(name).signature.returnType;
        checkLookupNotNull(mathConst, name, type, "invalid mathematical constant");
        return type;
    }

    @Override
    public BlocklyType visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        return FunctionNames.CONSTRAIN.signature.typeCheckExprs(mathConstrainFunct, this, mathConstrainFunct.value, mathConstrainFunct.lowerBound, mathConstrainFunct.upperBound);
    }

    @Override
    public BlocklyType visitMathModuloFunct(MathModuloFunct mathModuloFunct) {
        BlocklyType divisorType = mathModuloFunct.divisor.accept(this);
        BlocklyType dividentType = mathModuloFunct.dividend.accept(this);
        checkFor(mathModuloFunct, divisorType.equals(BlocklyType.NUMBER) && dividentType.equals(BlocklyType.NUMBER), "type number is expected");
        return BlocklyType.NUMBER;
    }

    @Override
    public BlocklyType visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        return mathNumPropFunct.functName.signature.typeCheckExprList(mathNumPropFunct, this, mathNumPropFunct.param);
    }

    @Override
    public BlocklyType visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        Sig funcSig = mathOnListFunct.functName.signature;
        return funcSig.typeCheckExprs(mathOnListFunct, this, mathOnListFunct.list);
    }

    @Override
    public BlocklyType visitMathPowerFunct(MathPowerFunct func) {
        return FunctionNames.get(func.functName.name()).signature.typeCheckExprList(func, this, func.param);
    }

    @Override
    public BlocklyType visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        return BlocklyType.NUMBER;
    }

    @Override
    public BlocklyType visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        return FunctionNames.RANDOMINT.signature.typeCheckExprs(mathRandomIntFunct, this, mathRandomIntFunct.from, mathRandomIntFunct.to);
    }

    @Override
    public BlocklyType visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        return mathSingleFunct.functName.signature.typeCheckExprList(mathSingleFunct, this, mathSingleFunct.param);
    }


    @Override
    public BlocklyType visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitMethodReturn(MethodReturn methodReturn) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitMethodStmt(MethodStmt methodStmt) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitMethodCall(MethodCall methodCall) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitMethodVoid(MethodVoid methodVoid) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitNNStepStmt(NNStepStmt nnStepStmt) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitNNSetInputNeuronVal(NNSetInputNeuronVal nnSetInputNeuronVal) {
        return BlocklyType.VOID;
    }


    @Override
    public BlocklyType visitNNGetOutputNeuronVal(NNGetOutputNeuronVal nnGetOutputNeuronVal) {
        return BlocklyType.NUMBER;
    }

    @Override
    public BlocklyType visitNNSetWeightStmt(NNSetWeightStmt nnSetWeightStmt) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitNNSetBiasStmt(NNSetBiasStmt nnSetBiasStmt) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitNNGetWeight(NNGetWeight nnGetWeight) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitNNGetBias(NNGetBias nnGetBias) {
        return BlocklyType.VOID;
    }


    @Override
    public BlocklyType visitNullConst(NullConst nullConst) {
        Assert.isTrue(nullConst.getKind().hasName("NULL_CONST"));
        return BlocklyType.NULL;
    }

    @Override
    public BlocklyType visitNumConst(NumConst numConst) {
        Assert.isTrue(numConst.getKind().hasName("NUM_CONST"));
        return BlocklyType.NUMBER;
    }

    @Override
    public BlocklyType visitRepeatStmt(RepeatStmt repeatStmt) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitRgbColor(RgbColor rgbColor) {
        Assert.isTrue(rgbColor.getKind().hasName("RGB_COLOR"));
        return Sig.of(BlocklyType.COLOR, BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER).
            typeCheckExprs(rgbColor, this, rgbColor.R, rgbColor.G, rgbColor.B);

    }

    @Override
    public BlocklyType visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        return serialWriteAction.value.accept(this);
    }

    @Override
    public BlocklyType visitStmtFlowCon(StmtFlowCon stmtFlowCon) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitStmtList(StmtList stmtList) {
        stmtList.sl.stream().forEach(s -> typeCheckPhrase(stmtList, s, BlocklyType.VOID));
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitStmtTextComment(StmtTextComment stmtTextComment) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitStringConst(StringConst stringConst) {
        Assert.isTrue(stringConst.getKind().hasName("STRING_CONST"));
        return BlocklyType.STRING;
    }

    @Override
    public BlocklyType visitTextAppendStmt(TextAppendStmt textAppendStmt) {
        return Sig.of(BlocklyType.VOID, BlocklyType.STRING, BlocklyType.STRING).typeCheckExprs(textAppendStmt, this, textAppendStmt.var, textAppendStmt.text);
    }

    @Override
    public BlocklyType visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        return BlocklyType.NUMBER_INT;
    }

    @Override
    public BlocklyType visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        return FunctionNames.TEXTJOIN.signature.typeCheckExprList(textJoinFunct, this, textJoinFunct.param.el);
    }

    @Override
    public BlocklyType visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        return BlocklyType.NUMBER_INT;
    }

    @Override
    public BlocklyType visitTimerSensor(TimerSensor timerSensor) {
        return BlocklyType.NUMBER;
    }

    @Override
    public BlocklyType visitTimerReset(TimerReset timerReset) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitUnary(Unary unary) {
        return unary.op.signature.typeCheckExprs(unary, this, unary.expr);
    }

    @Override
    public BlocklyType visitVar(Var var) {
        return usedHardwareBean.getTypeOfDeclaredVariable(var.name);
    }

    @Override
    public BlocklyType visitVarDeclaration(VarDeclaration var) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitWaitStmt(WaitStmt waitStmt) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        return BlocklyType.NUMBER_INT;
    }

    /**
     * typecheck the given {@link phraseToTypeCheck} and expect, that it is of the type {@link expectedType}. The expression is a component of
     * the AST-object {@link parentPhrase}. If an error is found, add an error annotation to {@link parentPhrase}.
     *
     * @param phrase AST-object of which a component should be typechecked
     * @param phraseToTypeCheck an expression, part of phrase, that should be typechecked
     * @param expectedType the expected type
     */
    protected final BlocklyType typeCheckPhrase(Phrase parentPhrase, Phrase phraseToTypeCheck, BlocklyType expectedType) {
        BlocklyType actualType = phraseToTypeCheck.accept(this);
        if ( expectedType.equals(BlocklyType.CAPTURED_TYPE) ) {
            return actualType;
        } else {
            addErrorIfTypeCheckFailed(parentPhrase, expectedType, actualType);
            return expectedType;
        }
    }

    /**
     * typecheck a predefined function with one parameter
     *
     * @param functName
     * @param functionPhrase
     * @param parameterPhrase
     * @return the return type of the function
     */
    protected final BlocklyType typeCheckPrefefinedFunctionOneParam(FunctionNames functName, Phrase functionPhrase, Expr parameterPhrase) {
        Sig sig = functName.signature;
        return sig.typeCheckExprs(functionPhrase, this, parameterPhrase);
    }

    /**
     * typecheck a predefined function with more than one parameter
     *
     * @param functName
     * @param functionPhrase
     * @param parameterPhrases
     * @return the return type of the function
     */
    @Deprecated
    protected final BlocklyType typeCheckPredefinedFunctionManyParams(FunctionNames functName, Phrase functionPhrase, List<Expr> parameterPhrases) {
        Sig sig = functName.signature;
        return sig.typeCheckExprList(functionPhrase, this, parameterPhrases);
    }

    public static void addErrorIfTypeCheckFailed(Phrase phrase, BlocklyType expectedType, BlocklyType actualType) {
        if ( !expectedType.equals(actualType) ) {
            String message = "expected was: " + expectedType + ", but found was: " + actualType;
            phrase.addInfo(NepoInfo.error(message));
        }
    }

    @Deprecated
    public BlocklyType deprecated(FunctionExpr functionExpr) {
        Function function = functionExpr.function;
        if ( function instanceof MathOnListFunct ) {
            MathOnListFunct molFunction = (MathOnListFunct) function;
            return typeCheckPrefefinedFunctionOneParam(molFunction.functName, functionExpr, molFunction.list);
        } else if ( function instanceof MathSingleFunct ) {
            MathSingleFunct msfFunction = (MathSingleFunct) function;
            return typeCheckPredefinedFunctionManyParams(msfFunction.functName, functionExpr, msfFunction.param);
        } else if ( function instanceof MathNumPropFunct ) {
            MathNumPropFunct mnpFunction = (MathNumPropFunct) function;
            return typeCheckPredefinedFunctionManyParams(mnpFunction.functName, functionExpr, mnpFunction.param);

        } else if ( function instanceof IsListEmptyFunct ) {
            IsListEmptyFunct ilFunction = (IsListEmptyFunct) function;
            return typeCheckPrefefinedFunctionOneParam(FunctionNames.LIST_IS_EMPTY, functionExpr, ilFunction.value);
        } else if ( function instanceof ListRepeat ) {
            ListRepeat lrFunction = (ListRepeat) function;
            return typeCheckPredefinedFunctionManyParams(FunctionNames.LISTS_REPEAT, functionExpr, lrFunction.param);
        } else if ( function instanceof MathConstrainFunct ) {
            MathConstrainFunct mcFunction = (MathConstrainFunct) function;
            List<Expr> objectList = Arrays.asList(mcFunction.value, mcFunction.lowerBound, mcFunction.upperBound);
            return typeCheckPredefinedFunctionManyParams(FunctionNames.CONSTRAIN, functionExpr, objectList);
        } else if ( function instanceof MathRandomIntFunct ) {
            MathRandomIntFunct mrFunction = (MathRandomIntFunct) function;
            List<Expr> objectList = Arrays.asList(mrFunction.from, mrFunction.to);
            return typeCheckPredefinedFunctionManyParams(FunctionNames.RANDOMINT, functionExpr, objectList);
        }
        throw new DbcException("invalid function expression");
    }

}
