package de.fhg.iais.roberta.visitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.expr.BoolConst;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.expr.ExprList;
import de.fhg.iais.roberta.syntax.expr.NumConst;
import de.fhg.iais.roberta.syntax.expr.StringConst;
import de.fhg.iais.roberta.syntax.expr.Unary;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.methods.MethodCall;
import de.fhg.iais.roberta.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.stmt.StmtList;
import de.fhg.iais.roberta.typecheck.BlocklyType;

public abstract class CommonLanguageVisitor implements AstVisitor<Void> {
    protected ArrayList<ArrayList<Phrase<Void>>> programPhrases;

    protected int indentation;

    protected final StringBuilder sb = new StringBuilder();

    protected int loopCounter = 0;
    protected LinkedList<Integer> currenLoop = new LinkedList<Integer>();
    protected Map<Integer, Boolean> loopsLabels;

    /**
     * initialize the common language code generator visitor.
     *
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    public CommonLanguageVisitor(ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        this.programPhrases = programPhrases;
        this.indentation = indentation;
    }

    /**
     * Get the current indentation of the visitor. Meaningful for tests only.
     *
     * @return indentation value of the visitor.
     */
    public int getIndentation() {
        return this.indentation;
    }

    /**
     * Get the string builder of the visitor. Meaningful for tests only.
     *
     * @return (current state of) the string builder
     */
    public StringBuilder getSb() {
        return this.sb;
    }

    protected void generateCode(boolean withWrapping) {
        generateProgramPrefix(withWrapping);
        generateProgramMainBody(withWrapping);
        generateProgramSuffix(withWrapping);
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        this.sb.append(numConst.getValue());
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        this.sb.append(boolConst.isValue());
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        this.sb.append("\"").append(StringEscapeUtils.escapeEcmaScript(stringConst.getValue().replaceAll("[<>\\$]", ""))).append("\"");
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        this.sb.append(var.getValue());
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar())).append(" ");
        this.sb.append(var.getName());
        if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
            this.sb.append(" = ");
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
    public Void visitUnary(Unary<Void> unary) {
        if ( unary.getOp() == Unary.Op.POSTFIX_INCREMENTS ) {
            generateExprCode(unary, this.sb);
            this.sb.append(unary.getOp().getOpSymbol());
        } else {
            this.sb.append(unary.getOp().getOpSymbol());
            generateExprCode(unary, this.sb);
        }
        return null;
    }

    @Override
    public Void visitExprList(ExprList<Void> exprList) {
        boolean first = true;
        for ( Expr<Void> expr : exprList.get() ) {
            if ( !expr.getKind().hasName("EMPTY_EXPR") ) {
                if ( first ) {
                    first = false;
                } else {
                    this.sb.append(", ");
                }
                expr.visit(this);
            }
        }
        return null;
    }

    @Override
    public Void visitActionStmt(ActionStmt<Void> actionStmt) {
        actionStmt.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        assignStmt.getName().visit(this);
        this.sb.append(" = ");
        assignStmt.getExpr().visit(this);
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        if ( ifStmt.isTernary() ) {
            generateCodeFromTernary(ifStmt);
        } else {
            generateCodeFromIfElse(ifStmt);
            generateCodeFromElse(ifStmt);
        }
        return null;
    }

    @Override
    public Void visitStmtList(StmtList<Void> stmtList) {
        stmtList.get().stream().forEach(stmt -> {
            nlIndent();
            stmt.visit(this);
        });
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        this.sb.append(methodCall.getMethodName() + "(");
        methodCall.getParametersValues().visit(this);
        this.sb.append(")");
        return null;
    }

    protected void generateExprCode(Unary<Void> unary, StringBuilder sb) {
        if ( unary.getExpr().getPrecedence() < unary.getPrecedence() ) {
            sb.append("(");
            unary.getExpr().visit(this);
            sb.append(")");
        } else {
            unary.getExpr().visit(this);
        }
    }

    protected void incrIndentation() {
        this.indentation += 1;
    }

    protected void decrIndentation() {
        this.indentation -= 1;
    }

    protected void indent() {
        if ( this.indentation <= 0 ) {
            return;
        } else {
            for ( int i = 0; i < this.indentation; i++ ) {
                this.sb.append(INDENT);
            }
        }
    }

    protected void nlIndent() {
        this.sb.append("\n");
        indent();
    }

    protected boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch ( NumberFormatException e ) {
            return false;
        }
    }

    protected void increaseLoopCounter() {
        this.loopCounter++;
        this.currenLoop.add(this.loopCounter);
    }

    protected String whitespace() {
        return " ";
    }

    protected String getEnumCode(IMode value) {
        return value.getClass().getSimpleName() + "." + value;
    }

    abstract protected String getLanguageVarTypeFromBlocklyType(BlocklyType type);

    abstract protected void generateCodeFromTernary(IfStmt<Void> ifStmt);

    abstract protected void generateCodeFromIfElse(IfStmt<Void> ifStmt);

    abstract protected void generateCodeFromElse(IfStmt<Void> ifStmt);

    abstract protected void generateProgramMainBody(boolean withWrapping);

    abstract protected void generateProgramPrefix(boolean withWrapping);

    abstract protected void generateProgramSuffix(boolean withWrapping);

}
