package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class allows to create list of {@link Expr} elements.
 * Initially object from this class is writable. After adding all the elements to the list call {@link #setReadOnly()}.
 */
public class ExprList extends Expr {
    private final List<Expr> el = new ArrayList<Expr>();

    private ExprList() {
        super(Phrase.Kind.EXPR_LIST);
    }

    /**
     * @return writable object of type {@link ExprList}.
     */
    public static ExprList make() {
        return new ExprList();
    }

    /**
     * Add new element to the list.
     * 
     * @param expr
     */
    public final void addExpr(Expr expr) {
        Assert.isTrue(mayChange() && expr != null && expr.isReadOnly());
        this.el.add(expr);
    }

    /**
     * @return list with elements of type {@link Expr}.
     */
    public final List<Expr> get() {
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
        for ( Expr expr : this.el ) {
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
    public void generateJava(StringBuilder sb, int indentation) {
        boolean first = true;
        for ( Expr expr : this.el ) {
            if ( first ) {
                first = false;
            } else {
                if ( expr.getKind() == Kind.BINARY ) {
                    sb.append("; ");
                } else {
                    sb.append(", ");
                }
            }
            expr.generateJava(sb, indentation);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
