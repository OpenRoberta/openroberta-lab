package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon.Flow;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public abstract class RobotPythonVisitor extends CommonLanguageVisitor {
    protected Set<String> usedGlobalVarInFunctions;
    protected boolean isProgramEmpty = false;

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    protected RobotPythonVisitor(ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(programPhrases, indentation);
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        // TODO Do we have always to cast to float
        if ( isInteger(numConst.getValue()) ) {
            super.visitNumConst(numConst);
        } else {
            sb.append("float(");
            super.visitNumConst(numConst);
            sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        sb.append(boolConst.isValue() ? "True" : "False");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        sb.append("None");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        switch ( mathConst.getMathConst() ) {
            case PI:
                sb.append("math.pi");
                break;
            case E:
                sb.append("math.e");
                break;
            case GOLDEN_RATIO:
                sb.append("BlocklyMethods.GOLDEN_RATIO");
                break;
            case SQRT2:
                sb.append("math.sqrt(2)");
                break;
            case SQRT1_2:
                sb.append("math.sqrt(1.0/2.0)");
                break;
            case INFINITY:
                sb.append("float('inf')");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        usedGlobalVarInFunctions.add(var.getName());
        sb.append(var.getName());
        if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
            sb.append(" = ");
            if ( var.getValue().getKind().hasName("EXPR_LIST") ) {
                ExprList<Void> list = (ExprList<Void>) var.getValue();
                if ( list.get().size() == 2 ) {
                    list.get().get(1).visit(this);
                } else {
                    list.get().get(0).visit(this);
                }
            } else {
                var.getValue().visit(this);
            }
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        generateSubExpr(sb, false, binary.getLeft(), binary);
        Binary.Op op = binary.getOp();
        String sym = getBinaryOperatorSymbol(op);
        sb.append(' ').append(sym).append(' ');
        generateCodeRightExpression(binary, op);
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                sb.append("\"\"");
                break;
            case BOOLEAN:
                sb.append("True");
                break;
            case NUMBER_INT:
                sb.append("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            default:
                sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
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
                throw new DbcException("Invalide Repeat Statement!");
        }
        incrIndentation();
        appendPassIfEmptyBody(repeatStmt);
        repeatStmt.getList().visit(this);
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
        if ( loopsLabels.get(currenLoop.getLast()) != null ) {
            if ( loopsLabels.get(currenLoop.getLast()) ) {
                sb.append("raise " + (stmtFlowCon.getFlow() == Flow.BREAK ? "BreakOutOfALoop" : "ContinueLoop"));
                return null;
            }
        }
        sb.append(stmtFlowCon.getFlow().toString().toLowerCase());
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        sb.append("[]");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        sb.append("math.pow(");
        super.visitMathPowerFunct(mathPowerFunct);
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
            case ROOT:
                sb.append("math.sqrt(");
                break;
            case ABS:
                sb.append("math.fabs(");
                break;
            case LN:
                sb.append("math.log(");
                break;
            case LOG10:
                sb.append("math.log10(");
                break;
            case EXP:
                sb.append("math.exp(");
                break;
            case POW10:
                sb.append("math.pow(10, ");
                break;
            case SIN:
                sb.append("math.sin(");
                break;
            case COS:
                sb.append("math.cos(");
                break;
            case TAN:
                sb.append("math.tan(");
                break;
            case ASIN:
                sb.append("math.asin(");
                break;
            case ATAN:
                sb.append("math.atan(");
                break;
            case ACOS:
                sb.append("math.acos(");
                break;
            case ROUND:
                sb.append("round(");
                break;
            case ROUNDUP:
                sb.append("math.ceil(");
                break;
            case ROUNDDOWN:
                sb.append("math.floor(");
                break;
            default:
                break;
        }
        mathSingleFunct.getParam().get(0).visit(this);
        sb.append(")");

        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        sb.append("\ndef ").append(methodVoid.getMethodName()).append('(');
        List<String> paramList = new ArrayList<>();
        for ( Expr<Void> l : methodVoid.getParameters().get() ) {
            paramList.add(((VarDeclaration<Void>) l).getName());
        }
        sb.append(String.join(", ", paramList));
        sb.append("):");
        boolean isMethodBodyEmpty = methodVoid.getBody().get().isEmpty();
        if ( isMethodBodyEmpty ) {
            nlIndent();
            sb.append("pass");
        } else {
            if ( !usedGlobalVarInFunctions.isEmpty() ) {
                nlIndent();
                sb.append("global " + String.join(", ", usedGlobalVarInFunctions));
            }
            methodVoid.getBody().visit(this);
        }
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        sb.append("\ndef ").append(methodReturn.getMethodName()).append('(');
        List<String> paramList = new ArrayList<>();
        for ( Expr<Void> l : methodReturn.getParameters().get() ) {
            paramList.add(((VarDeclaration<Void>) l).getName());
        }
        sb.append(String.join(", ", paramList));
        sb.append("):");
        boolean isMethodBodyEmpty = methodReturn.getBody().get().isEmpty();
        if ( isMethodBodyEmpty ) {
            nlIndent();
            sb.append("pass");
        } else {
            if ( !usedGlobalVarInFunctions.isEmpty() ) {
                nlIndent();
                sb.append("global " + String.join(", ", usedGlobalVarInFunctions));
            }
            methodReturn.getBody().visit(this);
            nlIndent();
            sb.append("return ");
            methodReturn.getReturnValue().visit(this);
        }
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        sb.append("if ");
        methodIfReturn.getCondition().visit(this);
        if ( !methodIfReturn.getReturnValue().getKind().hasName("EMPTY_EXPR") ) {
            sb.append(": return ");
            methodIfReturn.getReturnValue().visit(this);
        } else {
            sb.append(": return None");
        }
        return null;
    }

    @Override
    public String getEnumCode(IMode value) {
        return "'" + value.toString().toLowerCase() + "'";
    }

    @Override
    protected void generateCodeFromTernary(IfStmt<Void> ifStmt) {
        ((ExprStmt<Void>) ifStmt.getThenList().get(0).get().get(0)).getExpr().visit(this);
        sb.append(whitespace() + "if" + whitespace() + "(" + whitespace());
        ifStmt.getExpr().get(0).visit(this);
        sb.append(whitespace() + ")" + whitespace() + "else" + whitespace());
        ((ExprStmt<Void>) ifStmt.getElseList().get().get(0)).getExpr().visit(this);
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
                sb.append("pass");
            } else {
                then.visit(this);
            }
            decrIndentation();
        }
    }

    @Override
    protected void generateCodeFromElse(IfStmt<Void> ifStmt) {
        if ( !ifStmt.getElseList().get().isEmpty() ) {
            nlIndent();
            sb.append("else:");
            incrIndentation();
            ifStmt.getElseList().visit(this);
            decrIndentation();
        }
    }

    @Override
    protected String getLanguageVarTypeFromBlocklyType(BlocklyType type) {
        return "";
    }

    protected void addPassIfProgramIsEmpty() {
        if ( isProgramEmpty ) {
            nlIndent();
            sb.append("pass");
        }
    }

    private void generateCodeRightExpression(Binary<Void> binary, Binary.Op op) {
        switch ( op ) {
            case TEXT_APPEND:
                sb.append("str(");
                generateSubExpr(sb, false, binary.getRight(), binary);
                sb.append(")");
                break;
            case DIVIDE:
                sb.append("float(");
                generateSubExpr(sb, parenthesesCheck(binary), binary.getRight(), binary);
                sb.append(")");
                break;
            default:
                generateSubExpr(sb, parenthesesCheck(binary), binary.getRight(), binary);
                break;
        }
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        sb.append(stmtType).append(whitespace());
        expr.visit(this);
        sb.append(":");
    }

    private void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        sb.append(stmtType).append(whitespace());
        ExprList<Void> expressions = (ExprList<Void>) expr;
        expressions.get().get(0).visit(this);
        sb.append(whitespace() + "in range(");
        expressions.get().get(1).visit(this);
        sb.append("," + whitespace());
        expressions.get().get(2).visit(this);
        sb.append("," + whitespace());
        expressions.get().get(3).visit(this);
        sb.append("):");
    }

    private void appendBreakStmt(RepeatStmt<Void> repeatStmt) {
        nlIndent();
        sb.append("break");
    }

    private void appendTry() {
        increaseLoopCounter();

        if ( loopsLabels.get(currenLoop.getLast()) ) {
            incrIndentation();
            nlIndent();
            sb.append("try:");
        }
    }

    private void appendExceptionHandling() {
        if ( loopsLabels.get(currenLoop.getLast()) ) {
            decrIndentation();
            nlIndent();
            sb.append("except BreakOutOfALoop:");
            incrIndentation();
            nlIndent();
            sb.append("break");
            decrIndentation();
            nlIndent();
            sb.append("except ContinueLoop:");
            incrIndentation();
            nlIndent();
            sb.append("continue");
            decrIndentation();
        }
        currenLoop.removeLast();
    }

    private void appendPassIfEmptyBody(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getList().get().isEmpty() ) {
            if ( repeatStmt.getMode() != RepeatStmt.Mode.WAIT ) {
                nlIndent();
                sb.append("pass");
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
        return Collections.unmodifiableMap(
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
        sb.append("# " + stmtTextComment.getTextComment());
        return null;
    };
}
