package de.fhg.iais.roberta.syntax.actors.arduino.sensebox;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "PLOT_POINT_ACTION", category = "ACTOR", blocklyNames = {"robactions_plot_point"})
public final class PlotPointAction extends Action {
    public final String port;
    public final Expr value;
    public final Expr tickmark;

    public PlotPointAction(String port, Expr value, Expr tickmark, BlocklyProperties properties) {
        super(properties);
        this.port = port;
        this.value = value;
        this.tickmark = tickmark;
        this.setReadOnly();
    }

    @Override
    public String toString() {
        return "PlotPointAction []";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.EMPTY_PORT);
        Phrase value = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NUMBER_INT));
        Phrase tickmark = helper.extractValue(values, new ExprParam(BlocklyConstants.TICKMARK, BlocklyType.NUMBER_INT));
        return new PlotPointAction(Jaxb2Ast.sanitizePort(port), Jaxb2Ast.convertPhraseToExpr(value), Jaxb2Ast.convertPhraseToExpr(tickmark), Jaxb2Ast.extractBlocklyProperties(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, port);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.value);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.TICKMARK, this.tickmark);
        return jaxbDestination;
    }
}
