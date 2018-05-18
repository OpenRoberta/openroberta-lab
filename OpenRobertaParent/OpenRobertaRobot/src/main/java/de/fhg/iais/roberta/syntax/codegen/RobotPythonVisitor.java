package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.factory.AbstractCompilerWorkflow;
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
    private static final ch.qos.logback.classic.Logger LOG = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(AbstractCompilerWorkflow.class);

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    protected RobotPythonVisitor(ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(programPhrases, indentation);
        LOG.setLevel(ch.qos.logback.classic.Level.TRACE);
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
        this.sb.append(boolConst.isValue() ? "True" : "False");
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
                this.sb.append("BlocklyMethods.GOLDEN_RATIO");
                break;
            case SQRT2:
                this.sb.append("math.sqrt(2)");
                break;
            case SQRT1_2:
                this.sb.append("math.sqrt(1.0/2.0)");
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
        this.usedGlobalVarInFunctions.add(var.getName());
        this.sb.append(var.getName());
        this.sb.append(" = ");
        if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
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
        } else {
            this.sb.append("None");
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        try {
            VarDeclaration<Void> variablePart = (VarDeclaration<Void>) binary.getLeft();
            this.sb.append(variablePart.getName());
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
        if ( this.loopsLabels.get(this.currenLoop.getLast()) != null ) {
            if ( this.loopsLabels.get(this.currenLoop.getLast()) ) {
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
        mathSingleFunct.getParam().get(0).visit(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        this.sb.append("\ndef ").append(methodVoid.getMethodName()).append('(');
        List<String> paramList = new ArrayList<>();
        for ( Expr<Void> l : methodVoid.getParameters().get() ) {
            paramList.add(((VarDeclaration<Void>) l).getName());
        }
        this.sb.append(String.join(", ", paramList));
        this.sb.append("):");
        boolean isMethodBodyEmpty = methodVoid.getBody().get().isEmpty();
        if ( isMethodBodyEmpty ) {
            nlIndent();
            this.sb.append("pass");
        } else {
            if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
                nlIndent();
                this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
            }
            methodVoid.getBody().visit(this);
        }
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        this.sb.append("\ndef ").append(methodReturn.getMethodName()).append('(');
        List<String> paramList = new ArrayList<>();
        for ( Expr<Void> l : methodReturn.getParameters().get() ) {
            paramList.add(((VarDeclaration<Void>) l).getName());
        }
        this.sb.append(String.join(", ", paramList));
        this.sb.append("):");
        boolean isMethodBodyEmpty = methodReturn.getBody().get().isEmpty();
        if ( isMethodBodyEmpty ) {
            nlIndent();
            this.sb.append("pass");
        } else {
            if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
                nlIndent();
                this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
            }
            methodReturn.getBody().visit(this);
            nlIndent();
            this.sb.append("return ");
            methodReturn.getReturnValue().visit(this);
        }
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        this.sb.append("if ");
        methodIfReturn.getCondition().visit(this);
        if ( !methodIfReturn.getReturnValue().getKind().hasName("EMPTY_EXPR") ) {
            this.sb.append(": return ");
            methodIfReturn.getReturnValue().visit(this);
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
    protected void generateCodeFromTernary(IfStmt<Void> ifStmt) {
        ((ExprStmt<Void>) ifStmt.getThenList().get(0).get().get(0)).getExpr().visit(this);
        this.sb.append(whitespace() + "if" + whitespace() + "(" + whitespace());
        ifStmt.getExpr().get(0).visit(this);
        this.sb.append(whitespace() + ")" + whitespace() + "else" + whitespace());
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
                this.sb.append("pass");
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
            this.sb.append("else:");
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
        if ( this.isProgramEmpty ) {
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

    private void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType).append(whitespace());
        expr.visit(this);
        this.sb.append(":");
    }

    private void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType).append(whitespace());
        ExprList<Void> expressions = (ExprList<Void>) expr;
        expressions.get().get(0).visit(this);
        this.sb.append(whitespace() + "in range(int(");
        expressions.get().get(1).visit(this);
        this.sb.append(")," + whitespace() + "int(");
        expressions.get().get(2).visit(this);
        this.sb.append(")," + whitespace() + "int(");
        expressions.get().get(3).visit(this);
        this.sb.append(")):");
    }

    private void appendBreakStmt(RepeatStmt<Void> repeatStmt) {
        nlIndent();
        this.sb.append("break");
    }

    private void appendTry() {
        increaseLoopCounter();

        if ( this.loopsLabels.get(this.currenLoop.getLast()) ) {
            incrIndentation();
            nlIndent();
            this.sb.append("try:");
        }
    }

    private void appendExceptionHandling() {
        if ( this.loopsLabels.get(this.currenLoop.getLast()) ) {
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

    private void appendPassIfEmptyBody(RepeatStmt<Void> repeatStmt) {
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
        this.sb.append("# " + stmtTextComment.getTextComment());
        return null;
    };
}
