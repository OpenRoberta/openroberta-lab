package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * {@link EmptyExpr} is used when in binary or unary expressions, expression is missing. When create instance from this class we pass as parameter the type of
 * the value should have the missing expression.
 */
public class EmptyExpr<V> extends Expr<V> {

    private final BlocklyType defVal;

    private EmptyExpr(BlocklyType defVal) {
        super(BlockTypeContainer.getByName("EMPTY_EXPR"), BlocklyBlockProperties.make("1", "1"), null);
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
    public static <V> EmptyExpr<V> make(BlocklyType defVal) {
        return new EmptyExpr<V>(defVal);
    }

    /**
     * @return type of the value that the missing expression should have.
     */
    public BlocklyType getDefVal() {
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
    public BlocklyType getVarType() {
        return BlocklyType.NOTHING;
    }

    @Override
    public String toString() {
        return "EmptyExpr [defVal=" + this.defVal + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitEmptyExpr(this);
    }

    @Override
    public Block astToBlock() {
        return null;
    }

}
