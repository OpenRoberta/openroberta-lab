package de.fhg.iais.roberta.syntax.action.display;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;

/**
 * This class represents the <b>robActions_display_clear</b> block from Blockly into the AST (abstract syntax tree).
 */
public final class ClearDisplayAction<V> extends Action<V> {
    private final String port;

    private ClearDisplayAction(String port, BlocklyBlockProperties properties, BlocklyComment comment) {
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
    public static <V> ClearDisplayAction<V> make(String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ClearDisplayAction<>(port, properties, comment);
    }

    /**
     * factory method: create an AST instance of {@link ClearDisplayAction}.<br>
     * <b>Main use: either testing or textual representation of programs (because in these cases no graphical regeneration is required.</b>
     *
     * @param port
     * @return read only object of class {@link ClearDisplayAction}
     */
    public static <V> ClearDisplayAction<V> make(String port) {
        return new ClearDisplayAction<>(port, BlocklyBlockProperties.make("1", "1"), null);
    }

    @Override
    public String toString() {
        return "ClearDisplayAction []";
    }

    @Override
    protected V accept(IVisitor<V> visitor) {
        return ((IDisplayVisitor<V>) visitor).visitClearDisplayAction(this);
    }

    /**
     * @return port of the display.
     */
    public String getPort() {
        return this.port;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = helper.extractFields(block, (short) 1);
        String port = helper.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.NO_PORT);
        return ClearDisplayAction.make(factory.sanitizePort(port), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
