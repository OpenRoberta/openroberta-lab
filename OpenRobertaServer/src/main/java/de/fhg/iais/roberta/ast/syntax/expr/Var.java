package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;

/**
 * This class represents the <b>variables_set</b> and <b>variables_get</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for creating a variable.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(String)}.<br>
 */
public class Var extends Expr {
    private final String name;

    private Var(String value) {
        super(Phrase.Kind.VAR);
        this.name = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link Var}. This instance is read only and can not be modified.
     * 
     * @param value name of the variable,
     * @return read only object of class {@link Var}
     */
    public static Var make(String value) {
        return new Var(value);
    }

    /**
     * @return name of the variable
     */
    public String getValue() {
        return this.name;
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
        return "Var [" + this.name + "]";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
