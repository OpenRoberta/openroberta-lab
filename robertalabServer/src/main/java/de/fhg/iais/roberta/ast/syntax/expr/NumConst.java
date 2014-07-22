package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * This class represents the <b>math_number</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code numerical value.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(String)}.<br>
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
     * @return read only object of class {@link NumConst}.
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
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public String toString() {
        return "NumConst [" + this.value + "]";
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append(this.value);
    }
}
