package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.Modus;
import de.fhg.iais.roberta.mode.action.nao.Posture;
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
 * This class represents the <b>naoActions_applyPosture</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for applying a posture<br/>
 * <br/>
 * The client must provide the {@link Posture} (name of posture).
 */
public final class SetMode<V> extends Action<V> {

    private final Modus modus;

    private SetMode(Modus modus, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SET_MODE"), properties, comment);
        Assert.notNull(modus, "Missing modus in Mode block!");
        this.modus = modus;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SetMode}. This instance is read only and can not be modified.
     *
     * @param port {@link Posture} which will be applied,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SetMode}
     */
    private static <V> SetMode<V> make(Modus modus, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SetMode<V>(modus, properties, comment);
    }

    public Modus getModus() {
        return this.modus;
    }

    @Override
    public String toString() {
        return "Mode [" + this.modus + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitSetMode(this);
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

        String modus = helper.extractField(fields, BlocklyConstants.DIRECTION);

        return SetMode.make(Modus.get(modus), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.modus.toString());

        return jaxbDestination;
    }
}
