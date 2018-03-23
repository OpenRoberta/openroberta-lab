package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
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
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable JAVA code representation of a phrase to a
 * StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public abstract class RobotJavaVisitor extends CommonLanguageVisitor {
    protected final String programName;
    protected boolean isInDebugMode = false;

    /**
     * initialize the Java code generator visitor.
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param usedSensors in the current program
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    protected RobotJavaVisitor(ArrayList<ArrayList<Phrase<Void>>> programPhrases, String programName, int indentation) {
        super(programPhrases, indentation);
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
                this.sb.append("BlocklyMethods.PI");
                break;
            case E:
                this.sb.append("BlocklyMethods.E");
                break;
            case GOLDEN_RATIO:
                this.sb.append("BlocklyMethods.GOLDEN_RATIO");
                break;
            case SQRT2:
                this.sb.append("BlocklyMethods.sqrt(2)");
                break;
            case SQRT1_2:
                this.sb.append("BlocklyMethods.sqrt(1.0/2.0)");
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
        repeatStmt.getList().visit(this);
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
        if ( this.loopsLabels.get(this.currenLoop.getLast()) != null ) {
            if ( this.loopsLabels.get(this.currenLoop.getLast()) ) {
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
        textPrintFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitStmtTextComment(StmtTextComment<Void> stmtTextComment) {
        this.sb.append("// " + stmtTextComment.getTextComment());
        return null;
    };

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        this.sb.append(
            "new ArrayList<"
                + getLanguageVarTypeFromBlocklyType(emptyList.getTypeVar()).substring(0, 1).toUpperCase()
                + getLanguageVarTypeFromBlocklyType(emptyList.getTypeVar()).substring(1).toLowerCase()
                + ">()");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        this.sb.append("BlocklyMethods.");
        switch ( mathSingleFunct.getFunctName() ) {
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
                this.sb.append("pow(10, ");
                break;
            case SIN:
                this.sb.append("sin(");
                break;
            case COS:
                this.sb.append("cos(");
                break;
            case TAN:
                this.sb.append("tan(");
                break;
            case ASIN:
                this.sb.append("asin(");
                break;
            case ATAN:
                this.sb.append("atan(");
                break;
            case ACOS:
                this.sb.append("acos(");
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
        mathSingleFunct.getParam().get(0).visit(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        this.sb.append("\n").append(INDENT).append("private void ");
        this.sb.append(methodVoid.getMethodName() + "(");
        methodVoid.getParameters().visit(this);
        this.sb.append(") {");
        methodVoid.getBody().visit(this);
        this.sb.append("\n").append(INDENT).append("}");
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        this.sb.append("\n").append(INDENT).append("private " + getLanguageVarTypeFromBlocklyType(methodReturn.getReturnType()));
        this.sb.append(" " + methodReturn.getMethodName() + "(");
        methodReturn.getParameters().visit(this);
        this.sb.append(") {");
        methodReturn.getBody().visit(this);
        nlIndent();
        this.sb.append("return ");
        methodReturn.getReturnValue().visit(this);
        this.sb.append(";\n").append(INDENT).append("}");
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        this.sb.append("if (");
        methodIfReturn.getCondition().visit(this);
        this.sb.append(") ");
        this.sb.append("return ");
        methodIfReturn.getReturnValue().visit(this);
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
        this.sb.append("(" + whitespace());
        ifStmt.getExpr().get(0).visit(this);
        this.sb.append(whitespace() + ")" + whitespace() + "?" + whitespace());
        ((ExprStmt<Void>) ifStmt.getThenList().get(0).get().get(0)).getExpr().visit(this);
        this.sb.append(whitespace() + ":" + whitespace());
        ((ExprStmt<Void>) ifStmt.getElseList().get().get(0)).getExpr().visit(this);
    }

    @Override
    protected void generateCodeFromIfElse(IfStmt<Void> ifStmt) {
        int exprSize = ifStmt.getExpr().size();
        String conditionStmt = "if";
        for ( int i = 0; i < exprSize; i++ ) {
            generateCodeFromStmtCondition(conditionStmt, ifStmt.getExpr().get(i));
            conditionStmt = "else" + whitespace() + "if";
            incrIndentation();
            ifStmt.getThenList().get(i).visit(this);
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
            ifStmt.getElseList().visit(this);
            decrIndentation();
        }
        nlIndent();
        this.sb.append("}");
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType + whitespace() + "(" + whitespace());
        expr.visit(this);
        this.sb.append(whitespace() + ")" + whitespace() + "{");
    }

    private void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType + whitespace() + "(" + whitespace() + "float" + whitespace());
        ExprList<Void> expressions = (ExprList<Void>) expr;
        expressions.get().get(0).visit(this);
        this.sb.append(whitespace() + "=" + whitespace());
        expressions.get().get(1).visit(this);
        this.sb.append(";" + whitespace());
        expressions.get().get(0).visit(this);
        int posOpenBracket = expressions.get().toString().lastIndexOf("[");
        int posClosedBracket = expressions.get().toString().lastIndexOf("]");
        int counterPos = expressions.get().toString().lastIndexOf("-");
        if ( counterPos > posOpenBracket && counterPos < posClosedBracket ) {
            this.sb.append(">" + whitespace());
        } else {
            this.sb.append("<" + whitespace());
        }
        expressions.get().get(2).visit(this);
        this.sb.append(";" + whitespace());
        expressions.get().get(0).visit(this);
        this.sb.append("+=" + whitespace());
        expressions.get().get(3).visit(this);
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
        if ( this.loopsLabels.get(this.currenLoop.getLast()) ) {
            this.sb.append("loop" + this.currenLoop.getLast() + ":");
            nlIndent();
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
        return Collections.unmodifiableMap(
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
        return Collections.unmodifiableMap(
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
