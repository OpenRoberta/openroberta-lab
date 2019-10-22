package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class allows to create list of {@link Stmt} elements. Initially object from this class is writable. After adding all the elements to the list call
 * {@link #setReadOnly()}.
 */
public class StmtList<V> extends Stmt<V> {
    private final List<Stmt<V>> sl = new ArrayList<Stmt<V>>();

    private StmtList() {
        super(BlockTypeContainer.getByName("STMT_LIST"), BlocklyBlockProperties.make("1", "1"), null);
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitStmtList(this);
    }

    @Override
    public Block astToBlock() {
        return null;
    }
}
