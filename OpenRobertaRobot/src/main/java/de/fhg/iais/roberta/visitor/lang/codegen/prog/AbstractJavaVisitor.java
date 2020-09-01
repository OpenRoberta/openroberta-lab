package de.fhg.iais.roberta.visitor.lang.codegen.prog;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.text.StringEscapeUtils;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
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
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable JAVA code representation of a phrase to a
 * StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public abstract class AbstractJavaVisitor extends AbstractLanguageVisitor {
    protected final String programName;
    protected boolean isInDebugMode = false;

    /**
     * initialize the Java code generator visitor.
     *
     * @param programPhrases to generate the code from
     * @param programName name of the program
     */
    protected AbstractJavaVisitor(List<List<Phrase<Void>>> programPhrases, String programName, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
        this.programName = programName;
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        // TODO Do we have always to cast to float
        if ( isInteger(numConst.getValue()) ) {
            super.visitNumConst(numConst);
        } else {
            this.sb.append("((float) ");
            super.visitNumConst(numConst);
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        this.sb.append("null");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        switch ( mathConst.getMathConst() ) {
            case PI:
                this.sb.append("(float) Math.PI");
                break;
            case E:
                this.sb.append("(float) Math.E");
                break;
            case GOLDEN_RATIO:
                this.sb.append("(float) ((1.0 + Math.sqrt(5.0)) / 2.0)");
                break;
            case SQRT2:
                this.sb.append("(float) Math.sqrt(2)");
                break;
            case SQRT1_2:
                this.sb.append("(float) Math.sqrt(0.5)");
                break;
            case INFINITY:
                this.sb.append("Float.POSITIVE_INFINITY");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        Op op = binary.getOp();
        if ( isEqualityOpOnStrings(binary) ) {
            generateCodeForStringEqualityOp(binary);
            return null;
        }
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        String sym = getBinaryOperatorSymbol(op);
        this.sb.append(whitespace() + sym + whitespace());
        switch ( op ) {
            case TEXT_APPEND:
                generateCodeToStringCastOnExpr(binary);
                break;
            case DIVIDE:
                this.sb.append("((float) ");
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
                this.sb.append(")");
                break;
            default:
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
                break;
        }
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
            case NUMBER_INT:
                this.sb.append("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            default:
                this.sb.append("null");
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
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        boolean additionalClosingBracket = false;
        boolean isWaitStmt = repeatStmt.getMode() == RepeatStmt.Mode.WAIT;
        switch ( repeatStmt.getMode() ) {
            case FOREVER:
                /*
                 *This ""if ( true ) {" statement is needed because when we have code after the "while ( true ) "
                 *statement is unreachable
                 */
                this.sb.append("if ( true ) {");
                additionalClosingBracket = true;
                incrIndentation();
                nlIndent();
            case UNTIL:
            case WHILE:
                addLabelToLoop();
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                break;
            case TIMES:
            case FOR:
                addLabelToLoop();
                generateCodeFromStmtConditionFor("for", repeatStmt.getExpr());
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                addLabelToLoop();
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                break;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.getList().accept(this);
        if ( !isWaitStmt ) {
            this.currenLoop.removeLast();
        } else {
            appendBreakStmt();
        }
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        if ( additionalClosingBracket ) {
            decrIndentation();
            nlIndent();
            this.sb.append("}");
        }
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
                this.sb.append("if (true) " + stmtFlowCon.getFlow().toString().toLowerCase() + " loop" + this.currenLoop.getLast() + ";");
                return null;
            }
        }
        this.sb.append(stmtFlowCon.getFlow().toString().toLowerCase() + ";");
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        this.sb.append("System.out.println(");
        textPrintFunct.getParam().get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        this.sb
            .append(
                "new ArrayList<"
                    + getLanguageVarTypeFromBlocklyType(emptyList.getTypeVar()).substring(0, 1).toUpperCase()
                    + getLanguageVarTypeFromBlocklyType(emptyList.getTypeVar()).substring(1).toLowerCase()
                    + ">()");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.sb.append("new ArrayList<>(Collections.nCopies( (int) ");
        listRepeat.getParam().get(1).accept(this);
        this.sb.append(", ");
        if ( listRepeat.getParam().get(0).getVarType() == BlocklyType.NUMBER ) {
            this.sb.append(" (float) ");
        }
        listRepeat.getParam().get(0).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
            case SQUARE:
                this.sb.append("(float) Math.pow(");
                mathSingleFunct.getParam().get(0).accept(this);
                this.sb.append(", 2)");
                return null;
            case ROOT:
                this.sb.append("(float) Math.sqrt(");
                break;
            case LN:
                this.sb.append("(float) Math.log(");
                break;
            case POW10:
                this.sb.append("(float) Math.pow(10, ");
                break;
            case ROUNDUP:
                this.sb.append("(float) Math.ceil(");
                break;
            case ROUNDDOWN:
                this.sb.append("(float) Math.floor(");
                break;
            default:
                this.sb.append("(float) Math.");
                this.sb.append(mathSingleFunct.getFunctName().name().toLowerCase(Locale.ENGLISH)).append("(");
                break;
        }
        mathSingleFunct.getParam().get(0).accept(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("Math.min(Math.max(");
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
                this.sb.append(" % 2 == 0)");
                break;
            case ODD:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" % 2 == 1)");
                break;
            case PRIME:
                String methodName = this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.PRIME);
                this.sb.append(methodName).append("( (int) ");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            case WHOLE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" % 1 == 0)");
                break;
            case POSITIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" > 0)");
                break;
            case NEGATIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" < 0)");
                break;
            case DIVISIBLE_BY:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" % ");
                mathNumPropFunct.getParam().get(1).accept(this);
                this.sb.append(" == 0)");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch ( mathOnListFunct.getFunctName() ) {
            case MIN:
                this.sb.append("Collections.min(");
                mathOnListFunct.getParam().get(0).accept(this);
                break;
            case MAX:
                this.sb.append("Collections.max(");
                mathOnListFunct.getParam().get(0).accept(this);
                break;
            case RANDOM:
                mathOnListFunct.getParam().get(0).accept(this);
                this.sb.append(".get(0"); // TODO remove? implement?
                break;
            default:
                this.sb
                    .append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(mathOnListFunct.getFunctName()))
                    .append("(");
                mathOnListFunct.getParam().get(0).accept(this);
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("(float) Math.random()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("Math.round(Math.random() * (");
        mathRandomIntFunct.getParam().get(1).accept(this);
        this.sb.append(" - ");
        mathRandomIntFunct.getParam().get(0).accept(this);
        this.sb.append(")) + ");
        mathRandomIntFunct.getParam().get(0).accept(this);
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.sb.append("(float) Math.pow(");
        super.visitMathPowerFunct(mathPowerFunct);
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        this.sb.append("(String.valueOf(");
        mathCastStringFunct.getParam().get(0).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        this.sb.append("String.valueOf((char)(int)(");
        mathCastCharFunct.getParam().get(0).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        this.sb.append("Float.parseFloat(");
        textStringCastNumberFunct.getParam().get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        this.sb.append("(int)(");
        textCharCastNumberFunct.getParam().get(0).accept(this);
        this.sb.append(".charAt(");
        textCharCastNumberFunct.getParam().get(1).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        List<Expr<Void>> exprs = textJoinFunct.getParam().get();
        Iterator<Expr<Void>> iterator = exprs.iterator();
        while ( iterator.hasNext() ) {
            this.sb.append("String.valueOf(");
            Expr<Void> expr = iterator.next();
            expr.accept(this);
            this.sb.append(")");
            if ( iterator.hasNext() ) {
                this.sb.append(" + ");
            }
        }
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        nlIndent();
        this.sb.append("private void ");
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
        this.sb.append("private ").append(getLanguageVarTypeFromBlocklyType(methodReturn.getReturnType()));
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
    public Void visitStringConst(StringConst<Void> stringConst) {
        this.sb.append("\"").append(StringEscapeUtils.escapeJava(stringConst.getValue().replaceAll("[<>\\$]", ""))).append("\"");
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt<Void> assertStmt) {
        this.sb.append("if (!(");
        assertStmt.getAssert().accept(this);
        this.sb.append(")) {");
        incrIndentation();
        nlIndent();
        this.sb.append("System.out.println(\"Assertion failed: \" + \"").append(assertStmt.getMsg()).append("\" + ");
        ((Binary<Void>) assertStmt.getAssert()).getLeft().accept(this);
        this.sb.append(" + \"").append(((Binary<Void>) assertStmt.getAssert()).getOp().toString()).append("\" + ");
        ((Binary<Void>) assertStmt.getAssert()).getRight().accept(this);
        this.sb.append(");");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction<Void> debugAction) {
        this.sb.append("System.out.println(");
        debugAction.getValue().accept(this);
        this.sb.append(");");
        return null;
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
                return "List";
            case ARRAY_NUMBER:
                return "ArrayList<Float>";
            case ARRAY_STRING:
                return "ArrayList<String>";
            case ARRAY_COLOUR:
                return "ArrayList<PickColor>";
            case ARRAY_BOOLEAN:
                return "ArrayList<Boolean>";
            case ARRAY_CONNECTION:
                return "ArrayList<NXTConnection>";
            case BOOLEAN:
                return "boolean";
            case NUMBER:
                return "float";
            case NUMBER_INT:
                return "int";
            case STRING:
                return "String";
            case COLOR:
                return "PickColor";
            case VOID:
                return "void";
            case CONNECTION:
                return "NXTConnection";
            default:
                throw new IllegalArgumentException("unhandled type");
        }
    }

    private boolean isStringExpr(Expr<Void> e) {
        switch ( e.getKind().getName() ) {
            case "STRING_CONST":
                return true;
            case "VAR":
                return ((Var<?>) e).getVarType() == BlocklyType.STRING;
            case "FUNCTION_EXPR":
                BlockType functionKind = ((FunctionExpr<?>) e).getFunction().getKind();
                return functionKind.hasName("TEXT_JOIN_FUNCT", "LIST_INDEX_OF");
            case "METHOD_EXPR":
                MethodCall<?> methodCall = (MethodCall<?>) ((MethodExpr<?>) e).getMethod();
                return methodCall.getKind().hasName("METHOD_CALL") && methodCall.getReturnType() == BlocklyType.STRING;
            case "ACTION_EXPR":
                Action<?> action = ((ActionExpr<?>) e).getAction();
                return action.getKind().hasName("BLUETOOTH_RECEIVED_ACTION");

            default:
                return false;
        }
    }

    @Override
    protected void generateCodeFromTernary(IfStmt<Void> ifStmt) {
        this.sb.append("(" + whitespace() + "(" + whitespace());
        ifStmt.getExpr().get(0).accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + "?" + whitespace());
        ((ExprStmt<Void>) ifStmt.getThenList().get(0).get().get(0)).getExpr().accept(this);
        this.sb.append(whitespace() + ":" + whitespace());
        ((ExprStmt<Void>) ifStmt.getElseList().get().get(0)).getExpr().accept(this);
        this.sb.append(whitespace() + ")");
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

    private void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType + whitespace() + "(" + whitespace());
        expr.accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + "{");
    }

    private void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType + whitespace() + "(" + whitespace() + "float" + whitespace());
        ExprList<Void> expressions = (ExprList<Void>) expr;
        expressions.get().get(0).accept(this);
        this.sb.append(whitespace() + "=" + whitespace());
        expressions.get().get(1).accept(this);
        this.sb.append(";" + whitespace());
        expressions.get().get(0).accept(this);
        int posOpenBracket = expressions.get().toString().lastIndexOf("[");
        int posClosedBracket = expressions.get().toString().lastIndexOf("]");
        int counterPos = expressions.get().toString().lastIndexOf("-");
        if ( counterPos > posOpenBracket && counterPos < posClosedBracket ) {
            this.sb.append(">" + whitespace());
        } else {
            this.sb.append("<" + whitespace());
        }
        expressions.get().get(2).accept(this);
        this.sb.append(";" + whitespace());
        expressions.get().get(0).accept(this);
        this.sb.append("+=" + whitespace());
        expressions.get().get(3).accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + "{");
    }

    private boolean isEqualityOpOnStrings(Binary<Void> binary) {
        boolean isLeftAndRightString = isStringExpr(binary.getLeft()) && isStringExpr(binary.getRight());
        boolean isEqualityOp = binary.getOp() == Op.EQ || binary.getOp() == Op.NEQ;
        return isEqualityOp && isLeftAndRightString;
    }

    private void generateCodeToStringCastOnExpr(Binary<Void> binary) {
        this.sb.append("String.valueOf(");
        generateSubExpr(this.sb, false, binary.getRight(), binary);
        this.sb.append(")");
    }

    private void generateCodeForStringEqualityOp(Binary<Void> binary) {
        if ( binary.getOp() == Op.NEQ ) {
            this.sb.append("!");
        }
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        this.sb.append(".equals(");
        generateSubExpr(this.sb, false, binary.getRight(), binary);
        this.sb.append(")");
    }

    private void appendBreakStmt() {
        nlIndent();
        this.sb.append("break;");
    }

    private void addLabelToLoop() {
        increaseLoopCounter();
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currenLoop.getLast()) ) {
            this.sb.append("loop" + this.currenLoop.getLast() + ":");
            nlIndent();
        }
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
                incrIndentation();
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
                decrIndentation();
                nlIndent();
            }
        }

        this.sb.append("}");
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
