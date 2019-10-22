package de.fhg.iais.roberta.syntax.actors.arduino.sensebox;

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
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

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

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IArduinoVisitor<V>) visitor).visitPlotClearAction(this);
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
        return PlotClearAction.make(factory.sanitizePort(port), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
