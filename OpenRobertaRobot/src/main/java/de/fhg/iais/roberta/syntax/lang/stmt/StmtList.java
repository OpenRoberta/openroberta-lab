package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class allows to create list of {@link Stmt} elements. Initially object from this class is writable. After adding all the elements to the list call
 * {@link #setReadOnly()}.
 */
@NepoBasic(name = "STMT_LIST", category = "STMT", blocklyNames = {})
public final class StmtList extends Stmt {
    public final List<Stmt> sl = new ArrayList<Stmt>();

    public StmtList() {
        super(BlocklyProperties.make("STMT_LIST", "1"));
    }

    /**
     * Add new element to the list.
     *
     * @param stmt
     */
    public final void addStmt(Stmt stmt) {
        Assert.isTrue(mayChange() && stmt != null && stmt.isReadOnly());
        this.sl.add(stmt);
    }

    /**
     * @return list with elements of type {@link Stmt}.
     */
    public final List<Stmt> get() {
        Assert.isTrue(isReadOnly());
        return Collections.unmodifiableList(this.sl);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for ( Stmt stmt : this.sl ) {
            sb.append(stmt.toString());
        }
        return sb.toString();
    }

    @Override
    public Block ast2xml() {
        return null;
    }
}
