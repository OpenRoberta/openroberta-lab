package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.DbcException;

public class MathConst extends Expr {
    private final Const mathConst;

    private MathConst(Const mathConst) {
        super(Phrase.Kind.MathConst);
        this.mathConst = mathConst;
        setReadOnly();
    }

    public static MathConst make(Const mathConst) {
        return new MathConst(mathConst);
    }

    public Const getMathConst() {
        return this.mathConst;
    }

    @Override
    public String toString() {
        return "MathConst [" + this.mathConst + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append(this.mathConst);
    }

    public static enum Const {
        GOLDEN_RATIO(), PI(), E(), SQRT2(), SQRT1_2(), INFINITY();

        private final String[] values;

        private Const(String... values) {
            this.values = values;
        }

        public static Const get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid binary operator symbol: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Const co : Const.values() ) {
                if ( co.toString().equals(sUpper) ) {
                    return co;
                }
                for ( String value : co.values ) {
                    if ( sUpper.equals(value) ) {
                        return co;
                    }
                }
            }
            throw new DbcException("Invalid binary operator symbol: " + s);
        }
    }
}
