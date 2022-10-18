package de.fhg.iais.roberta.syntax.action.motor.differential;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
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

@NepoBasic(name = "CURVE_ACTION", category = "ACTOR", blocklyNames = {"robActions_motorDiff_curve", "robActions_motorDiff_curve_for"})
public final class CurveAction extends Action {

    public final IDriveDirection direction;
    public final MotionParam paramLeft;
    public final MotionParam paramRight;
    public final String port;
    public final Hide hide;

    public CurveAction(
        IDriveDirection direction,
        MotionParam paramLeft,
        MotionParam paramRight,
        String port,
        Hide hide,
        BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(direction != null && paramLeft != null && paramRight != null);
        this.direction = direction;
        this.paramLeft = paramLeft;
        this.paramRight = paramRight;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "CurveAction [" + this.direction + ", " + this.paramLeft + this.paramRight + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields;
        String mode;
        List<Value> values;
        MotionParam mpLeft;
        MotionParam mpRight;
        Phrase left;
        Phrase right;
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        fields = Jaxb2Ast.extractFields(block, (short) 2);
        mode = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION);

        if ( !block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_CURVE) ) {
            values = Jaxb2Ast.extractValues(block, (short) 3);
            Phrase dist = helper.extractValue(values, new ExprParam(BlocklyConstants.DISTANCE, BlocklyType.NUMBER_INT));
            MotorDuration md = new MotorDuration(factory.getMotorMoveMode("DISTANCE"), Jaxb2Ast.convertPhraseToExpr(dist));
            left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_LEFT, BlocklyType.NUMBER_INT));
            right = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_RIGHT, BlocklyType.NUMBER_INT));
            mpLeft = new MotionParam.Builder().speed(Jaxb2Ast.convertPhraseToExpr(left)).duration(md).build();
            mpRight = new MotionParam.Builder().speed(Jaxb2Ast.convertPhraseToExpr(right)).duration(md).build();
        } else {
            values = Jaxb2Ast.extractValues(block, (short) 2);
            left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_LEFT, BlocklyType.NUMBER_INT));
            right = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_RIGHT, BlocklyType.NUMBER_INT));
            mpLeft = new MotionParam.Builder().speed(Jaxb2Ast.convertPhraseToExpr(left)).build();
            mpRight = new MotionParam.Builder().speed(Jaxb2Ast.convertPhraseToExpr(right)).build();
        }

        if ( fields.stream().anyMatch(field -> field.getName().equals(BlocklyConstants.ACTORPORT)) ) {
            String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
            return new CurveAction(factory.getDriveDirection(mode), mpLeft, mpRight, port, JaxbHelper.getHideFromBlock(block), Jaxb2Ast.extractBlocklyProperties(block));
        }

        return new CurveAction(factory.getDriveDirection(mode), mpLeft, mpRight, BlocklyConstants.EMPTY_PORT, null, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        if ( getProperty().getBlockType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_CURVE_FOR) ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.direction.toString() == "FOREWARD" ? this.direction.toString() : "BACKWARDS");
        } else {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.direction.toString());
        }
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.POWER_LEFT, this.paramLeft.getSpeed());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.POWER_RIGHT, this.paramRight.getSpeed());

        if ( this.paramLeft.getDuration() != null ) {
            Ast2Jaxb.addValue(jaxbDestination, this.paramLeft.getDuration().getType().toString(), this.paramLeft.getDuration().getValue());
        }
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, port);
        if ( this.hide != null ) {
            jaxbDestination.getHide().add(hide);
        }
        return jaxbDestination;
    }
}
