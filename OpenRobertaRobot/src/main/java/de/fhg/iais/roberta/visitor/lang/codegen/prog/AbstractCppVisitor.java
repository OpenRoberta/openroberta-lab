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
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
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
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
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
        this.sb.append("RGB(");
        rgbColor.R.accept(this);
        this.sb.append(", ");
        rgbColor.G.accept(this);
        this.sb.append(", ");
        rgbColor.B.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst nullConst) {
        this.sb.append("NULL");
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst connectConst) {
        this.sb.append(connectConst.value);
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                this.sb.append("\"\"");
                break;
            case BOOLEAN:
                this.sb.append("true");
                break;
            case NUMBER:
            case NUMBER_INT:
                this.sb.append("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            default:
                this.sb.append("NULL");
                break;
        }
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt assignStmt) {
        super.visitAssignStmt(assignStmt);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt exprStmt) {
        super.visitExprStmt(exprStmt);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt functionStmt) {
        super.visitFunctionStmt(functionStmt);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon stmtFlowCon) {
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) != null ) {
            if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) ) {
                this.sb.append("goto " + stmtFlowCon.flow.toString().toLowerCase() + "_loop" + this.currentLoop.getLast() + ";");
                return null;
            }
        }
        this.sb.append(stmtFlowCon.flow.toString().toLowerCase() + ";");
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList emptyList) {
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate listCreate) {
        this.sb.append("{");
        listCreate.exprList.accept(this);
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat listRepeat) {
        // This implementation assumes that robots providing a "list repeat" block have implemented a method "_createListRepeat(counter, element)" in one of the included files.
        this.sb.append("_createListRepeat(");
        listRepeat.getCounter().accept(this);
        this.sb.append(", ");
        BlocklyType itemType = listRepeat.getElement().getVarType();
        if ( itemType.equals(BlocklyType.NUMBER) || itemType.equals(BlocklyType.STRING) ) {
            this.sb.append("(" + getLanguageVarTypeFromBlocklyType(itemType) + ") ");
        }
        listRepeat.getElement().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst mathConst) {
        switch ( mathConst.mathConst ) {
            case PI:
                this.sb.append("M_PI");
                break;
            case E:
                this.sb.append("M_E");
                break;
            case GOLDEN_RATIO:
                this.sb.append("M_GOLDEN_RATIO");
                break;
            case SQRT2:
                this.sb.append("M_SQRT2");
                break;
            case SQRT1_2:
                this.sb.append("M_SQRT1_2");
                break;
            // IEEE 754 floating point representation
            case INFINITY:
                this.sb.append("M_INFINITY");
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
        this.sb.append(methodName);

        indexOfFunct.param.get(0).accept(this);
        this.sb.append(", ");
        indexOfFunct.param.get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct getSubFunct) {
        if ( getSubFunct.functName == FunctionNames.GET_SUBLIST ) {
            this.sb.append("_getSubList(");
            getSubFunct.param.get(0).accept(this);
            this.sb.append(", ");
            switch ( (IndexLocation) getSubFunct.strParam.get(0) ) {
                case FIRST:
                    this.sb.append("0, ");
                    break;
                case FROM_END:
                    getSubFunct.param.get(0).accept(this);
                    this.sb.append(".size() - 1 - ");
                    getSubFunct.param.get(1).accept(this);
                    this.sb.append(", ");
                    break;
                case FROM_START:
                    getSubFunct.param.get(1).accept(this);
                    this.sb.append(", ");
                    break;
                default:
                    break;
            }
            switch ( (IndexLocation) getSubFunct.strParam.get(1) ) {
                case LAST:
                    getSubFunct.param.get(0).accept(this);
                    this.sb.append(".size() - 1");
                    break;
                case FROM_END:
                    getSubFunct.param.get(0).accept(this);
                    this.sb.append(".size() - 1 - ");
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
            this.sb.append(")");
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
        this.sb.append(operation);
        switch ( (IndexLocation) listGetIndex.location ) {
            case FIRST:
                listGetIndex.param.get(0).accept(this);
                this.sb.append(", 0)");
                break;
            case FROM_END:
                listGetIndex.param.get(0).accept(this);
                this.sb.append(", ");
                listGetIndex.param.get(0).accept(this);
                this.sb.append(".size() - 1 - ");
                listGetIndex.param.get(1).accept(this);
                this.sb.append(")");
                break;
            case FROM_START:
                listGetIndex.param.get(0).accept(this);
                this.sb.append(", ");
                listGetIndex.param.get(1).accept(this);
                this.sb.append(")");
                break;
            case LAST:
                listGetIndex.param.get(0).accept(this);
                this.sb.append(", ");
                listGetIndex.param.get(0).accept(this);
                this.sb.append(".size() - 1)");
                break;
            case RANDOM:
                listGetIndex.param.get(0).accept(this);
                this.sb.append(", 0 /* absolutely random number */)");
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
                    this.sb.append(".push_back(");
                    listSetIndex.param.get(1).accept(this);
                    this.sb.append(")");
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
        this.sb.append(operation);
        switch ( (IndexLocation) listSetIndex.location ) {
            case FIRST:
                listSetIndex.param.get(0).accept(this);
                this.sb.append(", 0, ");
                listSetIndex.param.get(1).accept(this);
                this.sb.append(")");
                break;
            case FROM_END:
                listSetIndex.param.get(0).accept(this);
                this.sb.append(", ");
                listSetIndex.param.get(0).accept(this);
                this.sb.append(".size() - 1 - ");
                listSetIndex.param.get(2).accept(this);
                this.sb.append(", ");
                listSetIndex.param.get(1).accept(this);
                this.sb.append(")");
                break;
            case FROM_START:
                listSetIndex.param.get(0).accept(this);
                this.sb.append(", ");
                listSetIndex.param.get(2).accept(this);
                this.sb.append(", ");
                listSetIndex.param.get(1).accept(this);
                this.sb.append(")");
                break;
            case LAST:
                listSetIndex.param.get(0).accept(this);
                this.sb.append(", ");
                listSetIndex.param.get(0).accept(this);
                this.sb.append(".size() - 1, ");
                listSetIndex.param.get(1).accept(this);
                this.sb.append(")");
                break;
            case RANDOM:
                listSetIndex.param.get(0).accept(this);
                this.sb.append(", 0 /* absolutely random number */, ");
                listSetIndex.param.get(1).accept(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct lengthOfIsEmptyFunct) {
        if ( lengthOfIsEmptyFunct.functName == FunctionNames.LIST_IS_EMPTY ) {
            lengthOfIsEmptyFunct.param.get(0).accept(this);
            this.sb.append(".empty()");
        } else {
            this.sb.append("((int) ");
            lengthOfIsEmptyFunct.param.get(0).accept(this);
            this.sb.append(".size())");
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        switch ( mathOnListFunct.functName ) {
            case AVERAGE:
                this.sb.append("_getListAverage(");
                break;
            case MAX:
                this.sb.append("_getListMax(");
                break;
            case MEDIAN:
                this.sb.append("_getListMedian(");
                break;
            case MIN:
                this.sb.append("_getListMin(");
                break;
            case STD_DEV:
                this.sb.append("_getListStandardDeviation(");
                break;
            case SUM:
                this.sb.append("_getListSum(");
                break;
            case RANDOM:
                // TODO it has no implementation and should probably be removed from blockly as well
                this.sb.append("_getListElementByIndex(");
                mathOnListFunct.param.get(0).accept(this);
                this.sb.append(", 0)");
                return null;
            default:
                break;
        }
        mathOnListFunct.param.get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        boolean extraPar = false;
        switch ( mathSingleFunct.functName ) {
            case SQUARE:
                this.sb.append("pow(");
                mathSingleFunct.param.get(0).accept(this);
                this.sb.append(", 2)");
                return null;
            case ROOT:
                this.sb.append("sqrt(");
                break;
            case ABS:
                this.sb.append("abs(");
                break;
            case LN:
                this.sb.append("log(");
                break;
            case LOG10:
                this.sb.append("log10(");
                break;
            case EXP:
                this.sb.append("exp(");
                break;
            case POW10:
                this.sb.append("pow(10.0, ");
                break;
            case SIN:
                extraPar = true;
                this.sb.append("sin(M_PI / 180.0 * (");
                break;
            case COS:
                extraPar = true;
                this.sb.append("cos(M_PI / 180.0 * (");
                break;
            case TAN:
                extraPar = true;
                this.sb.append("tan(M_PI / 180.0 * (");
                break;
            case ASIN:
                this.sb.append("180.0 / M_PI * asin(");
                break;
            case ATAN:
                this.sb.append("180.0 / M_PI * atan(");
                break;
            case ACOS:
                this.sb.append("180.0 / M_PI * acos(");
                break;
            case ROUND:
                this.sb.append("round(");
                break;
            case ROUNDUP:
                this.sb.append("ceil(");
                break;
            case ROUNDDOWN:
                this.sb.append("floor(");
                break;
            default:
                break;
        }
        mathSingleFunct.param.get(0).accept(this);
        if ( extraPar ) {
            this.sb.append(")");
        }
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        Expr n = mathConstrainFunct.param.get(0);
        Expr min = mathConstrainFunct.param.get(1);
        Expr max = mathConstrainFunct.param.get(2);
        this.sb.append("std::min(std::max((double) ");
        n.accept(this);
        this.sb.append(", (double) ");
        min.accept(this);
        this.sb.append("), (double) ");
        max.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        switch ( mathNumPropFunct.functName ) {
            case EVEN:
                this.sb.append("(fmod(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(", 2) == 0");
                break;
            case ODD:
                this.sb.append("(fmod(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(", 2) != 0");
                break;
            case PRIME:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.PRIME));
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                break;
            case WHOLE:
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" == floor(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" > 0");
                break;
            case NEGATIVE:
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" < 0");
                break;
            case DIVISIBLE_BY:
                this.sb.append("(fmod(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(",");
                mathNumPropFunct.param.get(1).accept(this);
                this.sb.append(") == 0");
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        this.sb.append("pow(");
        return super.visitMathPowerFunct(mathPowerFunct);
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        this.sb.append("((double) rand() / (RAND_MAX))");
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        // TODO check why this is not working for Arduinos!
        this.sb.append("(std::to_string(");
        mathCastStringFunct.param.get(0).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        this.sb.append("(char)(int)(");
        mathCastCharFunct.param.get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        this.sb.append("std::stof(");
        textStringCastNumberFunct.param.get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        this.sb.append("(int)(");
        this.sb.append("(");
        textCharCastNumberFunct.param.get(0).accept(this);
        this.sb.append(")[");
        textCharCastNumberFunct.param.get(1).accept(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid methodVoid) {
        nlIndent();
        this.sb.append("void ");
        this.sb.append(methodVoid.getCodeSafeMethodName()).append("(");
        methodVoid.getParameters().accept(this);
        this.sb.append(") {");
        incrIndentation();
        methodVoid.body.accept(this);
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn methodReturn) {
        nlIndent();
        this.sb.append(getLanguageVarTypeFromBlocklyType(methodReturn.getReturnType()));
        this.sb.append(" ").append(methodReturn.getCodeSafeMethodName()).append("(");
        methodReturn.getParameters().accept(this);
        this.sb.append(") {");
        incrIndentation();
        methodReturn.body.accept(this);
        nlIndent();
        this.sb.append("return ");
        methodReturn.returnValue.accept(this);
        this.sb.append(";");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        this.sb.append("if (");
        methodIfReturn.oraCondition.accept(this);
        this.sb.append(") ");
        this.sb.append("return ");
        methodIfReturn.oraReturnValue.accept(this);
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt methodStmt) {
        super.visitMethodStmt(methodStmt);
        if ( methodStmt.getProperty().getBlockType().equals("robProcedures_ifreturn") ) {
            this.sb.append(";");
        }
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall methodCall) {
        super.visitMethodCall(methodCall);
        if ( methodCall.getReturnType() == BlocklyType.VOID ) {
            this.sb.append(";");
        }
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        return null;
    }

    @Override
    public Void visitStringConst(StringConst stringConst) {
        this.sb.append("\"").append(StringEscapeUtils.escapeJava(stringConst.value.replaceAll("[<>\\$]", ""))).append("\"");
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt assertStmt) {
        // please overwrite this in the robot-specific class and throw an exeption if "assertNepo" could not be provided
        this.sb.append("assertNepo((");
        assertStmt.asserts.accept(this);
        this.sb.append("), \"").append(assertStmt.msg).append("\", ");
        ((Binary) assertStmt.asserts).left.accept(this);
        this.sb.append(", \"").append(((Binary) assertStmt.asserts).op.toString()).append("\", ");
        ((Binary) assertStmt.asserts).getRight().accept(this);
        this.sb.append(");");
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
        this.sb.append("Serial.println(");
        serialWriteAction.value.accept(this);
        this.sb.append(");");
        return null;
    }

    protected void addContinueLabelToLoop() {
        Integer lastLoop = this.currentLoop.getLast();
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(lastLoop) ) {
            nlIndent();
            this.sb.append("continue_loop" + this.currentLoop.getLast() + ":;");
        }
    }

    protected void addBreakLabelToLoop(boolean isWaitStmt) {
        if ( !isWaitStmt ) {
            if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) ) {
                nlIndent();
                this.sb.append("break_loop" + this.currentLoop.getLast() + ":;");
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
        this.sb.append("(" + whitespace() + "(" + whitespace());
        ternaryExpr.condition.accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + "?" + whitespace() + "(" + whitespace());
        ternaryExpr.thenPart.accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + ":" + whitespace() + "(" + whitespace());
        ternaryExpr.elsePart.accept(this);
        this.sb.append(")" + whitespace() + ")");
    }

    @Override
    protected void generateCodeFromIfElse(IfStmt ifStmt) {
        int exprSize = ifStmt.expr.size();
        String conditionStmt = "if";
        for ( int i = 0; i < exprSize; i++ ) {
            generateCodeFromStmtCondition(conditionStmt, ifStmt.expr.get(i));
            conditionStmt = "else" + whitespace() + "if";
            incrIndentation();
            ifStmt.thenList.get(i).accept(this);
            decrIndentation();
            if ( i + 1 < exprSize ) {
                nlIndent();
                this.sb.append("}").append(whitespace());
            }
        }

    }

    @Override
    protected void generateCodeFromElse(IfStmt ifStmt) {
        if ( !ifStmt.elseList.get().isEmpty() ) {
            nlIndent();
            this.sb.append("}").append(whitespace()).append("else").append(whitespace() + "{");
            incrIndentation();
            ifStmt.elseList.accept(this);
            decrIndentation();
        }
        nlIndent();
        this.sb.append("}");
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
                    this.sb.append(it.next());
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
                    this.sb.append(it.next());
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
            this.sb.append(getLanguageVarTypeFromBlocklyType(phrase.getReturnType()) + " ");
            this.sb.append(phrase.getCodeSafeMethodName() + "(");
            phrase.getParameters().accept(this);
            this.sb.append(");");
        }
    }

    protected void generateCodeFromStmtCondition(String stmtType, Expr expr) {
        this.sb.append(stmtType + whitespace() + "(" + whitespace());
        expr.accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + "{");
    }

    protected void generateCodeFromStmtConditionFor(String stmtType, Expr expr) {
        this.sb.append(stmtType + whitespace() + "(" + "int" + whitespace());
        final ExprList expressions = (ExprList) expr;
        expressions.get().get(0).accept(this);
        this.sb.append(whitespace() + "=" + whitespace());
        expressions.get().get(1).accept(this);
        this.sb.append(";" + whitespace());
        expressions.get().get(0).accept(this);
        this.sb.append(whitespace());
        this.sb.append("<" + whitespace());
        expressions.get().get(2).accept(this);
        this.sb.append(";" + whitespace());
        expressions.get().get(0).accept(this);
        this.sb.append(whitespace());
        this.sb.append("+=" + whitespace());
        expressions.get().get(3).accept(this);
        this.sb.append(")" + whitespace() + "{");
    }

    protected void appendBreakStmt() {
        nlIndent();
        this.sb.append("break;");
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
                        entry(Binary.Op.MATH_CHANGE, "+="),
                        entry(Binary.Op.TEXT_APPEND, "+="),
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
