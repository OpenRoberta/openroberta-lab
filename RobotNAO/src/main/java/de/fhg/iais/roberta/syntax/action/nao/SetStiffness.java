package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.BodyPart;
import de.fhg.iais.roberta.mode.general.WorkingState;
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
 * This class represents the <b>naoActions_PartialStiffnessOn</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for removing the stiffness from one part of the robots body.<br/>
 * <br/>
 * The client must provide the {@link BodyPart} (body part in which the motors are released).
 */
public final class SetStiffness<V> extends Action<V> {

    private final BodyPart bodyPart;
    private final WorkingState onOff;

    private SetStiffness(BodyPart bodyPart, WorkingState onOff, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SET_STIFFNESS"), properties, comment);
        Assert.notNull(bodyPart, "Missing body part in SetStiffness block!");
        Assert.notNull(onOff, "Missing onOff in SetStiffness block!");
        this.bodyPart = bodyPart;
        this.onOff = onOff;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SetStiffness}. This instance is read only and can not be modified.
     *
     * @param part {@link BodyPart} on which the stiffness is turned off,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SetStiffness}
     */
    private static <V> SetStiffness<V> make(BodyPart bodyPart, WorkingState onOff, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SetStiffness<V>(bodyPart, onOff, properties, comment);
    }

    public BodyPart getBodyPart() {
        return this.bodyPart;
    }

    public WorkingState getOnOff() {
        return this.onOff;
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((INaoVisitor<V>) visitor).visitSetStiffness(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);

        String bodyPart = helper.extractField(fields, BlocklyConstants.PART);
        String onOff = helper.extractField(fields, BlocklyConstants.MODE);

        return SetStiffness.make(BodyPart.get(bodyPart), WorkingState.get(onOff), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public String toString() {
        return "SetStiffness [" + this.bodyPart + ", " + this.onOff + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.PART, this.bodyPart.toString());
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MODE, this.onOff.toString());

        return jaxbDestination;
    }
}