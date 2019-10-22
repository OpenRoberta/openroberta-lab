package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class allows to create list of {@link Expr} elements. Initially object from this class is writable. After adding all the elements to the list call
 * {@link #setReadOnly()}.
 */
public class ExprList<V> extends Expr<V> {
    private final List<Expr<V>> el = new ArrayList<Expr<V>>();

    private ExprList() {
        super(BlockTypeContainer.getByName("EXPR_LIST"), BlocklyBlockProperties.make("1", "1", false, false, false, false, false, null, false, false), null);
    }

    /**
     * @return writable object of type {@link ExprList}.
     */
    public static <V> ExprList<V> make() {
        return new ExprList<V>();
    }

    /**
     * Add new element to the list.
     *
     * @param expr
     */
    public final void addExpr(Expr<V> expr) {
        Assert.isTrue(mayChange() && expr != null && expr.isReadOnly());
        this.el.add(expr);
    }

    /**
     * @return list with elements of type {@link Expr}.
     */
    public final List<Expr<V>> get() {
        Assert.isTrue(isReadOnly());
        return Collections.unmodifiableList(this.el);
    }

    @Override
    public int getPrecedence() {
        throw new DbcException("not supported");
    }

    @Override
    public Assoc getAssoc() {
        throw new DbcException("not supported");
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.NOTHING;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for ( Expr<?> expr : this.el ) {
            if ( first ) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(expr.toString());
        }
        return sb.toString();
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitExprList(this);
    }

    @Override
    public Block astToBlock() {
        return null;
    }

}
