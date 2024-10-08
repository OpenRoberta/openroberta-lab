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
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
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
import de.fhg.iais.roberta.syntax.lang.stmt.MathChangeStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.TextAppendStmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlockDescriptor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.FunctionNames;
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
    protected AbstractJavaVisitor(List<List<Phrase>> programPhrases, String programName, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
        this.programName = programName;
    }

    @Override
    public Void visitNumConst(NumConst numConst) {
        // TODO Do we have always to cast to float
        if ( isInteger(numConst.value) ) {
            super.visitNumConst(numConst);
        } else {
            this.src.add("((float) ");
            super.visitNumConst(numConst);
            this.src.add(")");
        }
        return null;
    }

    @Override
    public Void visitNullConst(NullConst nullConst) {
        this.src.add("null");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst mathConst) {
        switch ( mathConst.mathConst ) {
            case PI:
                this.src.add("(float) Math.PI");
                break;
            case E:
                this.src.add("(float) Math.E");
                break;
            case GOLDEN_RATIO:
                this.src.add("(float) ((1.0 + Math.sqrt(5.0)) / 2.0)");
                break;
            case SQRT2:
                this.src.add("(float) Math.sqrt(2)");
                break;
            case SQRT1_2:
                this.src.add("(float) Math.sqrt(0.5)");
                break;
            case INFINITY:
                this.src.add("Float.POSITIVE_INFINITY");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        Op op = binary.op;
        if ( isEqualityOpOnStrings(binary) ) {
            generateCodeForStringEqualityOp(binary);
            return null;
        }
        generateSubExpr(this.src, false, binary.left, binary);
        String sym = getBinaryOperatorSymbol(op);
        this.src.add(" ", sym, " ");
        switch ( op ) {
            case DIVIDE:
                this.src.add("((float) ");
                generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
                this.src.add(")");
                break;
            default:
                generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
                break;
        }
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
            case NUMBER_INT:
                this.src.add("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            default:
                this.src.add("null");
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
    public Void visitRepeatStmt(RepeatStmt repeatStmt) {
        boolean additionalClosingBracket = false;
        boolean isWaitStmt = repeatStmt.mode == RepeatStmt.Mode.WAIT;
        switch ( repeatStmt.mode ) {
            case FOREVER:
                /*
                 *This ""if ( true ) {" statement is needed because when we have code after the "while ( true ) "
                 *statement is unreachable
                 */
                this.src.add("if ( true ) {");
                additionalClosingBracket = true;
                incrIndentation();
                nlIndent();
            case UNTIL:
            case WHILE:
                addLabelToLoop();
                generateCodeFromStmtCondition("while", repeatStmt.expr);
                break;
            case TIMES:
            case FOR:
                addLabelToLoop();
                generateCodeFromStmtConditionFor("for", repeatStmt.expr);
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.expr);
                break;
            case FOR_EACH:
                addLabelToLoop();
                generateCodeFromStmtCondition("for", repeatStmt.expr);
                break;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.list.accept(this);
        if ( !isWaitStmt ) {
            this.currentLoop.removeLast();
        } else {
            appendBreakStmt();
        }
        decrIndentation();
        nlIndent();
        this.src.add("}");
        if ( additionalClosingBracket ) {
            decrIndentation();
            nlIndent();
            this.src.add("}");
        }
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
                this.src.add("if (true) ", stmtFlowCon.flow.toString().toLowerCase(), " loop", this.currentLoop.getLast(), ";");
                return null;
            }
        }
        this.src.add(stmtFlowCon.flow.toString().toLowerCase(), ";");
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        this.src.add("System.out.println(");
        textPrintFunct.param.get(0).accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList emptyList) {
        this.src.add(
            "new ArrayList<",
            getLanguageVarTypeFromBlocklyType(emptyList.typeVar).substring(0, 1).toUpperCase(),
            getLanguageVarTypeFromBlocklyType(emptyList.typeVar).substring(1).toLowerCase(),
            ">()");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat listRepeat) {
        this.src.add("new ArrayList<>(Collections.nCopies( (int) ");
        listRepeat.param.get(1).accept(this);
        this.src.add(", ");
        if ( listRepeat.param.get(0).getBlocklyType() == BlocklyType.NUMBER ) {
            this.src.add(" (float) ");
        }
        listRepeat.param.get(0).accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        switch ( mathSingleFunct.functName ) {
            case SQUARE:
                this.src.add("(float) Math.pow(");
                mathSingleFunct.param.get(0).accept(this);
                this.src.add(", 2)");
                return null;
            case ROOT:
                this.src.add("(float) Math.sqrt(");
                break;
            case LN:
                this.src.add("(float) Math.log(");
                break;
            case POW10:
                this.src.add("(float) Math.pow(10, ");
                break;
            case ROUNDUP:
                this.src.add("(float) Math.ceil(");
                break;
            case ROUNDDOWN:
                this.src.add("(float) Math.floor(");
                break;
            default:
                this.src.add("(float) Math.");
                this.src.add(mathSingleFunct.functName.name().toLowerCase(Locale.ENGLISH), "(");
                break;
        }
        mathSingleFunct.param.get(0).accept(this);
        this.src.add(")");

        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        this.src.add("Math.min(Math.max(");
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
                this.src.add(" % 2 == 0)");
                break;
            case ODD:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % 2 == 1)");
                break;
            case PRIME:
                String methodName = this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.PRIME);
                this.src.add(methodName, "( (int) ");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(")");
                break;
            case WHOLE:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % 1 == 0)");
                break;
            case POSITIVE:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" > 0)");
                break;
            case NEGATIVE:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" < 0)");
                break;
            case DIVISIBLE_BY:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % ");
                mathNumPropFunct.param.get(1).accept(this);
                this.src.add(" == 0)");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        switch ( mathOnListFunct.functName ) {
            case MIN:
                this.src.add("Collections.min(");
                mathOnListFunct.list.accept(this);
                break;
            case MAX:
                this.src.add("Collections.max(");
                mathOnListFunct.list.accept(this);
                break;
            case RANDOM:
                mathOnListFunct.list.accept(this);
                this.src.add(".get(0"); // TODO remove? implement?
                break;
            default:
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(mathOnListFunct.functName), "(");
                mathOnListFunct.list.accept(this);
                break;
        }
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        this.src.add("(float) Math.random()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.src.add("( Math.round(Math.random() * ((");
        mathRandomIntFunct.to.accept(this);
        this.src.add(") - (");
        mathRandomIntFunct.from.accept(this);
        this.src.add("))) + (");
        mathRandomIntFunct.from.accept(this);
        this.src.add(") )");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        this.src.add("(float) Math.pow(");
        super.visitMathPowerFunct(mathPowerFunct);
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        this.src.add("(String.valueOf(");
        mathCastStringFunct.value.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        this.src.add("String.valueOf((char)(int)(");
        mathCastCharFunct.value.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitMathChangeStmt(MathChangeStmt mathChangeStmt) {
        super.visitMathChangeStmt(mathChangeStmt);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitTextAppendStmt(TextAppendStmt textAppendStmt) {
        textAppendStmt.var.accept(this);
        this.src.add(" += String.valueOf(");
        textAppendStmt.text.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        this.src.add("Float.parseFloat(");
        textStringCastNumberFunct.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        this.src.add("(int)(");
        textCharCastNumberFunct.value.accept(this);
        this.src.add(".charAt(");
        textCharCastNumberFunct.atIndex.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        List<Expr> exprs = textJoinFunct.param.get();
        Iterator<Expr> iterator = exprs.iterator();
        while ( iterator.hasNext() ) {
            this.src.add("String.valueOf(");
            Expr expr = iterator.next();
            expr.accept(this);
            this.src.add(")");
            if ( iterator.hasNext() ) {
                this.src.add(" + ");
            }
        }
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid methodVoid) {
        nlIndent();
        this.src.add("private void ");
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
        this.src.add("private ", getLanguageVarTypeFromBlocklyType(methodReturn.getReturnType()));
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
        BlocklyProperties blocklyProperties = methodStmt.getProperty();
        if ( blocklyProperties.blockType.equals("robProcedures_ifreturn") ) {
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
    public Void visitStringConst(StringConst stringConst) {
        this.src.add("\"", StringEscapeUtils.escapeJava(stringConst.value.replaceAll("[<>\\$]", "")), "\"");
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt assertStmt) {
        this.src.add("if (!(");
        assertStmt.asserts.accept(this);
        this.src.add(")) {");
        incrIndentation();
        nlIndent();
        this.src.add("System.out.println(\"Assertion failed: \" + \"", assertStmt.msg, "\" + ");
        ((Binary) assertStmt.asserts).left.accept(this);
        this.src.add(" + \"", ((Binary) assertStmt.asserts).op.toString(), "\" + ");
        ((Binary) assertStmt.asserts).getRight().accept(this);
        this.src.add(");");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        this.src.add("System.out.println(");
        debugAction.value.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        this.src.add("System.out.println(");
        serialWriteAction.value.accept(this);
        this.src.add(");");
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
            case CAPTURED_TYPE_ARRAY_ITEM:
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

    private boolean isStringExpr(Expr e) {
        switch ( e.getKind().getName() ) {
            case "STRING_CONST":
                return true;
            case "VAR":
                return e.getBlocklyType() == BlocklyType.STRING;
            case "FUNCTION_EXPR":
                BlockDescriptor functionKind = ((FunctionExpr) e).getFunction().getKind();
                return functionKind.hasName("TEXT_JOIN_FUNCT", "LIST_INDEX_OF");
            case "METHOD_EXPR":
                MethodCall methodCall = (MethodCall) ((MethodExpr) e).getMethod();
                return methodCall.getKind().hasName("METHOD_CALL") && methodCall.getReturnType() == BlocklyType.STRING;
            case "ACTION_EXPR":
                Action action = ((ActionExpr) e).action;
                return action.getKind().hasName("BLUETOOTH_RECEIVED_ACTION");

            default:
                return false;
        }
    }

    @Override
    protected void generateCodeFromTernary(TernaryExpr ternaryExpr) {
        this.src.add("( ", "( ");
        ternaryExpr.condition.accept(this);
        this.src.add(" ) ", "? ");
        ternaryExpr.thenPart.accept(this);
        this.src.add(" : ");
        ternaryExpr.elsePart.accept(this);
        this.src.add(" )");
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

    private void generateCodeFromStmtCondition(String stmtType, Expr expr) {
        this.src.add(stmtType, " ( ");
        expr.accept(this);
        this.src.add(" ) {");
    }

    private void generateCodeFromStmtConditionFor(String stmtType, Expr expr) {
        this.src.add(stmtType, " ( ", "float ");
        ExprList expressions = (ExprList) expr;
        expressions.get().get(0).accept(this);
        this.src.add(" = ");
        expressions.get().get(1).accept(this);
        this.src.add("; ");
        expressions.get().get(0).accept(this);
        int posOpenBracket = expressions.get().toString().lastIndexOf("[");
        int posClosedBracket = expressions.get().toString().lastIndexOf("]");
        int counterPos = expressions.get().toString().lastIndexOf("-");
        if ( counterPos > posOpenBracket && counterPos < posClosedBracket ) {
            this.src.add("> ");
        } else {
            this.src.add("< ");
        }
        expressions.get().get(2).accept(this);
        this.src.add("; ");
        expressions.get().get(0).accept(this);
        this.src.add("+= ");
        expressions.get().get(3).accept(this);
        this.src.add(" ) {");
    }

    private boolean isEqualityOpOnStrings(Binary binary) {
        boolean isLeftAndRightString = isStringExpr(binary.left) && isStringExpr(binary.getRight());
        boolean isEqualityOp = binary.op == Op.EQ || binary.op == Op.NEQ;
        return isEqualityOp && isLeftAndRightString;
    }

    private void generateCodeForStringEqualityOp(Binary binary) {
        if ( binary.op == Op.NEQ ) {
            this.src.add("!");
        }
        generateSubExpr(this.src, false, binary.left, binary);
        this.src.add(".equals(");
        generateSubExpr(this.src, false, binary.getRight(), binary);
        this.src.add(")");
    }

    private void appendBreakStmt() {
        nlIndent();
        this.src.add("break;");
    }

    private void addLabelToLoop() {
        increaseLoopCounter();
        if ( this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().get(this.currentLoop.getLast()) ) {
            this.src.add("loop", this.currentLoop.getLast(), ":");
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
                    this.src.add(it.next());
                    if ( it.hasNext() ) {
                        nlIndent();
                    }
                }
                decrIndentation();
                nlIndent();
            }
        }

        this.src.add("}");
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

    @Override
    protected void visitorGenerateNN() {
        generateNNStuff("java");
    }

    //TODO 1650 remove these once all visitors implement the methods
    @Override
    protected void visitorGenerateHelperMethods() {

    }

    @Override
    protected void visitorGenerateImports() {

    }

    @Override
    protected void visitorGenerateGlobalVariables() {

    }
    //TODO 1650 END

}
