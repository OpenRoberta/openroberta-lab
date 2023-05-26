package de.fhg.iais.roberta.visitor.lang.codegen.prog;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.text.StringEscapeUtils;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
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
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
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
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MathChangeStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.TextAppendStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.FunctionNames;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C++ code representation of a phrase to a
 * StringBuilder. <b>This representation is correct C++ code.</b> <br>
 */
public abstract class AbstractCppVisitor extends AbstractLanguageVisitor {
    /**
     * initialize the cpp code generator visitor.
     */
    protected AbstractCppVisitor(List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
    }

    @Override
    public Void visitRgbColor(RgbColor rgbColor) {
        this.src.add("RGB(");
        rgbColor.R.accept(this);
        this.src.add(", ");
        rgbColor.G.accept(this);
        this.src.add(", ");
        rgbColor.B.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst nullConst) {
        this.src.add("NULL");
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                this.src.add("\"\"");
                break;
            case BOOLEAN:
                this.src.add("true");
                break;
            case NUMBER:
            case NUMBER_INT:
                this.src.add("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            default:
                this.src.add("NULL");
                break;
        }
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt assignStmt) {
        super.visitAssignStmt(assignStmt);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt exprStmt) {
        super.visitExprStmt(exprStmt);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt functionStmt) {
        super.visitFunctionStmt(functionStmt);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon stmtFlowCon) {
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) != null ) {
            if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) ) {
                this.src.add("goto ", stmtFlowCon.flow.toString().toLowerCase(), "_loop", this.currentLoop.getLast(), ";");
                return null;
            }
        }
        this.src.add(stmtFlowCon.flow.toString().toLowerCase(), ";");
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList emptyList) {
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate listCreate) {
        this.src.add("{");
        listCreate.exprList.accept(this);
        this.src.add("}");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat listRepeat) {
        // This implementation assumes that robots providing a "list repeat" block have implemented a method "_createListRepeat(counter, element)" in one of the included files.
        this.src.add("_createListRepeat(");
        listRepeat.getCounter().accept(this);
        this.src.add(", ");
        BlocklyType itemType = listRepeat.getElement().getVarType();
        if ( itemType.equals(BlocklyType.NUMBER) || itemType.equals(BlocklyType.STRING) ) {
            this.src.add("(", getLanguageVarTypeFromBlocklyType(itemType), ") ");
        }
        listRepeat.getElement().accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst mathConst) {
        switch ( mathConst.mathConst ) {
            case PI:
                this.src.add("M_PI");
                break;
            case E:
                this.src.add("M_E");
                break;
            case GOLDEN_RATIO:
                this.src.add("M_GOLDEN_RATIO");
                break;
            case SQRT2:
                this.src.add("M_SQRT2");
                break;
            case SQRT1_2:
                this.src.add("M_SQRT1_2");
                break;
            // IEEE 754 floating point representation
            case INFINITY:
                this.src.add("M_INFINITY");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        String methodName = "_getFirstOccuranceOfElement(";
        if ( indexOfFunct.location != IndexLocation.FIRST ) {
            methodName = "_getLastOccuranceOfElement(";
        }
        this.src.add(methodName);

        indexOfFunct.value.accept(this);
        this.src.add(", ");
        indexOfFunct.find.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct getSubFunct) {
        if ( getSubFunct.functName == FunctionNames.GET_SUBLIST ) {
            this.src.add("_getSubList(");
            getSubFunct.param.get(0).accept(this);
            this.src.add(", ");
            switch ( (IndexLocation) getSubFunct.strParam.get(0) ) {
                case FIRST:
                    this.src.add("0, ");
                    break;
                case FROM_END:
                    getSubFunct.param.get(0).accept(this);
                    this.src.add(".size() - 1 - ");
                    getSubFunct.param.get(1).accept(this);
                    this.src.add(", ");
                    break;
                case FROM_START:
                    getSubFunct.param.get(1).accept(this);
                    this.src.add(", ");
                    break;
                default:
                    break;
            }
            switch ( (IndexLocation) getSubFunct.strParam.get(1) ) {
                case LAST:
                    getSubFunct.param.get(0).accept(this);
                    this.src.add(".size() - 1");
                    break;
                case FROM_END:
                    getSubFunct.param.get(0).accept(this);
                    this.src.add(".size() - 1 - ");
                    try {
                        getSubFunct.param.get(2).accept(this);
                    } catch ( IndexOutOfBoundsException e ) { // means that our start index does not have a variable
                        getSubFunct.param.get(1).accept(this);
                    }
                    break;
                case FROM_START:
                    try {
                        getSubFunct.param.get(2).accept(this);
                    } catch ( IndexOutOfBoundsException e ) { // means that our start index does not have a variable
                        getSubFunct.param.get(1).accept(this);
                    }
                    break;
                default:
                    break;
            }
            this.src.add(")");
        }
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex listGetIndex) {
        String operation = "";
        switch ( (ListElementOperations) listGetIndex.getElementOperation() ) {
            case GET:
                operation = "_getListElementByIndex(";
                break;
            case GET_REMOVE:
                operation = "_getAndRemoveListElementByIndex(";
                break;
            case INSERT:
                break;
            case REMOVE:
                operation = "_removeListElementByIndex(";
                break;
            case SET:
                break;
            default:
                break;
        }
        this.src.add(operation);
        switch ( (IndexLocation) listGetIndex.location ) {
            case FIRST:
                listGetIndex.param.get(0).accept(this);
                this.src.add(", 0)");
                break;
            case FROM_END:
                listGetIndex.param.get(0).accept(this);
                this.src.add(", ");
                listGetIndex.param.get(0).accept(this);
                this.src.add(".size() - 1 - ");
                listGetIndex.param.get(1).accept(this);
                this.src.add(")");
                break;
            case FROM_START:
                listGetIndex.param.get(0).accept(this);
                this.src.add(", ");
                listGetIndex.param.get(1).accept(this);
                this.src.add(")");
                break;
            case LAST:
                listGetIndex.param.get(0).accept(this);
                this.src.add(", ");
                listGetIndex.param.get(0).accept(this);
                this.src.add(".size() - 1)");
                break;
            case RANDOM:
                listGetIndex.param.get(0).accept(this);
                this.src.add(", 0 /* absolutely random number */)");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex listSetIndex) {
        String operation = "";
        switch ( (ListElementOperations) listSetIndex.mode ) {
            case GET:
                break;
            case GET_REMOVE:
                break;
            case INSERT:
                if ( ((IndexLocation) listSetIndex.location).equals(IndexLocation.LAST) ) {
                    listSetIndex.param.get(0).accept(this);
                    this.src.add(".push_back(");
                    listSetIndex.param.get(1).accept(this);
                    this.src.add(")");
                    return null;
                } else {
                    operation = "_insertListElementBeforeIndex(";
                }
                break;
            case REMOVE:
                break;
            case SET:
                operation = "_setListElementByIndex(";
                break;
            default:
                break;
        }
        this.src.add(operation);
        switch ( (IndexLocation) listSetIndex.location ) {
            case FIRST:
                listSetIndex.param.get(0).accept(this);
                this.src.add(", 0, ");
                listSetIndex.param.get(1).accept(this);
                this.src.add(")");
                break;
            case FROM_END:
                listSetIndex.param.get(0).accept(this);
                this.src.add(", ");
                listSetIndex.param.get(0).accept(this);
                this.src.add(".size() - 1 - ");
                listSetIndex.param.get(2).accept(this);
                this.src.add(", ");
                listSetIndex.param.get(1).accept(this);
                this.src.add(")");
                break;
            case FROM_START:
                listSetIndex.param.get(0).accept(this);
                this.src.add(", ");
                listSetIndex.param.get(2).accept(this);
                this.src.add(", ");
                listSetIndex.param.get(1).accept(this);
                this.src.add(")");
                break;
            case LAST:
                listSetIndex.param.get(0).accept(this);
                this.src.add(", ");
                listSetIndex.param.get(0).accept(this);
                this.src.add(".size() - 1, ");
                listSetIndex.param.get(1).accept(this);
                this.src.add(")");
                break;
            case RANDOM:
                listSetIndex.param.get(0).accept(this);
                this.src.add(", 0 /* absolutely random number */, ");
                listSetIndex.param.get(1).accept(this);
                this.src.add(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLengthOfListFunct(LengthOfListFunct lengthOfListFunct) {
        this.src.add("((int) ");
        lengthOfListFunct.value.accept(this);
        this.src.add(".size())");
        return null;
    }

    @Override
    public Void visitIsListEmptyFunct(IsListEmptyFunct isListEmptyFunct) {
        isListEmptyFunct.value.accept(this);
        this.src.add(".empty()");
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        switch ( mathOnListFunct.functName ) {
            case AVERAGE:
                this.src.add("_getListAverage(");
                break;
            case MAX:
                this.src.add("_getListMax(");
                break;
            case MEDIAN:
                this.src.add("_getListMedian(");
                break;
            case MIN:
                this.src.add("_getListMin(");
                break;
            case STD_DEV:
                this.src.add("_getListStandardDeviation(");
                break;
            case SUM:
                this.src.add("_getListSum(");
                break;
            case RANDOM:
                // TODO it has no implementation and should probably be removed from blockly as well
                this.src.add("_getListElementByIndex(");
                mathOnListFunct.list.accept(this);
                this.src.add(", 0)");
                return null;
            default:
                break;
        }
        mathOnListFunct.list.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        boolean extraPar = false;
        switch ( mathSingleFunct.functName ) {
            case SQUARE:
                this.src.add("pow(");
                mathSingleFunct.param.get(0).accept(this);
                this.src.add(", 2)");
                return null;
            case ROOT:
                this.src.add("sqrt(");
                break;
            case ABS:
                this.src.add("abs(");
                break;
            case LN:
                this.src.add("log(");
                break;
            case LOG10:
                this.src.add("log10(");
                break;
            case EXP:
                this.src.add("exp(");
                break;
            case POW10:
                this.src.add("pow(10.0, ");
                break;
            case SIN:
                extraPar = true;
                this.src.add("sin(M_PI / 180.0 * (");
                break;
            case COS:
                extraPar = true;
                this.src.add("cos(M_PI / 180.0 * (");
                break;
            case TAN:
                extraPar = true;
                this.src.add("tan(M_PI / 180.0 * (");
                break;
            case ASIN:
                this.src.add("180.0 / M_PI * asin(");
                break;
            case ATAN:
                this.src.add("180.0 / M_PI * atan(");
                break;
            case ACOS:
                this.src.add("180.0 / M_PI * acos(");
                break;
            case ROUND:
                this.src.add("round(");
                break;
            case ROUNDUP:
                this.src.add("ceil(");
                break;
            case ROUNDDOWN:
                this.src.add("floor(");
                break;
            default:
                break;
        }
        mathSingleFunct.param.get(0).accept(this);
        if ( extraPar ) {
            this.src.add(")");
        }
        this.src.add(")");

        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        Expr n = mathConstrainFunct.value;
        Expr min = mathConstrainFunct.lowerBound;
        Expr max = mathConstrainFunct.upperBound;
        this.src.add("std::min(std::max((double) ");
        n.accept(this);
        this.src.add(", (double) ");
        min.accept(this);
        this.src.add("), (double) ");
        max.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        switch ( mathNumPropFunct.functName ) {
            case EVEN:
                this.src.add("(fmod(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(", 2) == 0");
                break;
            case ODD:
                this.src.add("(fmod(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(", 2) != 0");
                break;
            case PRIME:
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.PRIME));
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                break;
            case WHOLE:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" == floor(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(")");
                break;
            case POSITIVE:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" > 0");
                break;
            case NEGATIVE:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" < 0");
                break;
            case DIVISIBLE_BY:
                this.src.add("(fmod(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(",");
                mathNumPropFunct.param.get(1).accept(this);
                this.src.add(") == 0");
                break;
            default:
                break;
        }
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        this.src.add("pow(");
        return super.visitMathPowerFunct(mathPowerFunct);
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        this.src.add("((double) rand() / (RAND_MAX))");
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        // TODO check why this is not working for Arduinos!
        this.src.add("(std::to_string(");
        mathCastStringFunct.value.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        this.src.add("(char)(int)(");
        mathCastCharFunct.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathChangeStmt(MathChangeStmt mathChangeStmt) {
        super.visitMathChangeStmt(mathChangeStmt);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitMathModuloFunct(MathModuloFunct mathModuloFunct) {
        this.src.add("fmod((");
        mathModuloFunct.dividend.accept(this);
        this.src.add("), (");
        mathModuloFunct.divisor.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitTextAppendStmt(TextAppendStmt textAppendStmt) {
        textAppendStmt.var.accept(this);
        this.src.add(" += ");
        textAppendStmt.text.accept(this);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        this.src.add("std::stof(");
        textStringCastNumberFunct.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        this.src.add("(int)(");
        this.src.add("(");
        textCharCastNumberFunct.value.accept(this);
        this.src.add(")[");
        textCharCastNumberFunct.atIndex.accept(this);
        this.src.add("])");
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid methodVoid) {
        nlIndent();
        this.src.add("void ");
        this.src.add(methodVoid.getCodeSafeMethodName(), "(");
        methodVoid.getParameters().accept(this);
        this.src.add(") {");
        incrIndentation();
        methodVoid.body.accept(this);
        decrIndentation();
        nlIndent();
        this.src.add("}");
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn methodReturn) {
        nlIndent();
        this.src.add(getLanguageVarTypeFromBlocklyType(methodReturn.getReturnType()));
        this.src.add(" ", methodReturn.getCodeSafeMethodName(), "(");
        methodReturn.getParameters().accept(this);
        this.src.add(") {");
        incrIndentation();
        methodReturn.body.accept(this);
        nlIndent();
        this.src.add("return ");
        methodReturn.returnValue.accept(this);
        this.src.add(";");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        this.src.add("if (");
        methodIfReturn.oraCondition.accept(this);
        this.src.add(") ");
        this.src.add("return ");
        methodIfReturn.oraReturnValue.accept(this);
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt methodStmt) {
        super.visitMethodStmt(methodStmt);
        if ( methodStmt.getProperty().getBlockType().equals("robProcedures_ifreturn") ) {
            this.src.add(";");
        }
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall methodCall) {
        super.visitMethodCall(methodCall);
        if ( methodCall.getReturnType() == BlocklyType.VOID ) {
            this.src.add(";");
        }
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        return null;
    }

    @Override
    public Void visitStringConst(StringConst stringConst) {
        this.src.add("\"", StringEscapeUtils.escapeJava(stringConst.value.replaceAll("[<>\\$]", "")), "\"");
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt assertStmt) {
        // please overwrite this in the robot-specific class and throw an exeption if "assertNepo" could not be provided
        this.src.add("assertNepo((");
        assertStmt.asserts.accept(this);
        this.src.add("), \"", assertStmt.msg, "\", ");
        ((Binary) assertStmt.asserts).left.accept(this);
        this.src.add(", \"", ((Binary) assertStmt.asserts).op.toString(), "\", ");
        ((Binary) assertStmt.asserts).getRight().accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        throw new UnsupportedOperationException("should be overriden in a robot-specific class");
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        throw new UnsupportedOperationException("should be overriden in a robot-specific class");
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        throw new UnsupportedOperationException("should be overriden in a robot-specific class");
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        this.src.add("Serial.println(");
        serialWriteAction.value.accept(this);
        this.src.add(");");
        return null;
    }

    protected void addContinueLabelToLoop() {
        Integer lastLoop = this.currentLoop.getLast();
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(lastLoop) ) {
            nlIndent();
            this.src.add("continue_loop", this.currentLoop.getLast(), ":;");
        }
    }

    protected void addBreakLabelToLoop(boolean isWaitStmt) {
        if ( !isWaitStmt ) {
            if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) ) {
                nlIndent();
                this.src.add("break_loop", this.currentLoop.getLast(), ":;");
                nlIndent();
            }
            this.currentLoop.removeLast();
        }
    }

    @Override
    protected String getLanguageVarTypeFromBlocklyType(BlocklyType type) {
        switch ( type ) {
            case ANY:
            case COMPARABLE:
            case ADDABLE:
            case NULL:
            case REF:
            case PRIM:
            case NOTHING:
            case CAPTURED_TYPE:
            case R:
            case S:
            case T:
                return "";
            case ARRAY:
                return "std::list<double>";
            case ARRAY_NUMBER:
                return "std::list<double>";
            case ARRAY_STRING:
                return "std::list<String>";
            case ARRAY_BOOLEAN:
                return "std::list<bool>";
            case ARRAY_COLOUR:
                return "std::list<unsigned int>";
            case BOOLEAN:
                return "bool";
            case NUMBER:
                return "double";
            case NUMBER_INT:
                return "int";
            case STRING:
                return "String";
            case VOID:
                return "void";
            case COLOR:
                return "unsigned int";
            case CONNECTION:
                return "int";
            default:
                throw new IllegalArgumentException("unhandled type");
        }
    }

    @Override
    protected void generateCodeFromTernary(TernaryExpr ternaryExpr) {
        this.src.add("( ", "( ");
        ternaryExpr.condition.accept(this);
        this.src.add(" ) ", "? ", "( ");
        ternaryExpr.thenPart.accept(this);
        this.src.add(" ) ", ": ", "( ");
        ternaryExpr.elsePart.accept(this);
        this.src.add(") ", ")");
    }

    @Override
    protected void generateCodeFromIfElse(IfStmt ifStmt) {
        int exprSize = ifStmt.expr.size();
        String conditionStmt = "if";
        for ( int i = 0; i < exprSize; i++ ) {
            generateCodeFromStmtCondition(conditionStmt, ifStmt.expr.get(i));
            conditionStmt = "else if";
            incrIndentation();
            ifStmt.thenList.get(i).accept(this);
            decrIndentation();
            if ( i + 1 < exprSize ) {
                nlIndent();
                this.src.add("} ");
            }
        }

    }

    @Override
    protected void generateCodeFromElse(IfStmt ifStmt) {
        if ( !ifStmt.elseList.get().isEmpty() ) {
            nlIndent();
            this.src.add("} else {");
            incrIndentation();
            ifStmt.elseList.accept(this);
            decrIndentation();
        }
        nlIndent();
        this.src.add("}");
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( withWrapping ) {
            generateSignaturesOfUserDefinedMethods();
            if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
                nlIndent();
                String helperMethodImpls =
                    this
                        .getBean(CodeGeneratorSetupBean.class)
                        .getHelperMethodGenerator()
                        .getHelperMethodDeclarations(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
                Iterator<String> it = Arrays.stream(helperMethodImpls.split("\n")).iterator();
                while ( it.hasNext() ) {
                    this.src.add(it.next());
                    if ( it.hasNext() ) {
                        nlIndent();
                    }
                }
            }
            generateNNStuff("c++");
        }
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
                String helperMethodImpls =
                    this
                        .getBean(CodeGeneratorSetupBean.class)
                        .getHelperMethodGenerator()
                        .getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
                Iterator<String> it = Arrays.stream(helperMethodImpls.split("\n")).iterator();
                while ( it.hasNext() ) {
                    this.src.add(it.next());
                    if ( it.hasNext() ) {
                        nlIndent();
                    }
                }
                nlIndent();
            }
        }
    }

    protected void generateSignaturesOfUserDefinedMethods() {
        for ( Method phrase : this.getBean(UsedHardwareBean.class).getUserDefinedMethods() ) {
            nlIndent();
            this.src.add(getLanguageVarTypeFromBlocklyType(phrase.getReturnType()), " ", phrase.getCodeSafeMethodName(), "(");
            phrase.getParameters().accept(this);
            this.src.add(");");
        }
    }

    protected void generateCodeFromStmtCondition(String stmtType, Expr expr) {
        this.src.add(stmtType, " ( ");
        expr.accept(this);
        this.src.add(" ) ", "{");
    }

    protected void generateCodeFromStmtConditionFor(String stmtType, Expr expr) {
        this.src.add(stmtType, " (", "int ");
        final ExprList expressions = (ExprList) expr;
        expressions.get().get(0).accept(this);
        this.src.add(" = ");
        expressions.get().get(1).accept(this);
        this.src.add("; ");
        expressions.get().get(0).accept(this);
        this.src.add(" ");
        this.src.add("< ");
        expressions.get().get(2).accept(this);
        this.src.add("; ");
        expressions.get().get(0).accept(this);
        this.src.add(" ");
        this.src.add("+= ");
        expressions.get().get(3).accept(this);
        this.src.add(") ", "{");
    }

    protected void appendBreakStmt() {
        nlIndent();
        this.src.add("break;");
    }

    @Override
    protected String getBinaryOperatorSymbol(Binary.Op op) {
        return binaryOpSymbols().get(op);
    }

    @Override
    protected String getUnaryOperatorSymbol(Unary.Op op) {
        return unaryOpSymbols().get(op);
    }

    protected static Map<Binary.Op, String> binaryOpSymbols() {
        return Collections
            .unmodifiableMap(
                Stream
                    .of(

                        entry(Binary.Op.ADD, "+"),
                        entry(Binary.Op.MINUS, "-"),
                        entry(Binary.Op.MULTIPLY, "*"),
                        entry(Binary.Op.DIVIDE, "/"),
                        entry(Binary.Op.MOD, "%"),
                        entry(Binary.Op.EQ, "=="),
                        entry(Binary.Op.NEQ, "!="),
                        entry(Binary.Op.LT, "<"),
                        entry(Binary.Op.LTE, "<="),
                        entry(Binary.Op.GT, ">"),
                        entry(Binary.Op.GTE, ">="),
                        entry(Binary.Op.AND, "&&"),
                        entry(Binary.Op.OR, "||"),
                        entry(Binary.Op.IN, ":"),
                        entry(Binary.Op.ASSIGNMENT, "="),
                        entry(Binary.Op.ADD_ASSIGNMENT, "+="),
                        entry(Binary.Op.MINUS_ASSIGNMENT, "-="),
                        entry(Binary.Op.MULTIPLY_ASSIGNMENT, "*="),
                        entry(Binary.Op.DIVIDE_ASSIGNMENT, "/="),
                        entry(Binary.Op.MOD_ASSIGNMENT, "%=")

                    )
                    .collect(entriesToMap()));
    }

    protected static Map<Unary.Op, String> unaryOpSymbols() {
        return Collections
            .unmodifiableMap(
                Stream
                    .of(

                        entry(Unary.Op.PLUS, "+"),
                        entry(Unary.Op.NEG, "-"),
                        entry(Unary.Op.NOT, "!"),
                        entry(Unary.Op.POSTFIX_INCREMENTS, "++"),
                        entry(Unary.Op.PREFIX_INCREMENTS, "++")

                    )
                    .collect(entriesToMap()));
    }

}
