package de.fhg.iais.roberta.visitor.validate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
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
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.EvalStmts;
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
import de.fhg.iais.roberta.typecheck.NepoInfoProcessor;
import de.fhg.iais.roberta.typecheck.Sig;
import de.fhg.iais.roberta.util.dbc.Assert;
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
    private static final Set<BlocklyType> VALID_LIST_TYPES = new HashSet<>(Arrays.asList(
        BlocklyType.ARRAY_BOOLEAN,
        BlocklyType.ARRAY_COLOUR,
        BlocklyType.ARRAY_CONNECTION,
        BlocklyType.ARRAY_NUMBER,
        BlocklyType.ARRAY_STRING,
        BlocklyType.ARRAY_IMAGE
    ));


    /**
     * initialize the typecheck visitor.
     */
    public TypecheckCommonLanguageVisitor(UsedHardwareBean usedHardwareBean) {
        this.usedHardwareBean = usedHardwareBean;
    }

    public BlocklyType visitEvalExpr(EvalExpr evalExpr) {
        BlocklyType expectedType = evalExpr.getBlocklyType();
        Sig.of(expectedType, expectedType).typeCheckPhrases(evalExpr, this, evalExpr.exprAsBlock);
        NepoInfoProcessor.elevateNepoInfos(evalExpr);
        return expectedType;
    }

    public BlocklyType visitEvalStmts(EvalStmts evalStmts) {
        BlocklyType expectedType = evalStmts.getBlocklyType();
        Sig.of(expectedType, expectedType).typeCheckPhrases(evalStmts, this, evalStmts.stmtsAsBlock);
        NepoInfoProcessor.elevateNepoInfos(evalStmts);
        return expectedType;
    }

    @Override
    public BlocklyType visitAssertStmt(AssertStmt assertStmt) {
        return Sig.of(BlocklyType.VOID, BlocklyType.BOOLEAN).typeCheckPhrases(assertStmt, this, assertStmt.asserts);
    }

    @Override
    public BlocklyType visitAssignStmt(AssignStmt assignStmt) {
        BlocklyType lhs = this.usedHardwareBean.getTypeOfDeclaredVariable(assignStmt.name.name);
        return Sig.of(BlocklyType.VOID, lhs, lhs).typeCheckPhrases(assignStmt, this, assignStmt.name, assignStmt.expr);
    }

    @Override
    public BlocklyType visitBinary(Binary binary) {
        return binary.op.getSignature().typeCheckPhrases(binary, this, binary.left, binary.right);
    }

    @Override
    public BlocklyType visitBoolConst(BoolConst boolConst) {
        return Sig.of(BlocklyType.BOOLEAN).typeCheckPhrases(boolConst, this);
    }

    @Override
    public BlocklyType visitColorConst(ColorConst colorConst) {
        return Sig.of(BlocklyType.COLOR).typeCheckPhrases(colorConst, this);
    }

    @Override
    public BlocklyType visitDebugAction(DebugAction debugAction) {
        // really ANY, not PRIM only
        return Sig.of(BlocklyType.VOID, BlocklyType.ANY).typeCheckPhrases(debugAction, this, debugAction.value);
    }

    @Override
    public BlocklyType visitEmptyExpr(EmptyExpr emptyExpr) {
        return Sig.of(BlocklyType.NOTHING).typeCheckPhrases(emptyExpr, this);
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

        if ( getSubFunct.param.size() == 3 ) {
            return FunctionNames.GET_SUBLIST.signature.typeCheckPhraseList(getSubFunct, this, getSubFunct.param);
        } else if ( getSubFunct.param.size() == 2 ) {
            return Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE, BlocklyType.NUMBER).typeCheckPhraseList(getSubFunct, this, getSubFunct.param);
        } else if ( getSubFunct.param.size() == 1 ) {
            return Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE).typeCheckPhraseList(getSubFunct, this, getSubFunct.param);
        } else {
            getSubFunct.addTextlyError("Invalid number of parameters", true);
            return BlocklyType.VOID;
        }
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
            typeCheckPhrases(ternaryExpr, this, ternaryExpr.condition, ternaryExpr.thenPart, ternaryExpr.elsePart);
    }

    @Override
    public BlocklyType visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        return Sig.of(BlocklyType.NUMBER, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE_ARRAY_ITEM).typeCheckPhrases(indexOfFunct, this, indexOfFunct.value, indexOfFunct.find);
    }

    @Override
    public BlocklyType visitLengthOfListFunct(LengthOfListFunct lengthOfListFunct) {
        BlocklyType actualArrayType = lengthOfListFunct.value.accept(this);
        if ( VALID_LIST_TYPES.contains(actualArrayType) ) {
            return FunctionNames.LIST_LENGTH.signature.typeCheckPhrases(lengthOfListFunct, this, lengthOfListFunct.value);
        } else {
            lengthOfListFunct.addTextlyError("Cannot measure the length. The provided object is a " + actualArrayType + " but a list is expected", true);
            return BlocklyType.NOTHING;
        }
    }

    @Override
    public BlocklyType visitIsListEmptyFunct(IsListEmptyFunct isListEmptyFunct) {
        BlocklyType actualType = isListEmptyFunct.value.accept(this);
        if ( !actualType.isArray() ) {
            isListEmptyFunct.addTextlyError("expected was a list, but found was: " + actualType, true);
        }
        return BlocklyType.BOOLEAN;
    }

    @Override
    public BlocklyType visitListCreate(ListCreate listCreate) {
        BlocklyType expectedType = BlocklyType.CAPTURED_TYPE;
        if ( listCreate.exprList.el.size() == 0 ) {
            expectedType = listCreate.getBlocklyType();
        } else {
            expectedType = listCreate.exprList.el.get(0).getBlocklyType();
            if ( listCreate.exprList.el.get(0) instanceof Var ) {
                expectedType = usedHardwareBean.getTypeOfDeclaredVariable(((Var) listCreate.exprList.el.get(0)).name);
            } else if ( expectedType == BlocklyType.NULL || ((listCreate.exprList.el.get(0) instanceof ActionExpr) && expectedType.equalAsTypes(BlocklyType.CAPTURED_TYPE)) ) {
                expectedType = BlocklyType.CONNECTION;
            }
            for ( Phrase phrase : listCreate.exprList.get() ) {
                this.typeCheckPhrase(listCreate, phrase, expectedType);
            }
        }
        return expectedType.getMatchingArrayTypeForElementType();
    }

    @Override
    public BlocklyType visitListGetIndex(ListGetIndex listGetIndex) {
        switch ( listGetIndex.mode ) {
            case GET:
            case GET_REMOVE:
                if ( listGetIndex.location == IndexLocation.FIRST || listGetIndex.location == IndexLocation.LAST ) {
                    return Sig.of(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.CAPTURED_TYPE).typeCheckPhraseList(listGetIndex, this, listGetIndex.param);
                } else {
                    return Sig.of(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.CAPTURED_TYPE, BlocklyType.NUMBER).typeCheckPhraseList(listGetIndex, this, listGetIndex.param);
                }
            case REMOVE:
                if ( listGetIndex.location == IndexLocation.FIRST || listGetIndex.location == IndexLocation.LAST ) {
                    return Sig.of(BlocklyType.VOID, BlocklyType.CAPTURED_TYPE).typeCheckPhraseList(listGetIndex, this, listGetIndex.param);
                } else {
                    return Sig.of(BlocklyType.VOID, BlocklyType.CAPTURED_TYPE, BlocklyType.NUMBER).typeCheckPhraseList(listGetIndex, this, listGetIndex.param);
                }
            default:
                throw new UnsupportedOperationException("Invalid list get index mode. Expected 'GET', 'GET_REMOVE', or 'REMOVE'.");
        }
    }

    @Override
    public BlocklyType visitListRepeat(ListRepeat listRepeat) {
        return FunctionNames.LISTS_REPEAT.signature.typeCheckPhraseList(listRepeat, this, listRepeat.param);
    }

    @Override
    public BlocklyType visitListSetIndex(ListSetIndex listSetIndex) {
        Sig sig = null;
        // If the IndexLocation is "FIRST" or "LAST", there are 2 parameters:
        // one for the list and one for the new value to be set at the specified index.
        // If the IndexLocation is "FROM_START" or "FROM_END", there are 3 parameters:
        // the list, the new value, and an additional number indicating the exact index position.
        if ( listSetIndex.param.size() == 2 ) {
            sig = Sig.of(BlocklyType.VOID, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE_ARRAY_ITEM);
        } else {
            sig = Sig.of(BlocklyType.VOID, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.NUMBER);
        }
        return sig.typeCheckPhraseList(listSetIndex, this, listSetIndex.param);
    }

    @Override
    public BlocklyType visitMainTask(MainTask mainTask) {
        mainTask.variables.accept(this);
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(mainTask, this);
    }

    @Override
    public BlocklyType visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        return Sig.of(BlocklyType.STRING, BlocklyType.NUMBER).typeCheckPhrases(mathCastCharFunct, this, mathCastCharFunct.value);
    }

    @Override
    public BlocklyType visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        return Sig.of(BlocklyType.STRING, BlocklyType.NUMBER).typeCheckPhrases(mathCastStringFunct, this, mathCastStringFunct.value);
    }

    @Override
    public BlocklyType visitMathChangeStmt(MathChangeStmt mathChangeStmt) {
        BlocklyType varType = usedHardwareBean.getTypeOfDeclaredVariable(((Var) mathChangeStmt.var).name);
        if ( varType != BlocklyType.NOTHING ) {
            return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(mathChangeStmt, this, mathChangeStmt.var, mathChangeStmt.delta);
        } else {
            mathChangeStmt.addTextlyError("Invalid variable name for this function", true);
            return BlocklyType.NOTHING;
        }
    }

    @Override
    public BlocklyType visitMathConst(MathConst mathConst) {
        String name = mathConst.mathConst.name();
        Sig sig = FunctionNames.get(name).signature;
        return sig.typeCheckPhraseList(mathConst, this, Collections.emptyList());
    }

    @Override
    public BlocklyType visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        return Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(mathConstrainFunct, this, mathConstrainFunct.value, mathConstrainFunct.lowerBound, mathConstrainFunct.upperBound);
    }

    @Override
    public BlocklyType visitMathModuloFunct(MathModuloFunct mathModuloFunct) {
        return Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER)
            .typeCheckPhrases(mathModuloFunct, this, mathModuloFunct.divisor, mathModuloFunct.dividend);
    }

    @Override
    public BlocklyType visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        return mathNumPropFunct.functName.signature.typeCheckPhraseList(mathNumPropFunct, this, mathNumPropFunct.param);
    }

    @Override
    public BlocklyType visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        return mathOnListFunct.functName.signature.typeCheckPhrases(mathOnListFunct, this, mathOnListFunct.list);
    }

    @Override
    public BlocklyType visitMathPowerFunct(MathPowerFunct func) {
        return FunctionNames.get(func.functName.name()).signature.typeCheckPhraseList(func, this, func.param);
    }

    @Override
    public BlocklyType visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        return FunctionNames.RANDOM_DOUBLE.signature.typeCheckPhrases(mathRandomFloatFunct, this);
    }

    @Override
    public BlocklyType visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        return Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(mathRandomIntFunct, this, mathRandomIntFunct.from, mathRandomIntFunct.to);
    }

    @Override
    public BlocklyType visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        return mathSingleFunct.functName.signature.typeCheckPhraseList(mathSingleFunct, this, mathSingleFunct.param);
    }

    @Override
    public BlocklyType visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        return Sig.of(BlocklyType.VOID, BlocklyType.BOOLEAN).typeCheckPhrases(methodIfReturn, this, methodIfReturn.oraCondition);
    }

    @Override
    public BlocklyType visitMethodReturn(MethodReturn methodReturn) {
        methodReturn.returnValue.accept(this);
        methodReturn.body.accept(this);
        return BlocklyType.NOTHING;
    }

    @Override
    public BlocklyType visitMethodStmt(MethodStmt methodStmt) {
        return methodStmt.method.accept(this);
    }

    @Override
    public BlocklyType visitMethodCall(MethodCall methodCall) {
        boolean methodExists = false;
        List<Method> methods = usedHardwareBean.getUserDefinedMethods();
        for ( Method m : methods ) {
            if ( m.getMethodName().equals(methodCall.getMethodName()) ) {
                methodExists = true;
                break;
            }
        }

        if ( methodExists ) {
            return usedHardwareBean.getSignatureOfMethod(methodCall.getMethodName()).typeCheckPhraseList(methodCall, this, methodCall.getParametersValues().el);
        } else {
            methodCall.addTextlyError("Invalid Function name or Expression", true);
            return BlocklyType.NOTHING;
        }

    }

    @Override
    public BlocklyType visitMethodVoid(MethodVoid methodVoid) {
        methodVoid.body.accept(this);
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(methodVoid, this);
    }

    @Override
    public BlocklyType visitNNStepStmt(NNStepStmt nnStepStmt) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(nnStepStmt, this);
    }

    @Override
    public BlocklyType visitNNSetInputNeuronVal(NNSetInputNeuronVal nnSetInputNeuronVal) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(nnSetInputNeuronVal, this, nnSetInputNeuronVal.value);
    }


    @Override
    public BlocklyType visitNNGetOutputNeuronVal(NNGetOutputNeuronVal nnGetOutputNeuronVal) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(nnGetOutputNeuronVal, this);
    }

    @Override
    public BlocklyType visitNNSetWeightStmt(NNSetWeightStmt nnSetWeightStmt) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(nnSetWeightStmt, this, nnSetWeightStmt.value);
    }

    @Override
    public BlocklyType visitNNSetBiasStmt(NNSetBiasStmt nnSetBiasStmt) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(nnSetBiasStmt, this, nnSetBiasStmt.value);
    }

    @Override
    public BlocklyType visitNNGetWeight(NNGetWeight nnGetWeight) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(nnGetWeight, this);
    }

    @Override
    public BlocklyType visitNNGetBias(NNGetBias nnGetBias) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(nnGetBias, this);
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
        switch ( repeatStmt.mode.name() ) {
            case "TIMES":
            case "FOR":
                int exprListSize = ((ExprList) repeatStmt.expr).el.size();
                if ( exprListSize == 4 ) {
                    for ( int i = 0; i < exprListSize; i++ ) {
                        typeCheckPhrase(repeatStmt, ((ExprList) repeatStmt.expr).el.get(i), BlocklyType.NUMBER);
                    }
                }
                break;
            case "UNTIL":
            case "WHILE":
            case "FOREVER":
                typeCheckPhrase(repeatStmt, repeatStmt.expr, BlocklyType.BOOLEAN);
                break;
            case "FOR_EACH":
                Binary exprBinary = (Binary) repeatStmt.expr;
                BlocklyType listType = typeCheckPhrase(repeatStmt, exprBinary.right, BlocklyType.CAPTURED_TYPE);
                BlocklyType varType = exprBinary.left instanceof VarDeclaration ? exprBinary.left.getBlocklyType() : BlocklyType.NOTHING;
                if ( listType.isArray() ) {
                    if ( listType.getMatchingElementTypeForArrayType().equalAsTypes(varType) ) {
                        break;
                    } else {
                        repeatStmt.addTextlyError("a list of " + exprBinary.left.getBlocklyType().getBlocklyName() + " was expected but it was found a list of " + listType.getMatchingElementTypeForArrayType().toString().toLowerCase(), true);
                        break;
                    }
                } else {
                    repeatStmt.addTextlyError("this control statement is only for a list of numbers, images, strings or booleans", true);
                    break;
                }
            case "WAIT":
                typeCheckPhrase(repeatStmt, repeatStmt.expr, BlocklyType.BOOLEAN);
                break;

            default:
                repeatStmt.addTextlyError("invalid repeat mode. Expected 'TIMES', 'FOR', 'UNTIL', 'WHILE', 'FOREVER', 'FOR_EACH', or 'WAIT'.", true);
        }
        typeCheckPhrase(repeatStmt, repeatStmt.list, BlocklyType.VOID);
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitRgbColor(RgbColor rgbColor) {
        Assert.isTrue(rgbColor.getKind().hasName("RGB_COLOR"));
        if ( rgbColor.getParameters().length == 0 ) {
            return BlocklyType.COLOR;
        } else if ( rgbColor.getParameters().length == 3 ) {
            return Sig.of(BlocklyType.COLOR, BlocklyType.VARARGS, BlocklyType.NUMBER).typeCheckPhrases(rgbColor, this, rgbColor.R, rgbColor.G);
        } else if ( rgbColor.getParameters().length == 4 ) {
            return Sig.of(BlocklyType.COLOR, BlocklyType.VARARGS, BlocklyType.NUMBER).typeCheckPhrases(rgbColor, this, rgbColor.R, rgbColor.G, rgbColor.B);
        } else {
            rgbColor.addTextlyError("getRGB can only have 0,3 or 4 parameters", true);
            return null;
        }
    }

    @Override
    public BlocklyType visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        BlocklyType actualType = serialWriteAction.value.accept(this);
        if ( actualType.isArray() ) {
            serialWriteAction.addTextlyError("A list of " + actualType.getMatchingElementTypeForArrayType().toString().toLowerCase() + " are not supported for this Function", true);
        } else if ( actualType.equalAsTypes(BlocklyType.IMAGE) ) {
            serialWriteAction.addTextlyError("Images are not supported for this Function", true);
        }
        return Sig.of(BlocklyType.VOID, BlocklyType.ANY).typeCheckPhrases(serialWriteAction, this, serialWriteAction.value);
    }

    @Override
    public BlocklyType visitStmtFlowCon(StmtFlowCon stmtFlowCon) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(stmtFlowCon, this);
    }

    @Override
    public BlocklyType visitStmtList(StmtList stmtList) {
        stmtList.sl.stream().forEach(s -> typeCheckPhrase(stmtList, s, BlocklyType.VOID));
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitStmtTextComment(StmtTextComment stmtTextComment) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(stmtTextComment, this);
    }

    @Override
    public BlocklyType visitStringConst(StringConst stringConst) {
        Assert.isTrue(stringConst.getKind().hasName("STRING_CONST"));
        return BlocklyType.STRING;
    }

    @Override
    public BlocklyType visitTextAppendStmt(TextAppendStmt textAppendStmt) {
        return Sig.of(BlocklyType.VOID, BlocklyType.STRING, BlocklyType.PRIM).typeCheckPhrases(textAppendStmt, this, textAppendStmt.var, textAppendStmt.text);
    }

    @Override
    public BlocklyType visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        return Sig.of(BlocklyType.NUMBER, BlocklyType.STRING, BlocklyType.NUMBER).typeCheckPhrases(textCharCastNumberFunct, this, textCharCastNumberFunct.value, textCharCastNumberFunct.atIndex);
    }

    @Override
    public BlocklyType visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        return Sig.of(BlocklyType.STRING, BlocklyType.VARARGS, BlocklyType.PRIM).typeCheckPhraseList(textJoinFunct, this, textJoinFunct.param.el);
    }

    @Override
    public BlocklyType visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(textPrintFunct, this);
    }

    @Override
    public BlocklyType visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        return Sig.of(BlocklyType.NUMBER, BlocklyType.STRING).typeCheckPhrases(textStringCastNumberFunct, this, textStringCastNumberFunct.value);
    }

    @Override
    public BlocklyType visitTimerSensor(TimerSensor timerSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(timerSensor, this);
    }

    @Override
    public BlocklyType visitTimerReset(TimerReset timerReset) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(timerReset, this);
    }

    @Override
    public BlocklyType visitUnary(Unary unary) {
        return unary.op.signature.typeCheckPhrases(unary, this, unary.expr);
    }

    @Override
    public BlocklyType visitVar(Var var) {
        BlocklyType varType = usedHardwareBean.getTypeOfDeclaredVariable(var.name);
        if ( varType.equalAsTypes(BlocklyType.NOTHING) ) {
            String message = "no type found for variable: " + var.name;
            var.addTextlyError("unknown variable: " + var.name, true);
        }
        var.setBlocklyTypeVar(varType);
        return varType;
    }

    @Override
    public BlocklyType visitVarDeclaration(VarDeclaration var) {
        return Sig.of(BlocklyType.VOID, var.getBlocklyType()).typeCheckPhrases(var, this, var.value);
    }

    @Override
    public BlocklyType visitWaitStmt(WaitStmt waitStmt) {
        typeCheckPhrase(waitStmt, waitStmt.statements, BlocklyType.VOID);
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(waitStmt, this);
    }

    @Override
    public BlocklyType visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(waitTimeStmt, this, waitTimeStmt.time);
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
        return Sig.of(expectedType, expectedType).typeCheckPhrases(parentPhrase, this, phraseToTypeCheck);
    }
}
