package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.TurnDirection;
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
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "TURN_DEGREES", category = "ACTOR", blocklyNames = {"naoActions_turn"})
public final class TurnDegrees extends Action {

    public final TurnDirection turnDirection;
    public final Expr degreesToTurn;

    public TurnDegrees(TurnDirection turnDirection, Expr degreesToTurn, BlocklyProperties properties) {
        super(properties);
        Assert.notNull(turnDirection, "Missing degrees in TurnDegrees block!");
        this.turnDirection = turnDirection;
        this.degreesToTurn = degreesToTurn;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "TurnDegrees [" + this.turnDirection + ", " + this.degreesToTurn + "]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);

        String turnDirection = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION);
        Phrase walkDistance = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));

        return new TurnDegrees(TurnDirection.get(turnDirection), Jaxb2Ast.convertPhraseToExpr(walkDistance), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.turnDirection.toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.POWER, this.degreesToTurn);

        return jaxbDestination;
    }
}
