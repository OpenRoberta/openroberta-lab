package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

public class Unary extends Expr {
    private final Op op;
    private final Expr expr;

    private Unary(Op op, Expr expr) {
        super(Phrase.Kind.Unary);
        Assert.isTrue(op != null && expr != null && expr.isReadOnly());
        this.op = op;
        this.expr = expr;
        setReadOnly();
    }

    public static Unary make(Op op, Expr expr) {
        return new Unary(op, expr);
    }

    public Op getOp() {
        return this.op;
    }

    public Expr getExpr() {
        return this.expr;
    }

    @Override
    public String toString() {
        return "Unary [" + this.op + ", " + this.expr + "]";
    }

    public static enum Op {
        PLUS( "+" ),
        NEG( "-" ),
        NOT( "!" ),
        ROOT( "SQRT" ),
        ABS(),
        LN(),
        LOG10(),
        EXP(),
        POW10(),
        SIN(),
        COS(),
        TAN(),
        ASIN(),
        ACOS(),
        ATAN(),
        EVEN(),
        ODD(),
        PRIME(),
        WHOLE(),
        POSITIVE(),
        NEGATIVE(),
        ROUND(),
        ROUNDUP( "CEIL" ),
        ROUNDDOWN( "FLOOR" ),
        SUM(),
        MIN(),
        MAX(),
        AVERAGE(),
        MEDIAN(),
        MODE(),
        STD_DEV(),
        RANDOM(),
        TEXT_JOIN(),
        TEXT_LENGTH(),
        IS_EMPTY(),
        UPPERCASE(),
        LOWERCASE(),
        TITLECASE(),
        LEFT(),
        RIGHT(),
        BOTH(),
        TEXT(),
        NUMBER(),
        LISTS_LENGTH();

        private final String[] values;

        private Op(String... values) {
            this.values = values;
        }

        public static Op get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid binary operator symbol: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Op op : Op.values() ) {
                if ( op.toString().equals(sUpper) ) {
                    return op;
                }
                for ( String value : op.values ) {
                    if ( sUpper.equals(value) ) {
                        return op;
                    }
                }
            }
            throw new DbcException("Invalid binary operator symbol: " + s);
        }
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append("(");
        sb.append(this.op).append(" (");
        this.expr.toStringBuilder(sb, indentation);
        sb.append("))");

    }

}
