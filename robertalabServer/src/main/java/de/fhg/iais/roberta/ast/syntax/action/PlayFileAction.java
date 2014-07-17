package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class PlayFileAction extends Action {
    private final String filename;

    private PlayFileAction(String filename) {
        super(Phrase.Kind.PlayFileAction);
        Assert.isTrue(filename != "");
        this.filename = filename;
        setReadOnly();
    }

    public static PlayFileAction make(String filename) {
        return new PlayFileAction(filename);
    }

    public String getFilename() {
        return this.filename;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "PlayFileAction [" + this.filename + "]";
    }

}
