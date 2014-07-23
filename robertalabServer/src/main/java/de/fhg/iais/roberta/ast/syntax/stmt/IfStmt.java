package de.fhg.iais.roberta.ast.syntax.stmt;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>if-else-elseif</b> blocks from Blockly into the AST (abstract syntax
 * tree).
 * Object from this class will generate if statement.<br/>
 */
public class IfStmt extends Stmt {
    private final List<Expr> expr;
    private final List<StmtList> thenList;
    private final StmtList elseList;

    private IfStmt(List<Expr> expr, List<StmtList> thenList, StmtList elseList) {
        super(Phrase.Kind.IfStmt);
        Assert.isTrue(expr.size() == thenList.size() && elseList.isReadOnly());
        this.expr = expr;
        this.thenList = thenList;
        this.elseList = elseList;
        setReadOnly();
    }

    /**
     * create <b>if-then-else</b> statement where we have one or more the one <b>if</b> and <b>then</b>.
     * 
     * @param expr list of all expressions that should be evaluated in the <b>if</b> parts
     * @param thenList all statements that are in the <b>then</b> parts
     * @param elseList all statements that are in the <b>else</b> parts
     * @return read only object of class {@link IfStmt}
     */
    public static IfStmt make(List<Expr> expr, List<StmtList> thenList, StmtList elseList) {
        return new IfStmt(expr, thenList, elseList);
    }

    /**
     * create ternary operator
     * 
     * @param expr expression that should be evaluated in the <b>if</b> part
     * @param thenList statement that is in the <b>then</b> part
     * @param elseList all statement that is in the <b>else</b> part
     * @return read only object of class {@link IfStmt}
     */
    public static IfStmt make(Expr expr, StmtList thenList, StmtList elseList) {
        List<Expr> exprsList = new ArrayList<Expr>();
        List<StmtList> thensList = new ArrayList<StmtList>();
        exprsList.add(expr);
        thensList.add(thenList);
        return new IfStmt(exprsList, thensList, elseList);
    }

    /**
     * create <b>if-then</b> statement where we have one or more the one <b>if</b> and <b>then</b>.
     * 
     * @param expr list of all expressions that should be evaluated in the <b>if</b> parts
     * @param thenList all statements that are in the <b>then</b> parts
     * @return read only object of class {@link IfStmt}
     */
    public static IfStmt make(List<Expr> expr, List<StmtList> thenList) {
        StmtList elseList = StmtList.make();
        elseList.setReadOnly();
        return new IfStmt(expr, thenList, elseList);
    }

    /**
     * @return list with all expressions that should be evaluated in the <b>if</b> part.
     */
    public final List<Expr> getExpr() {
        return this.expr;
    }

    /**
     * @return list with all statements that are in <b>then</b> part.
     */
    public final List<StmtList> getThenList() {
        return this.thenList;
    }

    /**
     * @return list with all statements that are in <b>else</b> part.
     */
    public final StmtList getElseList() {
        return this.elseList;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        int next = indentation + 3;
        appendNewLine(sb, indentation, null);
        for ( int i = 0; i < this.expr.size(); i++ ) {
            sb.append("if ( ");
            this.expr.get(i).generateJava(sb, indentation);
            sb.append(" ) {");
            this.thenList.get(i).generateJava(sb, next);
            if ( i + 1 < this.expr.size() ) {
                appendNewLine(sb, indentation, "} else ");
            }
        }
        if ( this.elseList.get().size() != 0 ) {
            appendNewLine(sb, indentation, "} else {");
            this.elseList.generateJava(sb, next);
        }
        appendNewLine(sb, indentation, "}");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendNewLine(sb, 0, null);
        for ( int i = 0; i < this.expr.size(); i++ ) {
            sb.append("if ").append(this.expr.get(i));
            appendNewLine(sb, 0, ",then");
            sb.append(this.thenList.get(i).toString());
            if ( i + 1 < this.expr.size() ) {
                appendNewLine(sb, 0, ",else ");
            }
        }
        if ( this.elseList.get().size() != 0 ) {
            appendNewLine(sb, 0, ",else");
            sb.append(this.elseList.toString());
        }
        appendNewLine(sb, 0, "");
        return sb.toString();
    }

}
