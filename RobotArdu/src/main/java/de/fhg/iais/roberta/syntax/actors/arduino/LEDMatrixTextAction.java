package de.fhg.iais.roberta.syntax.actors.arduino;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "LED_MATRIX_TEXT_ACTION", category = "ACTOR", blocklyNames = {"mBotActions_display_text"})
public final class LEDMatrixTextAction extends Action {
    public final String port;
    public final Expr msg;
    public final String displayMode;

    public LEDMatrixTextAction(String port, String displayMode, Expr msg, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(msg != null && port != null);
        this.port = port;
        this.msg = msg;
        this.displayMode = displayMode;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "DisplayTextAction [" + this.msg + "]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        final String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
        Phrase msg = helper.extractValue(values, new ExprParam(BlocklyConstants.OUT, BlocklyType.STRING));
        String displayMode = "";
        try {
            displayMode = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
        } catch ( DbcException e ) {
            displayMode = "TEXT";
        }
        return new LEDMatrixTextAction(port, displayMode, Jaxb2Ast.convertPhraseToExpr(msg), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, this.displayMode);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, this.port);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.OUT, this.msg);

        return jaxbDestination;
    }

}
