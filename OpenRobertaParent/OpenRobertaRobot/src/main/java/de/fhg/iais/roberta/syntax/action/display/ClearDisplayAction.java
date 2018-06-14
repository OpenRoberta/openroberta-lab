package de.fhg.iais.roberta.syntax.action.display;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorDisplayVisitor;

/**
 * This class represents the <b>robActions_display_clear</b> block from Blockly into the AST (abstract syntax tree).
 */
public final class ClearDisplayAction<V> extends Action<V> {
    private final IActorPort port;

    private ClearDisplayAction(IActorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("CLEAR_DISPLAY_ACTION"), properties, comment);
        this.port = port;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ClearDisplayAction}. This instance is read only and can not be modified.
     *
     * @param port
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ClearDisplayAction}
     */
    public static <V> ClearDisplayAction<V> make(IActorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ClearDisplayAction<>(port, properties, comment);
    }

    @Override
    public String toString() {
        return "ClearDisplayAction []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstActorDisplayVisitor<V>) visitor).visitClearDisplayAction(this);
    }

    /**
     * @return port of the display.
     */
    public IActorPort getPort() {
        return this.port;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        IRobotFactory factory = helper.getModeFactory();
        List<Field> fields = helper.extractFields(block, (short) 1);
        String port = helper.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.NO_PORT);
        return ClearDisplayAction.make(factory.getActorPort(port), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
