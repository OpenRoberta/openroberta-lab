package de.fhg.iais.roberta.ast.syntax.stmt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class StmtList extends Stmt {
    private final List<Stmt> sl = new ArrayList<Stmt>();

    private StmtList() {
        super(Phrase.Kind.StmtList);
    }

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
    public void toStringBuilder(StringBuilder sb, int indentation) {
        for ( Stmt stmt : this.sl ) {
            stmt.toStringBuilder(sb, indentation);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringBuilder(sb, 0);
        return sb.toString();
    }
}
