package de.fhg.iais.roberta.syntax.action.motor;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.IMotorMoveMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.MotionParam;
import de.fhg.iais.roberta.util.syntax.MotorDuration;

@NepoBasic(name = "MOTOR_ON_ACTION", category = "ACTOR", blocklyNames = {"sim_motor_on_for", "robActions_motor_on_for_ardu", "robActions_motor_on", "sim_motor_on", "robActions_motor_on_for", "mbedActions_motor_on"})
public final class MotorOnAction extends MoveAction {

    public final MotionParam param;

    public MotorOnAction(String port, MotionParam param, BlocklyProperties properties) {
        super(properties, port);
        Assert.isTrue((param != null) && (port != null));
        this.param = param;

        setReadOnly();
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        String port;
        List<Field> fields;
        List<Value> values;
        MotionParam mp;
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        if ( block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_ON)
            || block.getType().equals(BlocklyConstants.SIM_MOTOR_ON)
            || block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_ON_FOR_ARDU)
            || block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_ON_FOR_MBED) ) {
            fields = Jaxb2Ast.extractFields(block, (short) 1);
            values = Jaxb2Ast.extractValues(block, (short) 1);
            port = Jaxb2Ast.extractField(fields, BlocklyConstants.MOTORPORT);
            Phrase expr = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));
            mp = new MotionParam.Builder().speed(Jaxb2Ast.convertPhraseToExpr(expr)).build();
        } else {
            fields = Jaxb2Ast.extractFields(block, (short) 2);
            values = Jaxb2Ast.extractValues(block, (short) 2);
            port = Jaxb2Ast.extractField(fields, BlocklyConstants.MOTORPORT);
            Phrase left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));
            Phrase right = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NUMBER_INT));
            MotorDuration md;
            if ( fields.size() == 1 ) {
                md = new MotorDuration(null, Jaxb2Ast.convertPhraseToExpr(right));
            } else {
                String mode = Jaxb2Ast.extractField(fields, BlocklyConstants.MOTORROTATION);
                md = new MotorDuration(factory.getMotorMoveMode(mode), Jaxb2Ast.convertPhraseToExpr(right));
            }
            mp = new MotionParam.Builder().speed(Jaxb2Ast.convertPhraseToExpr(left)).duration(md).build();
        }
        return new MotorOnAction(port, mp, Jaxb2Ast.extractBlocklyProperties(block));
    }

    /**
     * @return duration type of motor movement
     */
    public IMotorMoveMode getDurationMode() {
        return this.param.getDuration().getType();
    }

    /**
     * @return value of the duration of the motor movement
     */
    public Expr getDurationValue() {
        MotorDuration duration = this.param.getDuration();
        if ( duration != null ) {
            return duration.getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        return "MotorOnAction [" + getUserDefinedPort() + ", " + this.param + "]";
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MOTORPORT, getUserDefinedPort().toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.POWER, this.param.getSpeed());
        if ( this.param.getDuration() != null ) {
            if ( getDurationMode() != null ) {
                Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MOTORROTATION, getDurationMode().toString());
            }
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, getDurationValue());
        }

        return jaxbDestination;
    }
}
