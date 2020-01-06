package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.Posture;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.INaoVisitor;

/**
 * This class represents the <b>naoActions_applyPosture</b> block from Blockly into the AST (abstract syntax tree). Objects from this class will generate code
 * for applying a posture<br/>
 * <br/>
 * The client must provide the {@link Posture} (name of posture).
 */
public final class ApplyPosture<V> extends Action<V> {

    private final Posture posture;

    private ApplyPosture(Posture posture, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("APPLY_POSTURE"), properties, comment);
        Assert.notNull(posture, "Missing posture in ApplyPosture block!");
        this.posture = posture;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ApplyPosture}. This instance is read only and can not be modified.
     *
     * @param port {@link Posture} which will be applied,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ApplyPosture}
     */
    private static <V> ApplyPosture<V> make(Posture posture, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ApplyPosture<V>(posture, properties, comment);
    }

    public Posture getPosture() {
        return this.posture;
    }

    @Override
    public String toString() {
        return "ApplyPosture [" + this.posture + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((INaoVisitor<V>) visitor).visitApplyPosture(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);

        String posture = helper.extractField(fields, BlocklyConstants.DIRECTION);

        return ApplyPosture.make(Posture.get(posture), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.posture.toString());

        return jaxbDestination;
    }
}
