package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * numerical constant which can be used in expressions. This class extends {@link Expr}.
 * 
 * @author kcvejoski
 */
public class NumConst extends Expr {
    private final String value;

    private NumConst(String value) {
        super(Phrase.Kind.NumConst);
        this.value = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link NumConst}. This instance is read only and can not be modified.
     * 
     * @param value of the numerical constant
     * @return {@link NumConst}
     */
    public static NumConst make(String value) {
        return new NumConst(value);
    }

    /**
     * @return value of the numerical constant
     */
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "NumConst [" + this.value + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append(this.value);
    }
}
