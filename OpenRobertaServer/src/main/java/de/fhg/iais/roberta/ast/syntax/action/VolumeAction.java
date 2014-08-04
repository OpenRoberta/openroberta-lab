package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_play_setVolume</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code setting or getting the value of the volume.<br/>
 * <br/>
 * The client must provide the {@link Mode} and value of the volume.
 */
public class VolumeAction extends Action {
    private final Mode mode;
    private final Expr volume;

    private VolumeAction(Mode mode, Expr volume) {
        super(Phrase.Kind.VOLUME_ACTION);
        Assert.isTrue(volume != null && volume.isReadOnly() && mode != null);
        this.volume = volume;
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link VolumeAction}. This instance is read only and can not be modified.
     * 
     * @param mode of the action {@link Mode},
     * @param volume value
     * @return read only object of class {@link VolumeAction}.
     */
    public static VolumeAction make(Mode mode, Expr volume) {
        return new VolumeAction(mode, volume);
    }

    /**
     * @return value of the volume
     */
    public Expr getVolume() {
        return this.volume;
    }

    /**
     * @return mode of the action {@link Mode}
     */
    public Mode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return "VolumeAction [" + this.mode + ", " + this.volume + "]";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    /**
     * Type of action we want to do (set or get the volume).
     */
    public static enum Mode {
        SET, GET;
    }

}
