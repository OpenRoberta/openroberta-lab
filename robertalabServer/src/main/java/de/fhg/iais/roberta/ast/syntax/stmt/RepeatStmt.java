package de.fhg.iais.roberta.ast.syntax.stmt;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

public class RepeatStmt extends Stmt {
    private final Mode mode;
    private final Expr expr;
    private final StmtList list;

    private RepeatStmt(Mode mode, Expr expr, StmtList list) {
        super(Phrase.Kind.RepeatStmt);
        Assert.isTrue(expr.isReadOnly() && list.isReadOnly());
        this.expr = expr;
        this.list = list;
        this.mode = mode;
        setReadOnly();
    }

    public static RepeatStmt make(Mode mode, Expr expr, StmtList list) {
        return new RepeatStmt(mode, expr, list);
    }

    public Mode getMode() {
        return this.mode;
    }

    public final Expr getExpr() {
        return this.expr;
    }

    public final StmtList getlist() {
        return this.list;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        int next = indentation + 3;
        appendNewLine(sb, indentation, null);
        sb.append("(repeat [" + this.mode + ", ").append(this.expr).append("]");
        this.list.toStringBuilder(sb, next);
        appendNewLine(sb, indentation, ")");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringBuilder(sb, 0);
        return sb.toString();
    }

    public static enum Mode {
        WHILE(), UNTIL(), TIMES(), FOR(), FOR_EACH();

        private final String[] values;

        private Mode(String... values) {
            this.values = values;
        }

        public static Mode get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid binary operator symbol: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Mode mo : Mode.values() ) {
                if ( mo.toString().equals(sUpper) ) {
                    return mo;
                }
                for ( String value : mo.values ) {
                    if ( sUpper.equals(value) ) {
                        return mo;
                    }
                }
            }
            throw new DbcException("Invalid binary operator symbol: " + s);
        }
    }

}
