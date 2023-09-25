package de.fhg.iais.roberta.visitor.validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.syntax.Phrase;
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
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
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
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
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
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * A helper class for unit tests to validate that all ASTs built from blockly XML are using compatible types.
 * <p>
 * This helper can be used in tests to validate the constraints on blockly toolboxes (generic ones and robot specific ones).
 * </p>
 */
public class TypecheckCommonLanguageVisitor extends BaseVisitor<BlocklyType> implements ILanguageVisitor<BlocklyType> {
    /**
     * typecheck an AST. This is done by a visitor, which is an instance of this class<br>
     *
     * @param phrase to typecheck
     * @return the typecheck visitor (to get information about errors and the derived type)
     */
    public static TypecheckCommonLanguageVisitor makeVisitorAndTypecheck(Phrase phrase, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) //
    {
        Assert.notNull(phrase);

        TypecheckCommonLanguageVisitor astVisitor = new TypecheckCommonLanguageVisitor(phrase, beanBuilders);
        astVisitor.resultType = phrase.accept(astVisitor);
        return astVisitor;
    }

    private final int ERROR_LIMIT_FOR_TYPECHECK = 10;

    private final ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders;
    private final UsedHardwareBean usedHardware;
    private final Phrase phrase;
    private List<NepoInfo> infos = null;
    private int errorCount = 0;

    private BlocklyType resultType;

    /**
     * initialize the typecheck visitor.
     *
     * @param phrase to typecheck
     */
    TypecheckCommonLanguageVisitor(Phrase phrase, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        this.phrase = phrase;
        this.beanBuilders = beanBuilders;
        this.usedHardware = (UsedHardwareBean) beanBuilders.get(UsedHardwareBean.Builder.class).build();
    }

    private void checkFor(Phrase phrase, boolean condition, String message) {
        if ( !condition ) {
            if ( this.errorCount >= this.ERROR_LIMIT_FOR_TYPECHECK ) {
                // too many errors won't help the user. Ignore them
            } else {
                this.errorCount++;
                NepoInfo error = NepoInfo.error(message);
                phrase.addInfo(error);
            }
        }
    }

