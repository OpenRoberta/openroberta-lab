package de.fhg.iais.roberta.syntax.action.motor.differential;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.MotionParam;
import de.fhg.iais.roberta.util.syntax.MotorDuration;

/**
 * This class represents the <b>robActions_motorDiff_turn</b> and <b>robActions_motorDiff_turn_for</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motors in pilot mode and turn on given direction.<br/>
 * <br/>
 * The client must provide the {@link TurnDirection} and {@link MotionParam} (distance the robot should cover and speed).
 */
@NepoBasic(name = "TURN_ACTION", category = "ACTOR", blocklyNames = {"robActions_motorDiff_turn", "robActions_motorDiff_turn_for"})
public final class TurnAction extends Action {
    public final ITurnDirection direction;
    public final MotionParam param;
    public final String port;
    public final Hide hide;

    public TurnAction(ITurnDirection direction, MotionParam param, String port, Hide hide, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(direction != null && param != null);
        this.direction = direction;
        this.param = param;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "TurnAction [direction=" + this.direction + ", param=" + this.param + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields;
        String mode;
        List<Value> values;
        MotionParam mp;
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        fields = Jaxb2Ast.extractFields(block, (short) 2);
        mode = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION);

        if ( block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_TURN) ) {
            values = Jaxb2Ast.extractValues(block, (short) 1);
            Phrase expr = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));
            mp = new MotionParam.Builder().speed(Jaxb2Ast.convertPhraseToExpr(expr)).build();
        } else {
            values = Jaxb2Ast.extractValues(block, (short) 2);
            Phrase left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));
            Phrase right = helper.extractValue(values, new ExprParam(BlocklyConstants.DEGREE, BlocklyType.NUMBER_INT));
            MotorDuration md = new MotorDuration(factory.getMotorMoveMode("DEGREE"), Jaxb2Ast.convertPhraseToExpr(right));
            mp = new MotionParam.Builder().speed(Jaxb2Ast.convertPhraseToExpr(left)).duration(md).build();
        }

        if ( fields.stream().anyMatch(field -> field.getName().equals(BlocklyConstants.ACTORPORT)) ) {
            String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
            return new TurnAction(factory.getTurnDirection(mode), mp, port, JaxbHelper.getHideFromBlock(block), Jaxb2Ast.extractBlocklyProperties(block));
        }

        return new TurnAction(factory.getTurnDirection(mode), mp, BlocklyConstants.EMPTY_PORT, null, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.direction.toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.POWER, this.param.getSpeed());

        if ( this.param.getDuration() != null ) {
            Ast2Jaxb.addValue(jaxbDestination, this.param.getDuration().getType().toString(), this.param.getDuration().getValue());
        }

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, port);
        if ( this.hide != null ) {
            jaxbDestination.getHide().add(hide);
        }
        return jaxbDestination;
    }
}
