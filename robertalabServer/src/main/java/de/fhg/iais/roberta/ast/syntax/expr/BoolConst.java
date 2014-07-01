package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * boolean constant which can be used in expressions. This class extends {@link Expr}.
 * 
 * @author kcvejoski
 */
public class BoolConst extends Expr {
    private final boolean value;

    private BoolConst(boolean value) {
        super(Phrase.Kind.BoolConst);
        this.value = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link BoolConst}. This instance is read only and can not be modified.
     * 
     * @param value that the boolean constant will have
     * @return {@link BoolConst}
     */
    public static BoolConst make(boolean value) {
        return new BoolConst(value);
    }

    /**
     * @return the value of the boolean constant.
     */
    public boolean isValue() {
        return this.value;
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public String toString() {
        return "BoolConst [" + this.value + "]";
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append(this.value);
    }

}
