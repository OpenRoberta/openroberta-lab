package de.fhg.iais.roberta.syntax.actors.arduino.sensebox;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

public class PlotClearAction<V> extends Action<V> {
    private final String port;

    private PlotClearAction(String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PLOT_POINT_ACTION"), properties, comment);
        this.port = port;
        this.setReadOnly();
    }

    /**
     * Creates instance of {@link PlotClearAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link PlotClearAction}
     */
    public static <V> PlotClearAction<V> make(String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PlotClearAction<>(port, properties, comment);
    }

    public String getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "DataSendAction []";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.EMPTY_PORT);
        return PlotClearAction.make(Jaxb2Ast.sanitizePort(port), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, port);
        return jaxbDestination;
    }
}
