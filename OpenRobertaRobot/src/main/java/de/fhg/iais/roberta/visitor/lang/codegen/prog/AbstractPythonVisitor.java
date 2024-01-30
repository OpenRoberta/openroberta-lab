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
import de.fhg.iais.roberta.syntax.lang.functions.IsListEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfListFunct;
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
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetBiasStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetInputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetWeightStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon.Flow;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.TextAppendStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
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
            this.src.add("float(");
            super.visitNumConst(numConst);
            this.src.add(")");
        }
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst boolConst) {
        this.src.add(boolConst.value ? "True" : "False");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst nullConst) {
        this.src.add("None");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst mathConst) {
        switch ( mathConst.mathConst ) {
            case PI:
                this.src.add("math.pi");
                break;
            case E:
                this.src.add("math.e");
                break;
            case GOLDEN_RATIO:
                this.src.add("(1 + 5 ** 0.5) / 2");
                break;
            case SQRT2:
                this.src.add("math.sqrt(2)");
                break;
            case SQRT1_2:
                this.src.add("math.sqrt(0.5)");
                break;
            case INFINITY:
                this.src.add("float('inf')");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        this.usedGlobalVarInFunctions.add(var.getCodeSafeName());
        this.src.add(var.getCodeSafeName());
        this.src.add(" = ");
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
            this.src.add("None");
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        try {
            VarDeclaration variablePart = (VarDeclaration) binary.left;
            this.src.add(variablePart.getCodeSafeName());
        } catch ( ClassCastException e ) {
            generateSubExpr(this.src, false, binary.left, binary);
        }
        //if ( variablePart.getValue().getClass().equals(EmptyExpr.class) ) {
        Binary.Op op = binary.op;
        String sym = getBinaryOperatorSymbol(op);
        this.src.add(' ', sym, ' ');
        generateCodeRightExpression(binary, op);
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                this.src.add("\"\"");
                break;
            case BOOLEAN:
                this.src.add("True");
                break;
            case NUMBER_INT:
                this.src.add("0");
                break;
            case NUMBER:
                this.src.add("0.0");
                break;
            case CAPTURED_TYPE:
                this.src.add("None");
                break;
            case ARRAY:
                this.src.add("None");
                break;
            case NULL:
                break;
            default:
                this.src.add("[[EmptyExpr [defVal=", emptyExpr.getDefVal(), "]]]");
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
                this.src.add("raise ", (stmtFlowCon.flow == Flow.BREAK ? "BreakOutOfALoop" : "ContinueLoop"));
                return null;
            }
        }
        this.src.add(stmtFlowCon.flow.toString().toLowerCase());
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList emptyList) {
        this.src.add("[]");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        this.src.add("math.pow(");
        super.visitMathPowerFunct(mathPowerFunct);
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        switch ( mathSingleFunct.functName ) {
            case SQUARE:
                this.src.add("math.pow(");
                mathSingleFunct.param.get(0).accept(this);
                this.src.add(", 2)");
                return null;
            case ROOT:
                this.src.add("math.sqrt(");
                break;
            case ABS:
                this.src.add("math.fabs(");
                break;
            case LN:
                this.src.add("math.log(");
                break;
            case LOG10:
                this.src.add("math.log10(");
                break;
            case EXP:
                this.src.add("math.exp(");
                break;
            case POW10:
                this.src.add("math.pow(10, ");
                break;
            case SIN:
                this.src.add("math.sin(");
                break;
            case COS:
                this.src.add("math.cos(");
                break;
            case TAN:
                this.src.add("math.tan(");
                break;
            case ASIN:
                this.src.add("math.asin(");
                break;
            case ATAN:
                this.src.add("math.atan(");
                break;
            case ACOS:
                this.src.add("math.acos(");
                break;
            case ROUND:
                this.src.add("round(");
                break;
            case ROUNDUP:
                this.src.add("math.ceil(");
                break;
            case ROUNDDOWN:
                this.src.add("math.floor(");
                break;
            default:
                break;
        }
        mathSingleFunct.param.get(0).accept(this);
        this.src.add(")");

        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        this.src.add("min(max(");
        mathConstrainFunct.value.accept(this);
        this.src.add(", ");
        mathConstrainFunct.lowerBound.accept(this);
        this.src.add("), ");
        mathConstrainFunct.upperBound.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        switch ( mathNumPropFunct.functName ) {
            case EVEN:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % 2) == 0");
                break;
            case ODD:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % 2) == 1");
                break;
            case PRIME:
                String methodName = this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.PRIME);
                this.src.add(methodName, "(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(")");
                break;
            case WHOLE:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % 1) == 0");
                break;
            case POSITIVE:
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" > 0");
                break;
            case NEGATIVE:
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" < 0");
                break;
            case DIVISIBLE_BY:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % ");
                mathNumPropFunct.param.get(1).accept(this);
                this.src.add(") == 0");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        this.src.add("random.random()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.src.add("random.randint(");
        mathRandomIntFunct.from.accept(this);
        this.src.add(", ");
        mathRandomIntFunct.to.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        switch ( mathOnListFunct.functName ) {
            case SUM:
                this.src.add("sum(");
                mathOnListFunct.list.accept(this);
                this.src.add(")");
                break;
            case MIN:
                this.src.add("min(");
                mathOnListFunct.list.accept(this);
                this.src.add(")");
                break;
            case MAX:
                this.src.add("max(");
                mathOnListFunct.list.accept(this);
                this.src.add(")");
                break;
            case AVERAGE:
                this.src.add("float(sum(");
                mathOnListFunct.list.accept(this);
                this.src.add("))/len(");
                mathOnListFunct.list.accept(this);
                this.src.add(")");
                break;
            case MEDIAN:
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.MEDIAN), "(");
                mathOnListFunct.list.accept(this);
                this.src.add(")");
                break;
            case STD_DEV:
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.STD_DEV), "(");
                mathOnListFunct.list.accept(this);
                this.src.add(")");
                break;
            case RANDOM:
                mathOnListFunct.list.accept(this);
                this.src.add("[0]");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        this.src.add("str(");
        mathCastStringFunct.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        this.src.add("chr((int)(");
        mathCastCharFunct.value.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitTextAppendStmt(TextAppendStmt textAppendStmt) {
        textAppendStmt.var.accept(this);
        this.src.add(" += str(");
        textAppendStmt.text.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        this.src.add("float(");
        textStringCastNumberFunct.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        this.src.add("ord(");
        textCharCastNumberFunct.value.accept(this);
        this.src.add("[");
        textCharCastNumberFunct.atIndex.accept(this);
        this.src.add("])");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        this.src.add("\"\".join(str(arg) for arg in [");
        textJoinFunct.param.accept(this);
        this.src.add("])");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate listCreate) {
        this.src.add("[");
        listCreate.exprList.accept(this);
        this.src.add("]");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex listGetIndex) {
        listGetIndex.param.get(0).accept(this);

        if ( listGetIndex.getElementOperation() == GET ) {
            this.src.add("[");
        } else if ( listGetIndex.getElementOperation() == REMOVE || listGetIndex.getElementOperation() == GET_REMOVE ) {
            this.src.add(".pop(");
        }

        switch ( (IndexLocation) listGetIndex.location ) {
            case RANDOM: // backwards compatibility
                // TODO?
            case FIRST:
                this.src.add("0");
                break;
            case FROM_END:
                this.src.add("-1 -"); // TODO should be correct but how is it handled on other robots?
                listGetIndex.param.get(1).accept(this);
                break;
            case FROM_START:
                listGetIndex.param.get(1).accept(this);
                break;
            case LAST:
                this.src.add("-1");
                break;
            default:
                break;
        }
        if ( listGetIndex.getElementOperation() == GET ) {
            this.src.add("]");
        } else if ( listGetIndex.getElementOperation() == REMOVE || listGetIndex.getElementOperation() == GET_REMOVE ) {
            this.src.add(")");
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex listSetIndex) {
        listSetIndex.param.get(0).accept(this);
        if ( listSetIndex.mode == SET ) {
            this.src.add("[");
        } else if ( listSetIndex.mode == INSERT ) {
            this.src.add(".insert(");
        }
        switch ( (IndexLocation) listSetIndex.location ) {
            case RANDOM: // backwards compatibility
                // TODO?
            case FIRST:
                this.src.add("0");
                break;
            case FROM_END:
                this.src.add("-1 -");
                listSetIndex.param.get(2).accept(this);
                break;
            case FROM_START:
                listSetIndex.param.get(2).accept(this);
                break;
            case LAST:
                this.src.add("-1");
                break;
            default:
                break;
        }
        if ( listSetIndex.mode == SET ) {
            this.src.add("] = ");
            listSetIndex.param.get(1).accept(this);
        } else if ( listSetIndex.mode == INSERT ) {
            this.src.add(", ");
            listSetIndex.param.get(1).accept(this);
            this.src.add(")");
        }
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat listRepeat) {
        this.src.add("[");
        listRepeat.param.get(0).accept(this);
        this.src.add("] * ");
        listRepeat.param.get(1).accept(this);
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        switch ( (IndexLocation) indexOfFunct.location ) {
            case FIRST:
                indexOfFunct.value.accept(this);
                this.src.add(".index(");
                indexOfFunct.find.accept(this);
                this.src.add(")");
                break;
            case LAST:
                this.src.add("(len(");
                indexOfFunct.value.accept(this);
                this.src.add(") - 1) - ");
                indexOfFunct.value.accept(this);
                this.src.add("[::-1].index(");
                indexOfFunct.find.accept(this);
                this.src.add(")");
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
            this.src.add("[");
            switch ( (IndexLocation) getSubFunct.strParam.get(0) ) {
                case FIRST:
                    this.src.add("0:");
                    break;
                case FROM_END:
                    this.src.add("-1 -");
                    getSubFunct.param.get(1).accept(this);
                    this.src.add(":");
                    break;
                case FROM_START:
                    getSubFunct.param.get(1).accept(this);
                    this.src.add(":");
                    break;
                default:
                    break;
            }
            switch ( (IndexLocation) getSubFunct.strParam.get(1) ) {
                case LAST:
                    // append nothing
                    break;
                case FROM_END:
                    this.src.add("-1 -");
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
            this.src.add("]");
        }
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        this.src.add("print(");
        textPrintFunct.param.get(0).accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitLengthOfListFunct(LengthOfListFunct lengthOfListFunct) {
        this.src.add("len( ");
        lengthOfListFunct.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitIsListEmptyFunct(IsListEmptyFunct isListEmptyFunct) {
        this.src.add("not ");
        isListEmptyFunct.value.accept(this);
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid methodVoid) {
        nlIndent();
        this.src.add("def ", methodVoid.getCodeSafeMethodName(), '(');
        List<String> paramList = new ArrayList<>();
        for ( Expr l : methodVoid.getParameters().get() ) {
            paramList.add(((VarDeclaration) l).getCodeSafeName());
        }
        this.src.add(String.join(", ", paramList));
        this.src.add("):");
        incrIndentation();
        boolean isMethodBodyEmpty = methodVoid.body.get().isEmpty();
        if ( isMethodBodyEmpty ) {
            nlIndent();
            this.src.add("pass");
        } else {
            if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
                nlIndent();
                this.src.add("global ", String.join(", ", this.usedGlobalVarInFunctions));
            }
            methodVoid.body.accept(this);
        }
        decrIndentation();
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn methodReturn) {
        nlIndent();
        this.src.add("def ", methodReturn.getCodeSafeMethodName(), '(');
        List<String> paramList = new ArrayList<>();
        for ( Expr l : methodReturn.getParameters().get() ) {
            paramList.add(((VarDeclaration) l).getCodeSafeName());
        }
        this.src.add(String.join(", ", paramList));
        this.src.add("):");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.src.add("global ", String.join(", ", this.usedGlobalVarInFunctions));
        }
        methodReturn.body.accept(this);
        nlIndent();
        this.src.add("return ");
        methodReturn.returnValue.accept(this);
        decrIndentation();
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        this.src.add("if ");
        methodIfReturn.oraCondition.accept(this);
        if ( !methodIfReturn.oraReturnValue.getKind().hasName("EMPTY_EXPR") ) {
            this.src.add(": return ");
            methodIfReturn.oraReturnValue.accept(this);
        } else {
            this.src.add(": return None");
        }
        return null;
    }

    @Override
    public Void visitNNStepStmt(NNStepStmt nnStepStmt) {
        this.src.add("____nnStep()");
        return null;
    }

    @Override
    public Void visitNNSetInputNeuronVal(NNSetInputNeuronVal setVal) {
        this.src.add("global ____").add(setVal.name);
        this.src.nlI().add("____").add(setVal.name).add(" = ");
        setVal.value.accept(this);
        return null;
    }

    @Override
    public Void visitNNSetWeightStmt(NNSetWeightStmt chgStmt) {
        this.src.add("global ____w_", chgStmt.from, "_", chgStmt.to);
        this.src.nlI().add("____w_", chgStmt.from, "_", chgStmt.to, " = ");
        chgStmt.value.accept(this);
        return null;
    }

    @Override
    public Void visitNNSetBiasStmt(NNSetBiasStmt chgStmt) {
        this.src.add("global ____b_", chgStmt.name);
        this.src.nlI().add("____b_", chgStmt.name, " = ");
        chgStmt.value.accept(this);
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
        this.src.add("print(");
        serialWriteAction.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    protected void generateCodeFromTernary(TernaryExpr ternaryExpr) {
        ternaryExpr.thenPart.accept(this);
        this.src.add(" if ( ");
        ternaryExpr.condition.accept(this);
        this.src.add(" ) else ");
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
                this.src.add("pass");
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
            this.src.add("else:");
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
            this.src.add("pass");
        }
    }

    private void generateCodeRightExpression(Binary binary, Binary.Op op) {
        switch ( op ) {
            case DIVIDE:
                this.src.add("float(");
                generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
                this.src.add(")");
                break;
            default:
                generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
                break;
        }
    }

    protected void generateCodeFromStmtCondition(String stmtType, Expr expr) {
        this.src.add(stmtType, " ");
        expr.accept(this);
        this.src.add(":");
    }

    protected void generateCodeFromStmtConditionFor(String stmtType, Expr expr) {
        this.src.add(stmtType, " ");
        ExprList expressions = (ExprList) expr;
        expressions.get().get(0).accept(this);
        this.src.add(" in range(int(");
        expressions.get().get(1).accept(this);
        this.src.add("), ", "int(");
        expressions.get().get(2).accept(this);
        this.src.add("), ", "int(");
        expressions.get().get(3).accept(this);
        this.src.add(")):");
    }

    protected void appendBreakStmt(RepeatStmt repeatStmt) {
        nlIndent();
        this.src.add("break");
    }

    protected void appendTry() {
        increaseLoopCounter();

        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) ) {
            incrIndentation();
            nlIndent();
            this.src.add("try:");
        }
    }

    protected void appendExceptionHandling() {
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) ) {
            decrIndentation();
            nlIndent();
            this.src.add("except BreakOutOfALoop:");
            incrIndentation();
            nlIndent();
            this.src.add("break");
            decrIndentation();
            nlIndent();
            this.src.add("except ContinueLoop:");
            incrIndentation();
            nlIndent();
            this.src.add("continue");
            decrIndentation();
        }
        this.currentLoop.removeLast();
    }

    protected void appendPassIfEmptyBody(RepeatStmt repeatStmt) {
        if ( repeatStmt.list.get().isEmpty() ) {
            if ( repeatStmt.mode != RepeatStmt.Mode.WAIT ) {
                nlIndent();
                this.src.add("pass");
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
        this.src.add("# ", stmtTextComment.textComment.replace("\n", " "));
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt assertStmt) {
        this.src.add("if not ");
        assertStmt.asserts.accept(this);
        this.src.add(":");
        incrIndentation();
        nlIndent();
        this.src.add("print(\"Assertion failed: \", \"", assertStmt.msg, "\", ");
        ((Binary) assertStmt.asserts).left.accept(this);
        this.src.add(", \"", ((Binary) assertStmt.asserts).op.toString(), "\", ");
        ((Binary) assertStmt.asserts).getRight().accept(this);
        this.src.add(")");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        this.src.add("print(");
        debugAction.value.accept(this);
        this.src.add(")");
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
            this.src.add(helperMethodImpls);
        }

        nlIndent();
        this.src.add("if __name__ == \"__main__\":");
        incrIndentation();
        nlIndent();
        this.src.add("main()");
        decrIndentation();
    }

    /**
     * Visits the Blockly wait-until-block ("robControls_wait_for")
     *
     * @param waitStmt to be visited
     * @return null
     */
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.src.add("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        addWaitStatementTimeout();
        decrIndentation();
        return null;
    }
   
    protected abstract void addWaitStatementTimeout();
}
