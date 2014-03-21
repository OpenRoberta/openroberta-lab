package de.fhg.iais.roberta.ast.syntax.stmt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class StmtList extends Phrase {
    private List<Stmt> sl = new ArrayList<Stmt>();

    public static StmtList make() {
        return new StmtList();
    }

    public final void addStmt(Stmt stmt) {
        Assert.isTrue(mayChange() && stmt != null && stmt.isReadOnly());
        this.sl.add(stmt);
    }

    public final List<Stmt> get() {
        Assert.isTrue(isReadOnly());
        return Collections.unmodifiableList(this.sl);
    }

    @Override
    protected final void freeze() {
        Assert.isTrue(isReadOnly());
        this.sl = Collections.unmodifiableList(this.sl); // check whether the wrapper is called twice???
    }
}
