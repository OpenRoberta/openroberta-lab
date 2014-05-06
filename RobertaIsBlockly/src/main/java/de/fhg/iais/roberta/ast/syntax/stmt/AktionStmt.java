package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.aktion.Aktion;
import de.fhg.iais.roberta.dbc.Assert;

public class AktionStmt extends Stmt {
    private final Aktion akt;

    private AktionStmt(Aktion akt) {
        Assert.isTrue(akt.isReadOnly());
        this.akt = akt;
        setReadOnly();
    }

    public AktionStmt make(Aktion akt) {
        return new AktionStmt(akt);
    }

    public Aktion getAkt() {
        return this.akt;
    }

    @Override
    public Kind getKind() {
        return Kind.AktionStmt;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "AktionStmt [" + this.akt + "]";
    }

}
