package de.fhg.iais.roberta.ast.syntax.expr;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * This class represents the <b>robColour_picker</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate color constant.<br/>
 * <br>
 * The client must provide the value of the color. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(String)}.<br>
 */
public class ColorConst extends Expr {
    private final String value;

    private ColorConst(String value) {
        super(Phrase.Kind.ColorConst);
        this.value = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link ColorConst}. This instance is read only and can not be modified.
     * 
     * @param value that the color constant will have
     * @return read only object of class {@link ColorConst}.
     */
    public static ColorConst make(String value) {
        return new ColorConst(value);
    }

    /**
     * @return the value of the string constant.
     */
    public String getValue() {
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
        return "StringConst [" + this.value + "]";
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append("\"").append(StringEscapeUtils.escapeJava(this.value)).append("\"");
    }
}
