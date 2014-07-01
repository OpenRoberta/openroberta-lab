package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * null value which can be used in expressions. This class extends {@link Expr}.
 * 
 * @author kcvejoski
 */
public class NullConst extends Expr {

    private NullConst() {
        super(Phrase.Kind.NullConst);
        setReadOnly();
    }

    /**
     * creates instance of {@link NullConst}. This instance is read only and can not be modified.
     * 
     * @return {@link NullConst}
     */
    public static NullConst make() {
        return new NullConst();
    }

    /**
     * @return null value
     */
    public Object getValue() {
        return null;
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
        return "NullConst [null]";
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append("null");
    }

}
