package de.fhg.iais.roberta.syntax.actors.arduino.sensebox;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;

public class PlotPointAction<V> extends Action<V> {
    private final String port;
    private final Expr<V> value;
    private final Expr<V> tickmark;

    private PlotPointAction(String port, Expr<V> value, Expr<V> tickmark, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PLOT_POINT_ACTION"), properties, comment);
        this.port = port;
        this.value = value;
        this.tickmark = tickmark;
        this.setReadOnly();
    }

    /**
     * Creates instance of {@link PlotPointAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link PlotPointAction}
     */
    public static <V> PlotPointAction<V> make(String port, Expr<V> value, Expr<V> tickmark, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PlotPointAction<>(port, value, tickmark, properties, comment);
    }

    public String getPort() {
        return this.port;
    }

    public Expr<V> getValue() {
        return this.value;
    }

    public Expr<V> getTickmark() {
        return this.tickmark;
    }

    @Override
    public String toString() {
        return "PlotPointAction []";
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
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.EMPTY_PORT);
        Phrase<V> value = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NUMBER_INT));
        Phrase<V> tickmark = helper.extractValue(values, new ExprParam(BlocklyConstants.TICKMARK, BlocklyType.NUMBER_INT));
        return PlotPointAction
            .make(
                Jaxb2Ast.sanitizePort(port),
                Jaxb2Ast.convertPhraseToExpr(value),
                Jaxb2Ast.convertPhraseToExpr(tickmark),
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, port);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, getValue());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.TICKMARK, getTickmark());
        return jaxbDestination;
    }
}
