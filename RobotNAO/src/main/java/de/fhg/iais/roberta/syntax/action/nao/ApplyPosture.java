package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.Posture;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.dbc.Assert;

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

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);

        String posture = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION);

        return ApplyPosture.make(Posture.get(posture), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.posture.toString());

        return jaxbDestination;
    }
}
