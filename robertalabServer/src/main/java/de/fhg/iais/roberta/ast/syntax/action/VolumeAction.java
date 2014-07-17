package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;

public class VolumeAction extends Action {
    private final Mode mode;
    private final Expr volume;

    private VolumeAction(Mode mode, Expr volume) {
        super(Phrase.Kind.VolumeAction);
        Assert.isTrue(volume != null && volume.isReadOnly() && mode != null);
        this.volume = volume;
        this.mode = mode;
        setReadOnly();
    }

    public static VolumeAction make(Mode mode, Expr volume) {
        return new VolumeAction(mode, volume);
    }

    public Expr getVolume() {
        return this.volume;
    }

    public Mode getMode() {
        return this.mode;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "VolumeAction [" + this.mode + ", " + this.volume + "]";
    }

    public static enum Mode {
        SET, GET;
    }

}
