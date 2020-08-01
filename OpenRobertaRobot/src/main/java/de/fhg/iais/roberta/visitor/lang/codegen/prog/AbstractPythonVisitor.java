package de.fhg.iais.roberta.visitor.lang.codegen.prog;

import static de.fhg.iais.roberta.mode.general.ListElementOperations.GET;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.GET_REMOVE;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.INSERT;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.REMOVE;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.SET;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.antlr.v4.runtime.misc.OrderedHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
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
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon.Flow;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public abstract class AbstractPythonVisitor extends AbstractLanguageVisitor {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPythonVisitor.class);

    protected Set<String> usedGlobalVarInFunctions = new OrderedHashSet<>();

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    protected AbstractPythonVisitor(List<List<Phrase<Void>>> programPhrases, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        // TODO Do we have always to cast to float
        if ( isInteger(numConst.getValue()) ) {
            super.visitNumConst(numConst);
        } else {
            this.sb.append("float(");
            super.visitNumConst(numConst);
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        this.sb.append(boolConst.getValue() ? "True" : "False");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        this.sb.append("None");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        switch ( mathConst.getMathConst() ) {
            case PI:
                this.sb.append("math.pi");
                break;
            case E:
                this.sb.append("math.e");
                break;
            case GOLDEN_RATIO:
                this.sb.append("(1 + 5 ** 0.5) / 2");
                break;
            case SQRT2:
                this.sb.append("math.sqrt(2)");
                break;
            case SQRT1_2:
                this.sb.append("math.sqrt(0.5)");
                break;
            case INFINITY:
                this.sb.append("float('inf')");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        this.usedGlobalVarInFunctions.add(var.getCodeSafeName());
        this.sb.append(var.getCodeSafeName());
        this.sb.append(" = ");
        if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
            if ( var.getValue().getKind().hasName("EXPR_LIST") ) {
                ExprList<Void> list = (ExprList<Void>) var.getValue();
                if ( list.get().size() == 2 ) {
                    list.get().get(1).accept(this);
                } else {
                    list.get().get(0).accept(this);
                }
            } else {
                var.getValue().accept(this);
            }
        } else {
            this.sb.append("None");
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        try {
            VarDeclaration<Void> variablePart = (VarDeclaration<Void>) binary.getLeft();
            this.sb.append(variablePart.getCodeSafeName());
        } catch ( ClassCastException e ) {
            generateSubExpr(this.sb, false, binary.getLeft(), binary);
        }
        //if ( variablePart.getValue().getClass().equals(EmptyExpr.class) ) {
        Binary.Op op = binary.getOp();
        String sym = getBinaryOperatorSymbol(op);
        this.sb.append(' ').append(sym).append(' ');
        generateCodeRightExpression(binary, op);
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                this.sb.append("\"\"");
                break;
            case BOOLEAN:
                this.sb.append("True");
                break;
            case NUMBER_INT:
                this.sb.append("0");
                break;
            case NUMBER:
                this.sb.append("0.0");
                break;
            case CAPTURED_TYPE:
                this.sb.append("None");
                break;
            case ARRAY:
                this.sb.append("None");
                break;
            case NULL:
                break;
            default:
                this.sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        boolean isWaitStmt = repeatStmt.getMode() == RepeatStmt.Mode.WAIT;
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                appendTry();
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtConditionFor("for", repeatStmt.getExpr());
                appendTry();
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                appendTry();
                break;
            default:
                throw new DbcException("Invalid Repeat Statement!");
        }
        incrIndentation();
        appendPassIfEmptyBody(repeatStmt);
        repeatStmt.getList().accept(this);
        if ( !isWaitStmt ) {
            appendExceptionHandling();
        } else {
            appendBreakStmt(repeatStmt);
        }
        decrIndentation();
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currenLoop.getLast()) != null ) {
            if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currenLoop.getLast()) ) {
                this.sb.append("raise " + (stmtFlowCon.getFlow() == Flow.BREAK ? "BreakOutOfALoop" : "ContinueLoop"));
                return null;
            }
        }
        this.sb.append(stmtFlowCon.getFlow().toString().toLowerCase());
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        this.sb.append("[]");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.sb.append("math.pow(");
        super.visitMathPowerFunct(mathPowerFunct);
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
            case SQUARE:
                this.sb.append("math.pow(");
                mathSingleFunct.getParam().get(0).accept(this);
                this.sb.append(", 2)");
                return null;
            case ROOT:
                this.sb.append("math.sqrt(");
                break;
            case ABS:
                this.sb.append("math.fabs(");
                break;
            case LN:
                this.sb.append("math.log(");
                break;
            case LOG10:
                this.sb.append("math.log10(");
                break;
            case EXP:
                this.sb.append("math.exp(");
                break;
            case POW10:
                this.sb.append("math.pow(10, ");
                break;
            case SIN:
                this.sb.append("math.sin(");
                break;
            case COS:
                this.sb.append("math.cos(");
                break;
            case TAN:
                this.sb.append("math.tan(");
                break;
            case ASIN:
                this.sb.append("math.asin(");
                break;
            case ATAN:
                this.sb.append("math.atan(");
                break;
            case ACOS:
                this.sb.append("math.acos(");
                break;
            case ROUND:
                this.sb.append("round(");
                break;
            case ROUNDUP:
                this.sb.append("math.ceil(");
                break;
            case ROUNDDOWN:
                this.sb.append("math.floor(");
                break;
            default:
                break;
        }
        mathSingleFunct.getParam().get(0).accept(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("min(max(");
        mathConstrainFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).accept(this);
        this.sb.append("), ");
        mathConstrainFunct.getParam().get(2).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" % 2) == 0");
                break;
            case ODD:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" % 2) == 1");
                break;
            case PRIME:
                String methodName = this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.PRIME);
                this.sb.append(methodName).append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            case WHOLE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" % 1) == 0");
                break;
            case POSITIVE:
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" > 0");
                break;
            case NEGATIVE:
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" < 0");
                break;
            case DIVISIBLE_BY:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" % ");
                mathNumPropFunct.getParam().get(1).accept(this);
                this.sb.append(") == 0");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("random.random()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("random.randint(");
        mathRandomIntFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                this.sb.append("sum(");
                mathOnListFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            case MIN:
                this.sb.append("min(");
                mathOnListFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            case MAX:
                this.sb.append("max(");
                mathOnListFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            case AVERAGE:
                this.sb.append("float(sum(");
                mathOnListFunct.getParam().get(0).accept(this);
                this.sb.append("))/len(");
                mathOnListFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            case MEDIAN:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.MEDIAN)).append("(");
                mathOnListFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            case STD_DEV:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.STD_DEV)).append("(");
                mathOnListFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            case RANDOM:
                mathOnListFunct.getParam().get(0).accept(this);
                this.sb.append("[0]");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        this.sb.append("str(");
        mathCastStringFunct.getParam().get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        this.sb.append("chr((int)(");
        mathCastCharFunct.getParam().get(0).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        this.sb.append("float(");
        textStringCastNumberFunct.getParam().get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        this.sb.append("ord(");
        textCharCastNumberFunct.getParam().get(0).accept(this);
        this.sb.append("[");
        textCharCastNumberFunct.getParam().get(1).accept(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        this.sb.append("\"\".join(str(arg) for arg in [");
        textJoinFunct.getParam().accept(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        this.sb.append("[");
        listCreate.getValue().accept(this);
        this.sb.append("]");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        listGetIndex.getParam().get(0).accept(this);

        if ( listGetIndex.getElementOperation() == GET ) {
            this.sb.append("[");
        } else if ( listGetIndex.getElementOperation() == REMOVE || listGetIndex.getElementOperation() == GET_REMOVE ) {
            this.sb.append(".pop(");
        }

        switch ( (IndexLocation) listGetIndex.getLocation() ) {
            case RANDOM: // backwards compatibility
                // TODO?
            case FIRST:
                this.sb.append("0");
                break;
            case FROM_END:
                this.sb.append("-1 -"); // TODO should be correct but how is it handled on other robots?
                listGetIndex.getParam().get(1).accept(this);
                break;
            case FROM_START:
                listGetIndex.getParam().get(1).accept(this);
                break;
            case LAST:
                this.sb.append("-1");
                break;
            default:
                break;
        }
        if ( listGetIndex.getElementOperation() == GET ) {
            this.sb.append("]");
        } else if ( listGetIndex.getElementOperation() == REMOVE || listGetIndex.getElementOperation() == GET_REMOVE ) {
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        listSetIndex.getParam().get(0).accept(this);
        if ( listSetIndex.getElementOperation() == SET ) {
            this.sb.append("[");
        } else if ( listSetIndex.getElementOperation() == INSERT ) {
            this.sb.append(".insert(");
        }
        switch ( (IndexLocation) listSetIndex.getLocation() ) {
            case RANDOM: // backwards compatibility
                // TODO?
            case FIRST:
                this.sb.append("0");
                break;
            case FROM_END:
                this.sb.append("-1 -");
                listSetIndex.getParam().get(2).accept(this);
                break;
            case FROM_START:
                listSetIndex.getParam().get(2).accept(this);
                break;
            case LAST:
                this.sb.append("-1");
                break;
            default:
                break;
        }
        if ( listSetIndex.getElementOperation() == SET ) {
            this.sb.append("] = ");
            listSetIndex.getParam().get(1).accept(this);
        } else if ( listSetIndex.getElementOperation() == INSERT ) {
            this.sb.append(", ");
            listSetIndex.getParam().get(1).accept(this);
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.sb.append("[");
        listRepeat.getParam().get(0).accept(this);
        this.sb.append("] * ");
        listRepeat.getParam().get(1).accept(this);
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        switch ( (IndexLocation) indexOfFunct.getLocation() ) {
            case FIRST:
                indexOfFunct.getParam().get(0).accept(this);
                this.sb.append(".index(");
                indexOfFunct.getParam().get(1).accept(this);
                this.sb.append(")");
                break;
            case LAST:
                this.sb.append("(len(");
                indexOfFunct.getParam().get(0).accept(this);
                this.sb.append(") - 1) - ");
                indexOfFunct.getParam().get(0).accept(this);
                this.sb.append("[::-1].index(");
                indexOfFunct.getParam().get(1).accept(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        if ( getSubFunct.getFunctName() == FunctionNames.GET_SUBLIST ) {
            getSubFunct.getParam().get(0).accept(this);
            this.sb.append("[");
            switch ( (IndexLocation) getSubFunct.getStrParam().get(0) ) {
                case FIRST:
                    this.sb.append("0:");
                    break;
                case FROM_END:
                    this.sb.append("-1 -");
                    getSubFunct.getParam().get(1).accept(this);
                    this.sb.append(":");
                    break;
                case FROM_START:
                    getSubFunct.getParam().get(1).accept(this);
                    this.sb.append(":");
                    break;
                default:
                    break;
            }
            switch ( (IndexLocation) getSubFunct.getStrParam().get(1) ) {
                case LAST:
                    // append nothing
                    break;
                case FROM_END:
                    this.sb.append("-1 -");
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
            this.sb.append("]");
        }
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        this.sb.append("print(");
        textPrintFunct.getParam().get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        switch ( lengthOfIsEmptyFunct.getFunctName() ) {
            case LIST_LENGTH:
                this.sb.append("len( ");
                lengthOfIsEmptyFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;

            case LIST_IS_EMPTY:
                this.sb.append("not ");
                lengthOfIsEmptyFunct.getParam().get(0).accept(this);
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        nlIndent();
        this.sb.append("def ").append(methodVoid.getMethodName()).append('(');
        List<String> paramList = new ArrayList<>();
        for ( Expr<Void> l : methodVoid.getParameters().get() ) {
            paramList.add(((VarDeclaration<Void>) l).getCodeSafeName());
        }
        this.sb.append(String.join(", ", paramList));
        this.sb.append("):");
        incrIndentation();
        boolean isMethodBodyEmpty = methodVoid.getBody().get().isEmpty();
        if ( isMethodBodyEmpty ) {
            nlIndent();
            this.sb.append("pass");
        } else {
            if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
                nlIndent();
                this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
            }
            methodVoid.getBody().accept(this);
        }
        decrIndentation();
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        nlIndent();
        this.sb.append("def ").append(methodReturn.getMethodName()).append('(');
        List<String> paramList = new ArrayList<>();
        for ( Expr<Void> l : methodReturn.getParameters().get() ) {
            paramList.add(((VarDeclaration<Void>) l).getCodeSafeName());
        }
        this.sb.append(String.join(", ", paramList));
        this.sb.append("):");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
        }
        methodReturn.getBody().accept(this);
        nlIndent();
        this.sb.append("return ");
        methodReturn.getReturnValue().accept(this);
        decrIndentation();
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        this.sb.append("if ");
        methodIfReturn.getCondition().accept(this);
        if ( !methodIfReturn.getReturnValue().getKind().hasName("EMPTY_EXPR") ) {
            this.sb.append(": return ");
            methodIfReturn.getReturnValue().accept(this);
        } else {
            this.sb.append(": return None");
        }
        return null;
    }

    @Override
    public String getEnumCode(IMode value) {
        return "'" + value.toString().toLowerCase() + "'";
    }

    @Override
    public String getEnumCode(String value) {
        return "'" + value.toLowerCase() + "'";
    }

    @Override
    protected void generateCodeFromTernary(IfStmt<Void> ifStmt) {
        ((ExprStmt<Void>) ifStmt.getThenList().get(0).get().get(0)).getExpr().accept(this);
        this.sb.append(whitespace() + "if" + whitespace() + "(" + whitespace());
        ifStmt.getExpr().get(0).accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + "else" + whitespace());
        ((ExprStmt<Void>) ifStmt.getElseList().get().get(0)).getExpr().accept(this);
    }

    @Override
    protected void generateCodeFromIfElse(IfStmt<Void> ifStmt) {
        int stmtSize = ifStmt.getExpr().size();
        for ( int i = 0; i < stmtSize; i++ ) {
            if ( i == 0 ) {
                generateCodeFromStmtCondition("if", ifStmt.getExpr().get(i));
            } else {
                nlIndent();
                generateCodeFromStmtCondition("elif", ifStmt.getExpr().get(i));
            }
            incrIndentation();
            StmtList<Void> then = ifStmt.getThenList().get(i);
            if ( then.get().isEmpty() ) {
                nlIndent();
                this.sb.append("pass");
            } else {
                then.accept(this);
            }
            decrIndentation();
        }
    }

    @Override
    protected void generateCodeFromElse(IfStmt<Void> ifStmt) {
        if ( !ifStmt.getElseList().get().isEmpty() ) {
            nlIndent();
            this.sb.append("else:");
            incrIndentation();
            ifStmt.getElseList().accept(this);
            decrIndentation();
        }
    }

    @Override
    protected String getLanguageVarTypeFromBlocklyType(BlocklyType type) {
        return "";
    }

    protected void addPassIfProgramIsEmpty() {
        if ( this.getBean(UsedHardwareBean.class).isProgramEmpty() ) {
            nlIndent();
            this.sb.append("pass");
        }
    }

    private void generateCodeRightExpression(Binary<Void> binary, Binary.Op op) {
        switch ( op ) {
            case TEXT_APPEND:
                this.sb.append("str(");
                generateSubExpr(this.sb, false, binary.getRight(), binary);
                this.sb.append(")");
                break;
            case DIVIDE:
                this.sb.append("float(");
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
                this.sb.append(")");
                break;
            default:
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
                break;
        }
    }

    protected void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType).append(whitespace());
        expr.accept(this);
        this.sb.append(":");
    }

    protected void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType).append(whitespace());
        ExprList<Void> expressions = (ExprList<Void>) expr;
        expressions.get().get(0).accept(this);
        this.sb.append(whitespace() + "in range(int(");
        expressions.get().get(1).accept(this);
        this.sb.append(")," + whitespace() + "int(");
        expressions.get().get(2).accept(this);
        this.sb.append(")," + whitespace() + "int(");
        expressions.get().get(3).accept(this);
        this.sb.append(")):");
    }

    protected void appendBreakStmt(RepeatStmt<Void> repeatStmt) {
        nlIndent();
        this.sb.append("break");
    }

    protected void appendTry() {
        increaseLoopCounter();

        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currenLoop.getLast()) ) {
            incrIndentation();
            nlIndent();
            this.sb.append("try:");
        }
    }

    protected void appendExceptionHandling() {
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currenLoop.getLast()) ) {
            decrIndentation();
            nlIndent();
            this.sb.append("except BreakOutOfALoop:");
            incrIndentation();
            nlIndent();
            this.sb.append("break");
            decrIndentation();
            nlIndent();
            this.sb.append("except ContinueLoop:");
            incrIndentation();
            nlIndent();
            this.sb.append("continue");
            decrIndentation();
        }
        this.currenLoop.removeLast();
    }

    protected void appendPassIfEmptyBody(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getList().get().isEmpty() ) {
            if ( repeatStmt.getMode() != RepeatStmt.Mode.WAIT ) {
                nlIndent();
                this.sb.append("pass");
            }
        }
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
                        entry(Binary.Op.AND, "and"),
                        entry(Binary.Op.OR, "or"),
                        entry(Binary.Op.MATH_CHANGE, "+="),
                        entry(Binary.Op.TEXT_APPEND, "+="),
                        entry(Binary.Op.IN, "in"),
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
                        entry(Unary.Op.NOT, "not"),
                        entry(Unary.Op.POSTFIX_INCREMENTS, "++"),
                        entry(Unary.Op.PREFIX_INCREMENTS, "++")

                    )
                    .collect(entriesToMap()));
    }

    @Override
    public Void visitStmtTextComment(StmtTextComment<Void> stmtTextComment) {
        this.sb.append("# " + stmtTextComment.getTextComment().replace("\n", " "));
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt<Void> assertStmt) {
        this.sb.append("if not ");
        assertStmt.getAssert().accept(this);
        this.sb.append(":");
        incrIndentation();
        nlIndent();
        this.sb.append("print(\"Assertion failed: \", \"").append(assertStmt.getMsg()).append("\", ");
        ((Binary<Void>) assertStmt.getAssert()).getLeft().accept(this);
        this.sb.append(", \"").append(((Binary<Void>) assertStmt.getAssert()).getOp().toString()).append("\", ");
        ((Binary<Void>) assertStmt.getAssert()).getRight().accept(this);
        this.sb.append(")");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction<Void> debugAction) {
        this.sb.append("print(");
        debugAction.getValue().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
            String helperMethodImpls =
                this
                    .getBean(CodeGeneratorSetupBean.class)
                    .getHelperMethodGenerator()
                    .getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.sb.append(helperMethodImpls);
        }

        nlIndent();
        this.sb.append("if __name__ == \"__main__\":");
        incrIndentation();
        nlIndent();
        this.sb.append("main()");
        decrIndentation();
    }
}
