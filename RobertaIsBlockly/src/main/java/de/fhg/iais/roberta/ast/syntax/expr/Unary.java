package de.fhg.iais.roberta.ast.syntax.expr;

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
        PLUS,
        NEG,
        NOT,
        ROOT,
        ABS,
        LN,
        LOG10,
        EXP,
        POW10,
        SIN,
        COS,
        TAN,
        ASIN,
        ACOS,
        ATAN,
        EVEN,
        ODD,
        PRIME,
        WHOLE,
        POSITIVE,
        NEGATIVE,
        ROUND,
        ROUNDUP,
        ROUNDDOWN,
        SUM,
        MIN,
        MAX,
        AVERAGE,
        MEDIAN,
        MODE,
        STD_DEV,
        RANDOM,
        TEXT_JOIN,
        TEXT_LENGTH,
        IS_EMPTY,
        UPPERCASE,
        LOWERCASE,
        TITLECASE,
        LEFT,
        RIGHT,
        BOTH,
        TEXT,
        NUMBER,
        LISTS_LENGTH;

        public static Op get(String s) {
            if ( "+".equals(s) ) {
                return PLUS;
            } else if ( "-".equals(s) ) {
                return NEG;
            } else if ( "!".equals(s) ) {
                return NOT;
            } else if ( "sqrt".equals(s) ) {
                return ROOT;
            } else if ( "abs".equals(s) ) {
                return ABS;
            } else if ( "ln".equals(s) ) {
                return LN;
            } else if ( "log".equals(s) ) {
                return LOG10;
            } else if ( "exp".equals(s) ) {
                return EXP;
            } else if ( "pow10".equals(s) ) {
                return POW10;
            } else if ( "sin".equals(s) ) {
                return SIN;
            } else if ( "cos".equals(s) ) {
                return COS;
            } else if ( "tan".equals(s) ) {
                return TAN;
            } else if ( "asin".equals(s) ) {
                return ASIN;
            } else if ( "acos".equals(s) ) {
                return ACOS;
            } else if ( "atan".equals(s) ) {
                return ATAN;
            } else if ( "even".equals(s) ) {
                return EVEN;
            } else if ( "odd".equals(s) ) {
                return ODD;
            } else if ( "prime".equals(s) ) {
                return PRIME;
            } else if ( "whole".equals(s) ) {
                return WHOLE;
            } else if ( "positive".equals(s) ) {
                return POSITIVE;
            } else if ( "negative".equals(s) ) {
                return NEGATIVE;
            } else if ( "round".equals(s) ) {
                return ROUND;
            } else if ( "ceil".equals(s) ) {
                return ROUNDUP;
            } else if ( "floor".equals(s) ) {
                return ROUNDDOWN;
            } else if ( "sum".equals(s) ) {
                return SUM;
            } else if ( "min".equals(s) ) {
                return MIN;
            } else if ( "max".equals(s) ) {
                return MAX;
            } else if ( "average".equals(s) ) {
                return AVERAGE;
            } else if ( "median".equals(s) ) {
                return MEDIAN;
            } else if ( "mode".equals(s) ) {
                return MODE;
            } else if ( "standardDeviation".equals(s) ) {
                return STD_DEV;
            } else if ( "random".equals(s) ) {
                return RANDOM;
            } else if ( "text_join".equals(s) ) {
                return TEXT_JOIN;
            } else if ( "text_length".equals(s) ) {
                return TEXT_LENGTH;
            } else if ( "is_empty".equals(s) ) {
                return IS_EMPTY;
            } else if ( "uppercase".equals(s) ) {
                return UPPERCASE;
            } else if ( "lowercase".equals(s) ) {
                return LOWERCASE;
            } else if ( "titlecase".equals(s) ) {
                return TITLECASE;
            } else if ( "left".equals(s) ) {
                return LEFT;
            } else if ( "right".equals(s) ) {
                return RIGHT;
            } else if ( "both".equals(s) ) {
                return BOTH;
            } else if ( "text".equals(s) ) {
                return TEXT;
            } else if ( "number".equals(s) ) {
                return NUMBER;
            } else if ( "lists_length".equals(s) ) {
                return LISTS_LENGTH;
            } else {
                throw new DbcException("invalid unary operator: " + s);
            }
        }
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append("(");
        sb.append(this.op).append(" ");
        this.expr.toStringBuilder(sb, indentation);
        sb.append(")");

    }

}
