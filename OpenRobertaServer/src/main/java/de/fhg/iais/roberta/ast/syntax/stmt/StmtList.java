package de.fhg.iais.roberta.ast.syntax.stmt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class allows to create list of {@link Stmt} elements.
 * Initially object from this class is writable. After adding all the elements to the list call {@link #setReadOnly()}.
 */
public class StmtList<V> extends Stmt<V> {
    private final List<Stmt<V>> sl = new ArrayList<Stmt<V>>();

    private StmtList() {
        super(Phrase.Kind.STMT_LIST);
    }

    /**
     * @return writable object of type {@link StmtList}.
     */
    public static <V> StmtList<V> make() {
        return new StmtList<V>();
    }

    /**
     * Add new element to the list.
     * 
     * @param stmt
     */
    public final void addStmt(Stmt<V> stmt) {
        Assert.isTrue(mayChange() && stmt != null && stmt.isReadOnly());
        this.sl.add(stmt);
    }

    /**
     * @return list with elements of type {@link Stmt}.
     */
    public final List<Stmt<V>> get() {
        Assert.isTrue(isReadOnly());
        return Collections.unmodifiableList(this.sl);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for ( Stmt<V> stmt : this.sl ) {
            sb.append(stmt.toString());
        }
        return sb.toString();
    }

    @Override
    protected V accept(Visitor<V> visitor) {
        return visitor.visitStmtList(this);
    }
}
