package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;

/**
 * This class represents the <b>text</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for string constant.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(String)}.<br>
 */
public class StringConst extends Expr {
    private final String value;

    private StringConst(String value) {
        super(Phrase.Kind.STRING_CONST);
        this.value = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link StringConst}. This instance is read only and can not be modified.
     * 
     * @param value that the boolean constant will have
     * @return read only object of class {@link StringConst}.
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
    public void accept(Visitor visitor) {
        visitor.visitStringConst(this);
    }
}
