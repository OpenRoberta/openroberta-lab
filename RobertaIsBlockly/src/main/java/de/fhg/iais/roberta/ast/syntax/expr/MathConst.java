package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

public class MathConst extends Phrase {
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
        return mathConst;
    }

    @Override
    public String toString() {
        return "MathConst [" + mathConst + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append(mathConst);
    }

    public static enum Const {
        GOLDEN_RATIO, PI, E, SQRT2, SQRT1_2, INFINITY;
    }
}
