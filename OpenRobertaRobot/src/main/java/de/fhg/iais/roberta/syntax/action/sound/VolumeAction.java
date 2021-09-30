package de.fhg.iais.roberta.syntax.action.sound;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robActions_play_setVolume</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * setting or getting the value of the volume.<br/>
 * <br/>
 * The client must provide the {@link Mode} and value of the volume.
 */
public class VolumeAction<V> extends Action<V> {
    private final Mode mode;
    private final Expr<V> volume;
    private final String port;
    private final List<Hide> hide;

    private VolumeAction(Mode mode, Expr<V> volume, String port, List<Hide> hide, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("VOLUME_ACTION"), properties, comment);
        Assert.isTrue(volume != null && volume.isReadOnly() && mode != null);
        this.volume = volume;
        this.mode = mode;
        this.port = port;
        this.hide = hide;
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
        return new VolumeAction<>(mode, volume, BlocklyConstants.EMPTY_PORT, null, properties, comment);
    }

    public static <V> VolumeAction<V> make(Mode mode, Expr<V> volume, String port, List<Hide> hide, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new VolumeAction<>(mode, volume, port, hide, properties, comment);
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

    /**
     * @return {@link String} port the used actor is connected to
     */
    public String getPort() {
        return this.port;
    }

    /**
     * @return {@link List<Hide>} List of hidden ports
     */
    public List<Hide> getHide() {
        return this.hide;
    }

    @Override
    public String toString() {
        return "VolumeAction [" + this.mode + ", " + this.volume + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        Phrase<V> expr;
        Mode mode;
        if ( block.getType().equals(BlocklyConstants.ROB_ACTIONS_PLAY_SET_VOLUME) ) {
            List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
            expr = helper.extractValue(values, new ExprParam(BlocklyConstants.VOLUME, BlocklyType.NUMBER_INT));
            mode = VolumeAction.Mode.SET;
        } else {
            expr = NullConst.make(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
            mode = VolumeAction.Mode.GET;
        }

        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        if ( fields.stream().anyMatch(field -> field.getName().equals(BlocklyConstants.ACTORPORT)) ) {
            String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
            return VolumeAction
                .make(mode, Jaxb2Ast.convertPhraseToExpr(expr), port, block.getHide(), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
        }

        return VolumeAction
            .make(mode, Jaxb2Ast.convertPhraseToExpr(expr), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        if ( getMode() == VolumeAction.Mode.SET ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VOLUME, getVolume());
        }
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, port);
        if ( this.hide != null ) {
            jaxbDestination.getHide().addAll(hide);
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
