package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.aktion.Aktion;
import de.fhg.iais.roberta.dbc.Assert;

public class AktionStmt extends Stmt {
    private final Aktion akt;

    private AktionStmt(Aktion akt) {
        super(Phrase.Kind.AktionStmt);
        Assert.isTrue(akt.isReadOnly());
        this.akt = akt;
        setReadOnly();
    }

    public static AktionStmt make(Aktion akt) {
        return new AktionStmt(akt);
    }

    public Aktion getAkt() {
        return this.akt;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
    }

    @Override
    public String toString() {
        return "AktionStmt [" + this.akt + "]";
    }

}
