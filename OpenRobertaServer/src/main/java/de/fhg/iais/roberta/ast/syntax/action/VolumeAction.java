package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_play_setVolume</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code setting or getting the value of the volume.<br/>
 * <br/>
 * The client must provide the {@link Mode} and value of the volume.
 */
public class VolumeAction<V> extends Action<V> {
    private final Mode mode;
    private final Expr<V> volume;

    private VolumeAction(Mode mode, Expr<V> volume, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.VOLUME_ACTION, properties, comment);
        Assert.isTrue(volume != null && volume.isReadOnly() && mode != null);
        this.volume = volume;
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link VolumeAction}. This instance is read only and can not be modified.
     * 
     * @param mode of the action {@link Mode},
     * @param volume value,
     * @param properties of the block (see {@link BlocklyBlockProperties}),,
     * @param comment added from the user,
     * @return read only object of class {@link VolumeAction}.
     */
    public static <V> VolumeAction<V> make(Mode mode, Expr<V> volume, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new VolumeAction<V>(mode, volume, properties, comment);
    }

    /**
     * @return value of the volume
     */
    public Expr<V> getVolume() {
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitVolumeAction(this);
    }

    /**
     * Type of action we want to do (set or get the volume).
     */
    public static enum Mode {
        SET, GET;
    }

}
