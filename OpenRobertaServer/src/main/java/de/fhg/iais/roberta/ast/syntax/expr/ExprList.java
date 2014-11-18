package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class allows to create list of {@link Expr} elements.
 * Initially object from this class is writable. After adding all the elements to the list call {@link #setReadOnly()}.
 */
public class ExprList<V> extends Expr<V> {
    private final List<Expr<V>> el = new ArrayList<Expr<V>>();

    private ExprList() {
        super(Phrase.Kind.EXPR_LIST, null, null);
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitExprList(this);
    }

    @Override
    public Block astToBlock() {
        // TODO Auto-generated method stub
        return null;
    }
}
