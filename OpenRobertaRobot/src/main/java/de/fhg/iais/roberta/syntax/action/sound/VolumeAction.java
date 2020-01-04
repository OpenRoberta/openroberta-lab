package de.fhg.iais.roberta.syntax.action.sound;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;

/**
 * This class represents the <b>robActions_play_setVolume</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * setting or getting the value of the volume.<br/>
 * <br/>
 * The client must provide the {@link Mode} and value of the volume.
 */
public class VolumeAction<V> extends Action<V> {
    private final Mode mode;
    private final Expr<V> volume;

    private VolumeAction(Mode mode, Expr<V> volume, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("VOLUME_ACTION"), properties, comment);
        Assert.isTrue(volume != null && volume.isReadOnly() && mode != null);
        this.volume = clampValue(volume, 0, 100);
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link VolumeAction}. This instance is read only and can not be modified.
     *
     * @param mode of the action {@link Mode} must be <b>not</b> null,
     * @param volume value must be <b>not</b> null and <b>read only</b>,
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ISoundVisitor<V>) visitor).visitVolumeAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        if ( block.getType().equals(BlocklyConstants.ROB_ACTIONS_PLAY_SET_VOLUME) ) {
            List<Value> values = helper.extractValues(block, (short) 1);
            Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.VOLUME, BlocklyType.NUMBER_INT));
            return VolumeAction
                .make(VolumeAction.Mode.SET, helper.convertPhraseToExpr(expr), helper.extractBlockProperties(block), helper.extractComment(block));
        }
        NullConst<V> expr = NullConst.make(helper.extractBlockProperties(block), helper.extractComment(block));
        return VolumeAction.make(VolumeAction.Mode.GET, helper.convertPhraseToExpr(expr), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        if ( getMode() == VolumeAction.Mode.SET ) {
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.VOLUME, getVolume());
        }

        return jaxbDestination;
    }

    /**
     * Type of action we want to do (set or get the volume).
     */
    public static enum Mode {
        SET, GET;
    }
}
