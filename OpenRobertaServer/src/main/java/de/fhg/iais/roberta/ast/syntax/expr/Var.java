package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;

/**
 * This class represents the <b>variables_set</b> and <b>variables_get</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for creating a variable.<br/>
 * <br>
 * User must provide name of the variable and type of the variable, if the variable is created before in the code TypeVar should be <b>NONE</b>.
 * To create an instance from this class use the method {@link #make(String, TypeVar)}.<br>
 */
public class Var<V> extends Expr<V> {
    private final TypeVar typeVar;
    private final String name;

    private Var(String value, TypeVar typeVar) {
        super(Phrase.Kind.VAR);
        this.name = value;
        this.typeVar = typeVar;
        setReadOnly();
    }

    /**
     * creates instance of {@link Var}. This instance is read only and can not be modified.
     * 
     * @param value name of the variable,
     * @param typeVar type of the variable,
     * @return read only object of class {@link Var}
     */
    public static <V> Var<V> make(String value, TypeVar typeVar) {
        return new Var<V>(value, typeVar);
    }

    /**
     * @return type of the variable
     */
    public TypeVar getTypeVar() {
        return this.typeVar;
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitVar(this);
    }

    /**
     * Type of variables. Use NONE if the variable is defined already.
     * 
     * @author kcvejoski
     */
    public static enum TypeVar {
        DOUBLE, INTEGER, STRING, NONE;
    }
}
