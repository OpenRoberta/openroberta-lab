package de.fhg.iais.roberta.syntax.actors.arduino.mbot;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
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
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "IR_SENDER", category = "ACTOR", blocklyNames = {"robCommunication_ir_sendBlock"})
public final class SendIRAction extends Action {
    public final Expr message;

    public SendIRAction(Expr message, BlocklyProperties properties) {
        super(properties);
        this.message = message;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "LedOnAction [ " + this.message + " ]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Phrase message = helper.extractValue(values, new ExprParam(BlocklyConstants.MESSAGE, BlocklyType.STRING));
        return new SendIRAction(Jaxb2Ast.convertPhraseToExpr(message), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.MESSAGE, this.message);
        return jaxbDestination;

    }
}
