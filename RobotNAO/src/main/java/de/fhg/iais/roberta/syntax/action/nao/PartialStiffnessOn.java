package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.BodyPart;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

/**
 * This class represents the <b>naoActions_PartialStiffnessOn</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for removing the stiffness from one part of the robots body.<br/>
 * <br/>
 * The client must provide the {@link BodyPart} (body part in which the motors are released).
 */
public final class PartialStiffnessOn<V> extends Action<V> {

    private final BodyPart bodyPart;

    private PartialStiffnessOn(BodyPart bodyPart, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PARTIAL_STIFFNESS_ON"), properties, comment);
        Assert.notNull(bodyPart, "Missing body part in PartialStiffnessOn block!");
        this.bodyPart = bodyPart;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "PartialStiffnessOn [" + this.bodyPart + "]";
    }

    /**
     * Creates instance of {@link PartialStiffnessOn}. This instance is read only and can not be modified.
     *
     * @param part {@link BodyPart} on which the stiffness is turned off,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link PartialStiffnessOn}
     */
    private static <V> PartialStiffnessOn<V> make(BodyPart bodyPart, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PartialStiffnessOn<V>(bodyPart, properties, comment);
    }

    public BodyPart getBodyPart() {
        return this.bodyPart;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitPartialStiffnessOn(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);

        String bodyPart = helper.extractField(fields, BlocklyConstants.DIRECTION);

        return PartialStiffnessOn.make(BodyPart.get(bodyPart), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.bodyPart.toString());

        return jaxbDestination;
    }
}
