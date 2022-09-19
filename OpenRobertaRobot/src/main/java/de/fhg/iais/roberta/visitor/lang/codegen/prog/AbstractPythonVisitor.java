package de.fhg.iais.roberta.visitor.lang.codegen.prog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.antlr.v4.runtime.misc.OrderedHashSet;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.GET;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.GET_REMOVE;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.INSERT;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.REMOVE;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.SET;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
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
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon.Flow;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.FunctionNames;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public abstract class AbstractPythonVisitor extends AbstractLanguageVisitor {
    protected Set<String> usedGlobalVarInFunctions = new OrderedHashSet<>();

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    protected AbstractPythonVisitor(List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
    }

    @Override
    public Void visitNumConst(NumConst numConst) {
        // TODO Do we have always to cast to float
        if ( isInteger(numConst.value) ) {
            super.visitNumConst(numConst);
        } else {
            this.sb.append("float(");
            super.visitNumConst(numConst);
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst boolConst) {
        this.sb.append(boolConst.value ? "True" : "False");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst nullConst) {
        this.sb.append("None");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst mathConst) {
        switch ( mathConst.mathConst ) {
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
    public Void visitVarDeclaration(VarDeclaration var) {
        this.usedGlobalVarInFunctions.add(var.getCodeSafeName());
        this.sb.append(var.getCodeSafeName());
        this.sb.append(" = ");
        if ( !var.value.getKind().hasName("EMPTY_EXPR") ) {
            if ( var.value.getKind().hasName("EXPR_LIST") ) {
                ExprList list = (ExprList) var.value;
                if ( list.get().size() == 2 ) {
                    list.get().get(1).accept(this);
                } else {
                    list.get().get(0).accept(this);
                }
            } else {
                var.value.accept(this);
            }
        } else {
            this.sb.append("None");
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        try {
            VarDeclaration variablePart = (VarDeclaration) binary.left;
            this.sb.append(variablePart.getCodeSafeName());
        } catch ( ClassCastException e ) {
            generateSubExpr(this.sb, false, binary.left, binary);
        }
        //if ( variablePart.getValue().getClass().equals(EmptyExpr.class) ) {
        Binary.Op op = binary.op;
        String sym = getBinaryOperatorSymbol(op);
        this.sb.append(' ').append(sym).append(' ');
        generateCodeRightExpression(binary, op);
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr emptyExpr) {
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
    public Void visitRepeatStmt(RepeatStmt repeatStmt) {
        boolean isWaitStmt = repeatStmt.mode == RepeatStmt.Mode.WAIT;
        switch ( repeatStmt.mode ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                generateCodeFromStmtCondition("while", repeatStmt.expr);
                appendTry();
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtConditionFor("for", repeatStmt.expr);
                appendTry();
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.expr);
                break;
            case FOR_EACH:
                generateCodeFromStmtCondition("for", repeatStmt.expr);
                appendTry();
                break;
            default:
                throw new DbcException("Invalid Repeat Statement!");
        }
        incrIndentation();
        appendPassIfEmptyBody(repeatStmt);
        repeatStmt.list.accept(this);
        if ( !isWaitStmt ) {
            appendExceptionHandling();
        } else {
            appendBreakStmt(repeatStmt);
        }
        decrIndentation();
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon stmtFlowCon) {
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) != null ) {
            if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) ) {
                this.sb.append("raise " + (stmtFlowCon.flow == Flow.BREAK ? "BreakOutOfALoop" : "ContinueLoop"));
                return null;
            }
        }
        this.sb.append(stmtFlowCon.flow.toString().toLowerCase());
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList emptyList) {
        this.sb.append("[]");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        this.sb.append("math.pow(");
        super.visitMathPowerFunct(mathPowerFunct);
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        switch ( mathSingleFunct.functName ) {
            case SQUARE:
                this.sb.append("math.pow(");
                mathSingleFunct.param.get(0).accept(this);
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
        mathSingleFunct.param.get(0).accept(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        this.sb.append("min(max(");
        mathConstrainFunct.param.get(0).accept(this);
        this.sb.append(", ");
        mathConstrainFunct.param.get(1).accept(this);
        this.sb.append("), ");
        mathConstrainFunct.param.get(2).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        switch ( mathNumPropFunct.functName ) {
            case EVEN:
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" % 2) == 0");
                break;
            case ODD:
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" % 2) == 1");
                break;
            case PRIME:
                String methodName = this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.PRIME);
                this.sb.append(methodName).append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(")");
                break;
            case WHOLE:
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" % 1) == 0");
                break;
            case POSITIVE:
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" > 0");
                break;
            case NEGATIVE:
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" < 0");
                break;
            case DIVISIBLE_BY:
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" % ");
                mathNumPropFunct.param.get(1).accept(this);
                this.sb.append(") == 0");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        this.sb.append("random.random()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.sb.append("random.randint(");
        mathRandomIntFunct.param.get(0).accept(this);
        this.sb.append(", ");
        mathRandomIntFunct.param.get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        switch ( mathOnListFunct.functName ) {
            case SUM:
                this.sb.append("sum(");
                mathOnListFunct.param.get(0).accept(this);
                this.sb.append(")");
                break;
            case MIN:
                this.sb.append("min(");
                mathOnListFunct.param.get(0).accept(this);
                this.sb.append(")");
                break;
            case MAX:
                this.sb.append("max(");
                mathOnListFunct.param.get(0).accept(this);
                this.sb.append(")");
                break;
            case AVERAGE:
                this.sb.append("float(sum(");
                mathOnListFunct.param.get(0).accept(this);
                this.sb.append("))/len(");
                mathOnListFunct.param.get(0).accept(this);
                this.sb.append(")");
                break;
            case MEDIAN:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.MEDIAN)).append("(");
                mathOnListFunct.param.get(0).accept(this);
                this.sb.append(")");
                break;
            case STD_DEV:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.STD_DEV)).append("(");
                mathOnListFunct.param.get(0).accept(this);
                this.sb.append(")");
                break;
            case RANDOM:
                mathOnListFunct.param.get(0).accept(this);
                this.sb.append("[0]");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        this.sb.append("str(");
        mathCastStringFunct.param.get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        this.sb.append("chr((int)(");
        mathCastCharFunct.param.get(0).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        this.sb.append("float(");
        textStringCastNumberFunct.param.get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        this.sb.append("ord(");
        textCharCastNumberFunct.param.get(0).accept(this);
        this.sb.append("[");
        textCharCastNumberFunct.param.get(1).accept(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        this.sb.append("\"\".join(str(arg) for arg in [");
        textJoinFunct.param.accept(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate listCreate) {
        this.sb.append("[");
        listCreate.exprList.accept(this);
        this.sb.append("]");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex listGetIndex) {
        listGetIndex.param.get(0).accept(this);

        if ( listGetIndex.getElementOperation() == GET ) {
            this.sb.append("[");
        } else if ( listGetIndex.getElementOperation() == REMOVE || listGetIndex.getElementOperation() == GET_REMOVE ) {
            this.sb.append(".pop(");
        }

        switch ( (IndexLocation) listGetIndex.location ) {
            case RANDOM: // backwards compatibility
                // TODO?
            case FIRST:
                this.sb.append("0");
                break;
            case FROM_END:
                this.sb.append("-1 -"); // TODO should be correct but how is it handled on other robots?
                listGetIndex.param.get(1).accept(this);
                break;
            case FROM_START:
                listGetIndex.param.get(1).accept(this);
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
    public Void visitListSetIndex(ListSetIndex listSetIndex) {
        listSetIndex.param.get(0).accept(this);
        if ( listSetIndex.mode == SET ) {
            this.sb.append("[");
        } else if ( listSetIndex.mode == INSERT ) {
            this.sb.append(".insert(");
        }
        switch ( (IndexLocation) listSetIndex.location ) {
            case RANDOM: // backwards compatibility
                // TODO?
            case FIRST:
                this.sb.append("0");
                break;
            case FROM_END:
                this.sb.append("-1 -");
                listSetIndex.param.get(2).accept(this);
                break;
            case FROM_START:
                listSetIndex.param.get(2).accept(this);
                break;
            case LAST:
                this.sb.append("-1");
                break;
            default:
                break;
        }
        if ( listSetIndex.mode == SET ) {
            this.sb.append("] = ");
            listSetIndex.param.get(1).accept(this);
        } else if ( listSetIndex.mode == INSERT ) {
            this.sb.append(", ");
            listSetIndex.param.get(1).accept(this);
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat listRepeat) {
        this.sb.append("[");
        listRepeat.param.get(0).accept(this);
        this.sb.append("] * ");
        listRepeat.param.get(1).accept(this);
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        switch ( (IndexLocation) indexOfFunct.location ) {
            case FIRST:
                indexOfFunct.param.get(0).accept(this);
                this.sb.append(".index(");
                indexOfFunct.param.get(1).accept(this);
                this.sb.append(")");
                break;
            case LAST:
                this.sb.append("(len(");
                indexOfFunct.param.get(0).accept(this);
                this.sb.append(") - 1) - ");
                indexOfFunct.param.get(0).accept(this);
                this.sb.append("[::-1].index(");
                indexOfFunct.param.get(1).accept(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct getSubFunct) {
        if ( getSubFunct.functName == FunctionNames.GET_SUBLIST ) {
            getSubFunct.param.get(0).accept(this);
            this.sb.append("[");
            switch ( (IndexLocation) getSubFunct.strParam.get(0) ) {
                case FIRST:
                    this.sb.append("0:");
                    break;
                case FROM_END:
                    this.sb.append("-1 -");
                    getSubFunct.param.get(1).accept(this);
                    this.sb.append(":");
                    break;
                case FROM_START:
                    getSubFunct.param.get(1).accept(this);
                    this.sb.append(":");
                    break;
                default:
                    break;
            }
            switch ( (IndexLocation) getSubFunct.strParam.get(1) ) {
                case LAST:
                    // append nothing
                    break;
                case FROM_END:
                    this.sb.append("-1 -");
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
            this.sb.append("]");
        }
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        this.sb.append("print(");
        textPrintFunct.param.get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct lengthOfIsEmptyFunct) {
        switch ( lengthOfIsEmptyFunct.functName ) {
            case LIST_LENGTH:
                this.sb.append("len( ");
                lengthOfIsEmptyFunct.param.get(0).accept(this);
                this.sb.append(")");
                break;

            case LIST_IS_EMPTY:
                this.sb.append("not ");
                lengthOfIsEmptyFunct.param.get(0).accept(this);
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid methodVoid) {
        nlIndent();
        this.sb.append("def ").append(methodVoid.getCodeSafeMethodName()).append('(');
        List<String> paramList = new ArrayList<>();
        for ( Expr l : methodVoid.getParameters().get() ) {
            paramList.add(((VarDeclaration) l).getCodeSafeName());
        }
        this.sb.append(String.join(", ", paramList));
        this.sb.append("):");
        incrIndentation();
        boolean isMethodBodyEmpty = methodVoid.body.get().isEmpty();
        if ( isMethodBodyEmpty ) {
            nlIndent();
            this.sb.append("pass");
        } else {
            if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
                nlIndent();
                this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
            }
            methodVoid.body.accept(this);
        }
        decrIndentation();
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn methodReturn) {
        nlIndent();
        this.sb.append("def ").append(methodReturn.getCodeSafeMethodName()).append('(');
        List<String> paramList = new ArrayList<>();
        for ( Expr l : methodReturn.getParameters().get() ) {
            paramList.add(((VarDeclaration) l).getCodeSafeName());
        }
        this.sb.append(String.join(", ", paramList));
        this.sb.append("):");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
        }
        methodReturn.body.accept(this);
        nlIndent();
        this.sb.append("return ");
        methodReturn.returnValue.accept(this);
        decrIndentation();
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        this.sb.append("if ");
        methodIfReturn.oraCondition.accept(this);
        if ( !methodIfReturn.oraReturnValue.getKind().hasName("EMPTY_EXPR") ) {
            this.sb.append(": return ");
            methodIfReturn.oraReturnValue.accept(this);
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
    public Void visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        this.sb.append("print(");
        serialWriteAction.value.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    protected void generateCodeFromTernary(TernaryExpr ternaryExpr) {
        ternaryExpr.thenPart.accept(this);
        this.sb.append(whitespace() + "if" + whitespace() + "(" + whitespace());
        ternaryExpr.condition.accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + "else" + whitespace());
        ternaryExpr.elsePart.accept(this);
    }

    @Override
    protected void generateCodeFromIfElse(IfStmt ifStmt) {
        int stmtSize = ifStmt.expr.size();
        for ( int i = 0; i < stmtSize; i++ ) {
            if ( i == 0 ) {
                generateCodeFromStmtCondition("if", ifStmt.expr.get(i));
            } else {
                nlIndent();
                generateCodeFromStmtCondition("elif", ifStmt.expr.get(i));
            }
            incrIndentation();
            StmtList then = ifStmt.thenList.get(i);
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
    protected void generateCodeFromElse(IfStmt ifStmt) {
        if ( !ifStmt.elseList.get().isEmpty() ) {
            nlIndent();
            this.sb.append("else:");
            incrIndentation();
            ifStmt.elseList.accept(this);
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

    private void generateCodeRightExpression(Binary binary, Binary.Op op) {
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

    protected void generateCodeFromStmtCondition(String stmtType, Expr expr) {
        this.sb.append(stmtType).append(whitespace());
        expr.accept(this);
        this.sb.append(":");
    }

    protected void generateCodeFromStmtConditionFor(String stmtType, Expr expr) {
        this.sb.append(stmtType).append(whitespace());
        ExprList expressions = (ExprList) expr;
        expressions.get().get(0).accept(this);
        this.sb.append(whitespace() + "in range(int(");
        expressions.get().get(1).accept(this);
        this.sb.append(")," + whitespace() + "int(");
        expressions.get().get(2).accept(this);
        this.sb.append(")," + whitespace() + "int(");
        expressions.get().get(3).accept(this);
        this.sb.append(")):");
    }

    protected void appendBreakStmt(RepeatStmt repeatStmt) {
        nlIndent();
        this.sb.append("break");
    }

    protected void appendTry() {
        increaseLoopCounter();

        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) ) {
            incrIndentation();
            nlIndent();
            this.sb.append("try:");
        }
    }

    protected void appendExceptionHandling() {
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) ) {
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
        this.currentLoop.removeLast();
    }

    protected void appendPassIfEmptyBody(RepeatStmt repeatStmt) {
        if ( repeatStmt.list.get().isEmpty() ) {
            if ( repeatStmt.mode != RepeatStmt.Mode.WAIT ) {
                nlIndent();
                this.sb.append("pass");
            }
        }
    }

    @Override
    protected String getBinaryOperatorSymbol(Binary.Op op) {
        return AbstractPythonVisitor.binaryOpSymbols().get(op);
    }

    @Override
    protected String getUnaryOperatorSymbol(Unary.Op op) {
        return AbstractPythonVisitor.unaryOpSymbols().get(op);
    }

    protected static Map<Binary.Op, String> binaryOpSymbols() {
        return Collections
            .unmodifiableMap(
                Stream
                    .of(

                        AbstractLanguageVisitor.entry(Binary.Op.ADD, "+"),
                        AbstractLanguageVisitor.entry(Binary.Op.MINUS, "-"),
                        AbstractLanguageVisitor.entry(Binary.Op.MULTIPLY, "*"),
                        AbstractLanguageVisitor.entry(Binary.Op.DIVIDE, "/"),
                        AbstractLanguageVisitor.entry(Binary.Op.MOD, "%"),
                        AbstractLanguageVisitor.entry(Binary.Op.EQ, "=="),
                        AbstractLanguageVisitor.entry(Binary.Op.NEQ, "!="),
                        AbstractLanguageVisitor.entry(Binary.Op.LT, "<"),
                        AbstractLanguageVisitor.entry(Binary.Op.LTE, "<="),
                        AbstractLanguageVisitor.entry(Binary.Op.GT, ">"),
                        AbstractLanguageVisitor.entry(Binary.Op.GTE, ">="),
                        AbstractLanguageVisitor.entry(Binary.Op.AND, "and"),
                        AbstractLanguageVisitor.entry(Binary.Op.OR, "or"),
                        AbstractLanguageVisitor.entry(Binary.Op.MATH_CHANGE, "+="),
                        AbstractLanguageVisitor.entry(Binary.Op.TEXT_APPEND, "+="),
                        AbstractLanguageVisitor.entry(Binary.Op.IN, "in"),
                        AbstractLanguageVisitor.entry(Binary.Op.ASSIGNMENT, "="),
                        AbstractLanguageVisitor.entry(Binary.Op.ADD_ASSIGNMENT, "+="),
                        AbstractLanguageVisitor.entry(Binary.Op.MINUS_ASSIGNMENT, "-="),
                        AbstractLanguageVisitor.entry(Binary.Op.MULTIPLY_ASSIGNMENT, "*="),
                        AbstractLanguageVisitor.entry(Binary.Op.DIVIDE_ASSIGNMENT, "/="),
                        AbstractLanguageVisitor.entry(Binary.Op.MOD_ASSIGNMENT, "%=")

                    )
                    .collect(AbstractLanguageVisitor.entriesToMap()));
    }

    protected static Map<Unary.Op, String> unaryOpSymbols() {
        return Collections
            .unmodifiableMap(
                Stream
                    .of(

                        AbstractLanguageVisitor.entry(Unary.Op.PLUS, "+"),
                        AbstractLanguageVisitor.entry(Unary.Op.NEG, "-"),
                        AbstractLanguageVisitor.entry(Unary.Op.NOT, "not"),
                        AbstractLanguageVisitor.entry(Unary.Op.POSTFIX_INCREMENTS, "++"),
                        AbstractLanguageVisitor.entry(Unary.Op.PREFIX_INCREMENTS, "++")

                    )
                    .collect(AbstractLanguageVisitor.entriesToMap()));
    }

    @Override
    public Void visitStmtTextComment(StmtTextComment stmtTextComment) {
        this.sb.append("# " + stmtTextComment.textComment.replace("\n", " "));
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt assertStmt) {
        this.sb.append("if not ");
        assertStmt.asserts.accept(this);
        this.sb.append(":");
        incrIndentation();
        nlIndent();
        this.sb.append("print(\"Assertion failed: \", \"").append(assertStmt.msg).append("\", ");
        ((Binary) assertStmt.asserts).left.accept(this);
        this.sb.append(", \"").append(((Binary) assertStmt.asserts).op.toString()).append("\", ");
        ((Binary) assertStmt.asserts).getRight().accept(this);
        this.sb.append(")");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        this.sb.append("print(");
        debugAction.value.accept(this);
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
