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
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
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
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.VisitorException;
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
    protected AbstractCppVisitor(List<List<Phrase<Void>>> programPhrases, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        this.sb.append("RGB(");
        rgbColor.getR().accept(this);
        this.sb.append(", ");
        rgbColor.getG().accept(this);
        this.sb.append(", ");
        rgbColor.getB().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        this.sb.append("NULL");
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        this.sb.append(connectConst.getValue());
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
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
    public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        super.visitAssignStmt(assignStmt);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt<Void> exprStmt) {
        super.visitExprStmt(exprStmt);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt<Void> functionStmt) {
        super.visitFunctionStmt(functionStmt);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currenLoop.getLast()) != null ) {
            if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currenLoop.getLast()) ) {
                this.sb.append("goto " + stmtFlowCon.getFlow().toString().toLowerCase() + "_loop" + this.currenLoop.getLast() + ";");
                return null;
            }
        }
        this.sb.append(stmtFlowCon.getFlow().toString().toLowerCase() + ";");
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        if ( listCreate.getProperty().isErrorAttribute() != null && listCreate.getProperty().isErrorAttribute().booleanValue() ) {
            throw new VisitorException("Got error on list create block");
        }
        this.sb.append("{");
        listCreate.getValue().accept(this);
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
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
    public Void visitMathConst(MathConst<Void> mathConst) {
        switch ( mathConst.getMathConst() ) {
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
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        String methodName = "_getFirstOccuranceOfElement(";
        if ( indexOfFunct.getLocation() != IndexLocation.FIRST ) {
            methodName = "_getLastOccuranceOfElement(";
        }
        this.sb.append(methodName);

        indexOfFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        indexOfFunct.getParam().get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        if ( getSubFunct.getFunctName() == FunctionNames.GET_SUBLIST ) {
            this.sb.append("_getSubList(");
            getSubFunct.getParam().get(0).accept(this);
            this.sb.append(", ");
            switch ( (IndexLocation) getSubFunct.getStrParam().get(0) ) {
                case FIRST:
                    this.sb.append("0, ");
                    break;
                case FROM_END:
                    getSubFunct.getParam().get(0).accept(this);
                    this.sb.append(".size() - 1 - ");
                    getSubFunct.getParam().get(1).accept(this);
                    this.sb.append(", ");
                    break;
                case FROM_START:
                    getSubFunct.getParam().get(1).accept(this);
                    this.sb.append(", ");
                    break;
                default:
                    break;
            }
            switch ( (IndexLocation) getSubFunct.getStrParam().get(1) ) {
                case LAST:
                    getSubFunct.getParam().get(0).accept(this);
                    this.sb.append(".size() - 1");
                    break;
                case FROM_END:
                    getSubFunct.getParam().get(0).accept(this);
                    this.sb.append(".size() - 1 - ");
                    try {
                        getSubFunct.getParam().get(2).accept(this);
                    } catch ( IndexOutOfBoundsException e ) { // means that our start index does not have a variable
                        getSubFunct.getParam().get(1).accept(this);
                    }
                    break;
                case FROM_START:
                    try {
                        getSubFunct.getParam().get(2).accept(this);
                    } catch ( IndexOutOfBoundsException e ) { // means that our start index does not have a variable
                        getSubFunct.getParam().get(1).accept(this);
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
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
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
        switch ( (IndexLocation) listGetIndex.getLocation() ) {
            case FIRST:
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append(", 0)");
                break;
            case FROM_END:
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append(", ");
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append(".size() - 1 - ");
                listGetIndex.getParam().get(1).accept(this);
                this.sb.append(")");
                break;
            case FROM_START:
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append(", ");
                listGetIndex.getParam().get(1).accept(this);
                this.sb.append(")");
                break;
            case LAST:
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append(", ");
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append(".size() - 1)");
                break;
            case RANDOM:
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append(", 0 /* absolutely random number */)");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        String operation = "";
        switch ( (ListElementOperations) listSetIndex.getElementOperation() ) {
            case GET:
                break;
            case GET_REMOVE:
                break;
            case INSERT:
                if ( ((IndexLocation) listSetIndex.getLocation()).equals(IndexLocation.LAST) ) {
                    listSetIndex.getParam().get(0).accept(this);
                    this.sb.append(".push_back(");
                    listSetIndex.getParam().get(1).accept(this);
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
        switch ( (IndexLocation) listSetIndex.getLocation() ) {
            case FIRST:
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append(", 0, ");
                listSetIndex.getParam().get(1).accept(this);
                this.sb.append(")");
                break;
            case FROM_END:
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append(", ");
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append(".size() - 1 - ");
                listSetIndex.getParam().get(2).accept(this);
                this.sb.append(", ");
                listSetIndex.getParam().get(1).accept(this);
                this.sb.append(")");
                break;
            case FROM_START:
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append(", ");
                listSetIndex.getParam().get(2).accept(this);
                this.sb.append(", ");
                listSetIndex.getParam().get(1).accept(this);
                this.sb.append(")");
                break;
            case LAST:
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append(", ");
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append(".size() - 1, ");
                listSetIndex.getParam().get(1).accept(this);
                this.sb.append(")");
                break;
            case RANDOM:
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append(", 0 /* absolutely random number */, ");
                listSetIndex.getParam().get(1).accept(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            lengthOfIsEmptyFunct.getParam().get(0).accept(this);
            this.sb.append(".empty()");
        } else {
            this.sb.append("((int) ");
            lengthOfIsEmptyFunct.getParam().get(0).accept(this);
            this.sb.append(".size())");
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch ( mathOnListFunct.getFunctName() ) {
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
                mathOnListFunct.getParam().get(0).accept(this);
                this.sb.append(", 0)");
                return null;
            default:
                break;
        }
        mathOnListFunct.getParam().get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        boolean extraPar = false;
        switch ( mathSingleFunct.getFunctName() ) {
            case SQUARE:
                this.sb.append("pow(");
                mathSingleFunct.getParam().get(0).accept(this);
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
        mathSingleFunct.getParam().get(0).accept(this);
        if ( extraPar ) {
            this.sb.append(")");
        }
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        Expr<Void> n = mathConstrainFunct.getParam().get(0);
        Expr<Void> min = mathConstrainFunct.getParam().get(1);
        Expr<Void> max = mathConstrainFunct.getParam().get(2);
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
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(", 2) == 0");
                break;
            case ODD:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(", 2) != 0");
                break;
            case PRIME:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.PRIME));
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                break;
            case WHOLE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" == floor(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" > 0");
                break;
            case NEGATIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" < 0");
                break;
            case DIVISIBLE_BY:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(",");
                mathNumPropFunct.getParam().get(1).accept(this);
                this.sb.append(") == 0");
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.sb.append("pow(");
        return super.visitMathPowerFunct(mathPowerFunct);
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("((double) rand() / (RAND_MAX))");
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        // TODO check why this is not working for Arduinos!
        this.sb.append("(std::to_string(");
        mathCastStringFunct.getParam().get(0).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        this.sb.append("(char)(int)(");
        mathCastCharFunct.getParam().get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        this.sb.append("std::stof(");
        textStringCastNumberFunct.getParam().get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        this.sb.append("(int)(");
        this.sb.append("(");
        textCharCastNumberFunct.getParam().get(0).accept(this);
        this.sb.append(")[");
        textCharCastNumberFunct.getParam().get(1).accept(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        nlIndent();
        this.sb.append("void ");
        this.sb.append(methodVoid.getMethodName()).append("(");
        methodVoid.getParameters().accept(this);
        this.sb.append(") {");
        incrIndentation();
        methodVoid.getBody().accept(this);
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        nlIndent();
        this.sb.append(getLanguageVarTypeFromBlocklyType(methodReturn.getReturnType()));
        this.sb.append(" ").append(methodReturn.getMethodName()).append("(");
        methodReturn.getParameters().accept(this);
        this.sb.append(") {");
        incrIndentation();
        methodReturn.getBody().accept(this);
        nlIndent();
        this.sb.append("return ");
        methodReturn.getReturnValue().accept(this);
        this.sb.append(";");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        this.sb.append("if (");
        methodIfReturn.getCondition().accept(this);
        this.sb.append(") ");
        this.sb.append("return ");
        methodIfReturn.getReturnValue().accept(this);
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        super.visitMethodStmt(methodStmt);
        if ( methodStmt.getProperty().getBlockType().equals("robProcedures_ifreturn") ) {
            this.sb.append(";");
        }
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        super.visitMethodCall(methodCall);
        if ( methodCall.getReturnType() == BlocklyType.VOID ) {
            this.sb.append(";");
        }
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        this.sb.append("\"").append(StringEscapeUtils.escapeJava(stringConst.getValue().replaceAll("[<>\\$]", ""))).append("\"");
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt<Void> assertStmt) {
        // please overwrite this in the robot-specific class and throw an exeption if "assertNepo" could not be provided
        this.sb.append("assertNepo((");
        assertStmt.getAssert().accept(this);
        this.sb.append("), \"").append(assertStmt.getMsg()).append("\", ");
        ((Binary<Void>) assertStmt.getAssert()).getLeft().accept(this);
        this.sb.append(", \"").append(((Binary<Void>) assertStmt.getAssert()).getOp().toString()).append("\", ");
        ((Binary<Void>) assertStmt.getAssert()).getRight().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction<Void> debugAction) {
        throw new UnsupportedOperationException("should be overriden in a robot-specific class");
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        throw new UnsupportedOperationException("should be overriden in a robot-specific class");
    }

    protected void addContinueLabelToLoop() {
        Integer lastLoop = this.currenLoop.getLast();
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(lastLoop) ) {
            nlIndent();
            this.sb.append("continue_loop" + this.currenLoop.getLast() + ":");
        }
    }

    protected void addBreakLabelToLoop(boolean isWaitStmt) {
        if ( !isWaitStmt ) {
            if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currenLoop.getLast()) ) {
                nlIndent();
                this.sb.append("break_loop" + this.currenLoop.getLast() + ":");
                nlIndent();
            }
            this.currenLoop.removeLast();
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
    protected void generateCodeFromTernary(IfStmt<Void> ifStmt) {
        this.sb.append("(" + whitespace() + "(" + whitespace());
        ifStmt.getExpr().get(0).accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + "?" + whitespace() + "(" + whitespace());
        ((ExprStmt<Void>) ifStmt.getThenList().get(0).get().get(0)).getExpr().accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + ":" + whitespace() + "(" + whitespace());
        ((ExprStmt<Void>) ifStmt.getElseList().get().get(0)).getExpr().accept(this);
        this.sb.append(")" + whitespace() + ")");
    }

    @Override
    protected void generateCodeFromIfElse(IfStmt<Void> ifStmt) {
        int exprSize = ifStmt.getExpr().size();
        String conditionStmt = "if";
        for ( int i = 0; i < exprSize; i++ ) {
            generateCodeFromStmtCondition(conditionStmt, ifStmt.getExpr().get(i));
            conditionStmt = "else" + whitespace() + "if";
            incrIndentation();
            ifStmt.getThenList().get(i).accept(this);
            decrIndentation();
            if ( i + 1 < exprSize ) {
                nlIndent();
                this.sb.append("}").append(whitespace());
            }
        }

    }

    @Override
    protected void generateCodeFromElse(IfStmt<Void> ifStmt) {
        if ( !ifStmt.getElseList().get().isEmpty() ) {
            nlIndent();
            this.sb.append("}").append(whitespace()).append("else").append(whitespace() + "{");
            incrIndentation();
            ifStmt.getElseList().accept(this);
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
        for ( Method<Void> phrase : this.getBean(UsedHardwareBean.class).getUserDefinedMethods() ) {
            nlIndent();
            this.sb.append(getLanguageVarTypeFromBlocklyType(phrase.getReturnType()) + " ");
            this.sb.append(phrase.getMethodName() + "(");
            phrase.getParameters().accept(this);
            this.sb.append(");");
        }
    }

    protected void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType + whitespace() + "(" + whitespace());
        expr.accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + "{");
    }

    protected void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType + whitespace() + "(" + "int" + whitespace());
        final ExprList<Void> expressions = (ExprList<Void>) expr;
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
