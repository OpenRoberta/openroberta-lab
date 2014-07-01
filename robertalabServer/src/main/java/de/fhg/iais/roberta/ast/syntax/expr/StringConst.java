package de.fhg.iais.roberta.ast.syntax.expr;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * string constant which can be used in expressions. This class extends {@link Expr}.
 * 
 * @author kcvejoski
 */
public class StringConst extends Expr {
    private final String value;

    private StringConst(String value) {
        super(Phrase.Kind.StringConst);
        this.value = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link StringConst}. This instance is read only and can not be modified.
     * 
     * @param value that the boolean constant will have
     * @return {@link StringConst}
     */
    public static StringConst make(String value) {
        return new StringConst(value);
    }

    /**
     * @return the value of the string constant.
     */
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "StringConst [" + this.value + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append("\"").append(StringEscapeUtils.escapeJava(this.value)).append("\"");
    }
}
