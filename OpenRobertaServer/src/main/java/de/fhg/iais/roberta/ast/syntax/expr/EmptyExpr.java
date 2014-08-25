package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * {@link EmptyExpr} is used when in binary or unary expressions, expression is missing.
 * When create instance from this class we pass as parameter the type of the value should have the missing expression.
 */
public class EmptyExpr extends Expr {

    private final Class<?> defVal;

    private EmptyExpr(Class<?> defVal) {
        super(Phrase.Kind.EMPTY_EXPR);
        Assert.isTrue(defVal != null);
        this.defVal = defVal;
        setReadOnly();
    }

    /**
     * create read only instance from {@link EmptyExpr}.
     * 
     * @param defVal type of the value that the missing expression should have.
     * @return read only object of class {@link EmptyExpr}.
     */
    public static EmptyExpr make(Class<?> defVal) {
        return new EmptyExpr(defVal);
    }

    /**
     * @return type of the value that the missing expression should have.
     */
    public Class<?> getDefVal() {
        return this.defVal;
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
        return "EmptyExpr [defVal=" + this.defVal + "]";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitEmptyExpr(this);
    }

}