    private void checkLookupNotNull(Phrase phrase, String name, Object supposedToBeNotNull, String message) {
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
                this.errorCount++;
//                if ( info.getSeverity() == Severity.ERROR ) {
//                    this.errorCount++;
//                }
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

    private List typecheckList(List<Expr> params) {
        List paramTypes = new ArrayList<>(params.size());
        for ( Expr param : params ) {
            paramTypes.add(param.accept(this));
        }
        return paramTypes;
    }

    public BlocklyType visitEvalExpr(EvalExpr evalExpr) {
        BlocklyType typeOfEvalBlock = evalExpr.exprAsBlock.accept(this);
        checkFor(evalExpr, evalExpr.getBlocklyType().equals(typeOfEvalBlock), "type of eval block doesn't match the expression");
        return evalExpr.getBlocklyType();
    }

    @Override
    public BlocklyType visitActionExpr(ActionExpr actionExpr) {
        return null;
    }

    @Override
    public BlocklyType visitActionStmt(ActionStmt actionStmt) {
        return null;
    }

    @Override
    public BlocklyType visitAssertStmt(AssertStmt assertStmt) {
        return null;
    }

    @Override
    public BlocklyType visitAssignStmt(AssignStmt assignStmt) {
        BlocklyType lhs = this.usedHardware.getTypeOfDeclaredVariable(assignStmt.name.name);
        BlocklyType rhs = assignStmt.expr.accept(this);
        if ( !lhs.equals(rhs) ) {
            String message = "assignment fails. Type of lhs is " + lhs + ", but rhs is " + rhs + assignStmt.getProperty().getTextPosition();
            assignStmt.addInfo(NepoInfo.error(message));
        }
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitBinary(Binary binary) {
        BlocklyType left = binary.left.accept(this);
        BlocklyType right = binary.right.accept(this);
        //        Sig signature = TypeTransformations.getBinarySignature(binary.getOp().getOpSymbol());
        Sig signature = binary.op.getSignature();
        return signature.typeCheck(binary, Arrays.asList(left, right));
    }

    @Override
    public BlocklyType visitBoolConst(BoolConst boolConst) {
        Assert.isTrue(boolConst.getKind().hasName("BOOL_CONST"));
        return BlocklyType.BOOLEAN;
    }

    @Override
    public BlocklyType visitColorConst(ColorConst colorConst) {
        Assert.isTrue(colorConst.getKind().hasName("COLOR_CONST"));
        return BlocklyType.COLOR;
    }

    @Override
    public BlocklyType visitDebugAction(DebugAction debugAction) {
        return null;
    }

    @Override
    public BlocklyType visitEmptyExpr(EmptyExpr emptyExpr) {
        return null;
    }

    @Override
    public BlocklyType visitEmptyList(EmptyList emptyList) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitExprList(ExprList exprList) {
        return null;
    }

    @Override
    public BlocklyType visitExprStmt(ExprStmt exprStmt) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitFunctionExpr(FunctionExpr functionExpr) {
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
        } else if ( function instanceof LengthOfListFunct ) {
            LengthOfListFunct llFunction = (LengthOfListFunct) function;
            return typeCheckPrefefinedFunctionOneParam(FunctionNames.LIST_LENGTH, functionExpr, llFunction.value);
        } else if ( function instanceof IsListEmptyFunct ) {
            IsListEmptyFunct ilFunction = (IsListEmptyFunct) function;
            return typeCheckPrefefinedFunctionOneParam(FunctionNames.LIST_IS_EMPTY, functionExpr, ilFunction.value);
        } else if ( function instanceof IndexOfFunct ) {
            IndexOfFunct ioFunction = (IndexOfFunct) function;
            List<Expr> objectList = Arrays.asList(ioFunction.value, ioFunction.find);
            return typeCheckPredefinedFunctionManyParams(FunctionNames.INDEXOF, functionExpr, objectList);
        } else if ( function instanceof ListGetIndex ) {
            ListGetIndex lgiFunction = (ListGetIndex) function;
            if ( lgiFunction.location == IndexLocation.FIRST ) {
                return typeCheckPredefinedFunctionManyParams(FunctionNames.GETFIRST, functionExpr, lgiFunction.param);
            } else if ( lgiFunction.location == IndexLocation.LAST ) {
                return typeCheckPredefinedFunctionManyParams(FunctionNames.GETLAST, functionExpr, lgiFunction.param);
            } else {
                return typeCheckPredefinedFunctionManyParams(FunctionNames.GETLISTELEMENT, functionExpr, lgiFunction.param);
            }
        } else if ( function instanceof GetSubFunct ) {
            GetSubFunct gsFunction = (GetSubFunct) function;
            return typeCheckPredefinedFunctionManyParams(gsFunction.functName, functionExpr, gsFunction.param);
        } else if ( function instanceof ListRepeat ) {
            ListRepeat lrFunction = (ListRepeat) function;
            return typeCheckPredefinedFunctionManyParams(FunctionNames.LISTS_REPEAT, functionExpr, lrFunction.param);
        } else if ( function instanceof TextJoinFunct ) {
            TextJoinFunct tjFunction = (TextJoinFunct) function;
            return typeCheckPrefefinedFunctionOneParam(FunctionNames.TEXTJOIN, functionExpr, tjFunction.param);
        } else if ( function instanceof MathConstrainFunct ) {
            MathConstrainFunct mcFunction = (MathConstrainFunct) function;
            List<Expr> objectList = Arrays.asList(mcFunction.value, mcFunction.lowerBound, mcFunction.upperBound);
            return typeCheckPredefinedFunctionManyParams(FunctionNames.CONSTRAIN, functionExpr, objectList);
        } else if ( function instanceof MathRandomIntFunct ) {
            MathRandomIntFunct mrFunction = (MathRandomIntFunct) function;
            List<Expr> objectList = Arrays.asList(mrFunction.from, mrFunction.to);
            return typeCheckPredefinedFunctionManyParams(FunctionNames.RANDOMINT, functionExpr, objectList);
        } else if ( function instanceof MathRandomFloatFunct ) {
            return BlocklyType.NUMBER;
        }
        throw new DbcException("invalid function expression");
    }

    @Override
    public BlocklyType visitFunctionStmt(FunctionStmt functionStmt) {
        functionStmt.function.accept(this);
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitGetSubFunct(GetSubFunct getSubFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitIfStmt(IfStmt ifStmt) {
        return null;
    }

    @Override
    public BlocklyType visitTernaryExpr(TernaryExpr ternaryExpr) {
        typeCheckPhrase(ternaryExpr, ternaryExpr.condition, BlocklyType.BOOLEAN);
        BlocklyType actualType = ternaryExpr.thenPart.accept(this);
        typeCheckPhrase(ternaryExpr, ternaryExpr.elsePart, actualType);
        return actualType;
    }

    @Override
    public BlocklyType visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitLengthOfListFunct(LengthOfListFunct lengthOfListFunct) {
        return null;
    }

    @Override
    public BlocklyType visitIsListEmptyFunct(IsListEmptyFunct isListEmptyFunct) {
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitListRepeat(ListRepeat listRepeat) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitListSetIndex(ListSetIndex listSetIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitLocation(Location location) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMainTask(MainTask mainTask) {
        return null;
    }

    @Override
    public BlocklyType visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathChangeStmt(MathChangeStmt mathChangeStmt) {
        return null;
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
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        Sig funcSig = mathOnListFunct.functName.signature;
        return funcSig.typeCheckParameter(mathOnListFunct, this, mathOnListFunct.list);
    }

    @Override
    public BlocklyType visitMathPowerFunct(MathPowerFunct func) {
        List paramTypes = typecheckList(func.param);
        //Sig signature = TypeTransformations.getFunctionSignature(func.functName.name());
        Sig signature = FunctionNames.get(func.functName.name()).signature;
        BlocklyType resultType = signature.typeCheck(func, paramTypes);
        return resultType;
    }

    @Override
    public BlocklyType visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodCall(MethodCall methodCall) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodExpr(MethodExpr methodExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodReturn(MethodReturn methodReturn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodStmt(MethodStmt methodStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodVoid(MethodVoid methodVoid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitNNStepStmt(NNStepStmt nnStepStmt) {
        return null;
    }

    @Override
    public BlocklyType visitNNSetInputNeuronVal(NNSetInputNeuronVal nnSetInputNeuronVal) {
        return null;
    }


    @Override
    public BlocklyType visitNNGetOutputNeuronVal(NNGetOutputNeuronVal nnGetOutputNeuronVal) {
        return BlocklyType.NUMBER;
    }

    @Override
    public BlocklyType visitNNSetWeightStmt(NNSetWeightStmt nnSetWeightStmt) {
        return null;
    }

    @Override
    public BlocklyType visitNNSetBiasStmt(NNSetBiasStmt nnSetBiasStmt) {
        return null;
    }

    @Override
    public BlocklyType visitNNGetWeight(NNGetWeight nnGetWeight) {
        return null;
    }

    @Override
    public BlocklyType visitNNGetBias(NNGetBias nnGetBias) {
        return null;
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
        return null;
    }

    @Override
    public BlocklyType visitRgbColor(RgbColor rgbColor) {
        Assert.isTrue(rgbColor.getKind().hasName("RGB_COLOR"));
        typeCheckPhrase(rgbColor, rgbColor.R, BlocklyType.NUMBER);
        typeCheckPhrase(rgbColor, rgbColor.G, BlocklyType.NUMBER);
        typeCheckPhrase(rgbColor, rgbColor.B, BlocklyType.NUMBER);
        return BlocklyType.COLOR;
    }

    @Override
    public BlocklyType visitSensorExpr(SensorExpr sensorExpr) {
        return null;
    }

    @Override
    public BlocklyType visitSensorStmt(SensorStmt sensorStmt) {
        return null;
    }

    @Override
    public BlocklyType visitStmtExpr(StmtExpr stmtExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitStmtFlowCon(StmtFlowCon stmtFlowCon) {
        return null;
    }

    @Override
    public BlocklyType visitStmtList(StmtList stmtList) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitStmtTextComment(StmtTextComment stmtTextComment) {
        return null;
    }

    @Override
    public BlocklyType visitStringConst(StringConst stringConst) {
        Assert.isTrue(stringConst.getKind().hasName("STRING_CONST"));
        return BlocklyType.STRING;
    }

    @Override
    public BlocklyType visitTextAppendStmt(TextAppendStmt textAppendStmt) {
        return null;
    }

    @Override
    public BlocklyType visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        //textJoinFunct.param
        return BlocklyType.STRING;
    }

    @Override
    public BlocklyType visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitTimerSensor(TimerSensor timerSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitTimerReset(TimerReset timerReset) {
        return null;
    }

    @Override
    public BlocklyType visitUnary(Unary unary) {
        if ( unary.op.equals(Unary.Op.PLUS) || unary.op.equals(Unary.Op.NEG) ) {
            typeCheckPhrase(unary, unary.expr, BlocklyType.NUMBER);
            return BlocklyType.NUMBER;
        } else if ( unary.op.equals(Unary.Op.NOT) ) {
            typeCheckPhrase(unary, unary.expr, BlocklyType.BOOLEAN);
            return BlocklyType.BOOLEAN;
        } else {
            return null;
        }

    }

    @Override
    public BlocklyType visitVar(Var var) {
        return usedHardware.getTypeOfDeclaredVariable(var.name);
    }

    @Override
    public BlocklyType visitVarDeclaration(VarDeclaration var) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitWaitStmt(WaitStmt waitStmt) {
        return null;
    }

    @Override
    public BlocklyType visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        return null;
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
        return sig.typeCheckParameter(functionPhrase, this, parameterPhrase);
    }

    /**
     * typecheck a predefined function with more than one parameter
     *
     * @param functName
     * @param functionPhrase
     * @param parameterPhrases
     * @return the return type of the function
     */
    protected final BlocklyType typeCheckPredefinedFunctionManyParams(FunctionNames functName, Phrase functionPhrase, List<Expr> parameterPhrases) {
        Sig sig = functName.signature;
        return sig.typeCheckParameter(functionPhrase, this, parameterPhrases.toArray(new Expr[parameterPhrases.size()]));
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

    public static void addErrorIfTypeCheckFailed(Phrase phrase, BlocklyType expectedType, BlocklyType actualType) {
        if ( !expectedType.equals(actualType) ) {
            String message = "expected was: " + expectedType + ", but found was: " + actualType;
            phrase.addInfo(NepoInfo.error(message));
        }
    }
}
