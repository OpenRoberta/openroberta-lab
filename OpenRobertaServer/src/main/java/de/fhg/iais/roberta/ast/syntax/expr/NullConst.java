package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;

/**
 * This class represents the <b>logic_null</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for creating <b>null</b>.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make()}.<br>
 */
public class NullConst<V> extends Expr<V> {

    private NullConst() {
        super(Phrase.Kind.NULL_CONST);
        setReadOnly();
    }

    /**
     * creates instance of {@link NullConst}. This instance is read only and can not be modified.
     * 
     * @return read only object of class {@link NullConst}.
     */
    public static <V> NullConst<V> make() {
        return new NullConst<V>();
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitNullConst(this);
    }

}
