package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.PickColor;
import de.fhg.iais.roberta.codegen.lejos.Visitor;

/**
 * This class represents the <b>robColour_picker</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate color constant.<br/>
 * <br>
 * The client must provide the value of the color. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(String)}.<br>
 */
public class ColorConst extends Expr {
    private final PickColor value;

    private ColorConst(String value) {
        super(Phrase.Kind.PICK_COLOR_CONST);
        this.value = PickColor.get(value);
        setReadOnly();
    }

    /**
     * creates instance of {@link ColorConst}. This instance is read only and cannot be modified.
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
    public PickColor getValue() {
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
        return "ColorConst [" + this.value + "]";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
