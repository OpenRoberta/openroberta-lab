package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.action.nao.Modus;
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
import de.fhg.iais.roberta.visitor.nao.NaoAstVisitor;

/**
 * This class represents the <b>naoActions_PartialStiffnessOn</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for removing the stiffness from one part of the robots body.<br/>
 * <br/>
 * The client must provide the {@link TurnDirection} (body part in which the motors are released).
 */
public final class Hand<V> extends Action<V> {

    private final TurnDirection turnDirection;
    private final Modus modus;

    private Hand(TurnDirection turnDirection, Modus modus, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("HAND"), properties, comment);
        Assert.notNull(turnDirection, "Missing turn direction in Hand block!");
        Assert.notNull(modus, "Missing modus in Hand block!");
        this.turnDirection = turnDirection;
        this.modus = modus;
        setReadOnly();
    }

    /**
     * Creates instance of {@link Hand}. This instance is read only and can not be modified.
     *
     * @param part {@link TurnDirection} on which the stiffness is turned off,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Hand}
     */
    private static <V> Hand<V> make(TurnDirection turnDirection, Modus modus, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Hand<V>(turnDirection, modus, properties, comment);
    }

    public TurnDirection getTurnDirection() {
        return this.turnDirection;
    }

    public Modus getModus() {
        return this.modus;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitHand(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);

        String turnDirection = helper.extractField(fields, BlocklyConstants.SIDE);
        String modus = helper.extractField(fields, BlocklyConstants.MODE);

        return Hand.make(TurnDirection.get(turnDirection), Modus.get(modus), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public String toString() {
        return "Hand [" + this.turnDirection + ", " + this.modus + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SIDE, this.turnDirection.toString());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE, this.modus.getValues()[0]);

        return jaxbDestination;
    }
}