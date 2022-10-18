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

@NepoBasic(name = "DRIVE_ACTION", category = "ACTOR", blocklyNames = {"robActions_motorDiff_on_for", "robActions_motorDiff_on"})
public final class DriveAction extends Action {

    public final IDriveDirection direction;
    public final MotionParam param;
    public final String port;
    public final Hide hide;

    public DriveAction(IDriveDirection direction, MotionParam param, String port, Hide hide, BlocklyProperties properties) {
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
        return "DriveAction [" + this.direction + ", " + this.param + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields;
        String mode;
        List<Value> values;
        MotionParam mp;
        Phrase power;
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        fields = Jaxb2Ast.extractFields(block, (short) 3);
        mode = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION);

        if ( !block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_ON) ) {
            values = Jaxb2Ast.extractValues(block, (short) 2);
            power = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));
            Phrase distance = helper.extractValue(values, new ExprParam(BlocklyConstants.DISTANCE, BlocklyType.NUMBER_INT));
            MotorDuration md = new MotorDuration(factory.getMotorMoveMode("DISTANCE"), Jaxb2Ast.convertPhraseToExpr(distance));
            mp = new MotionParam.Builder().speed(Jaxb2Ast.convertPhraseToExpr(power)).duration(md).build();
        } else {
            values = Jaxb2Ast.extractValues(block, (short) 1);
            power = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));
            mp = new MotionParam.Builder().speed(Jaxb2Ast.convertPhraseToExpr(power)).build();
        }
        if ( fields.stream().anyMatch(field -> field.getName().equals(BlocklyConstants.ACTORPORT)) ) {
            String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
            return new DriveAction(factory.getDriveDirection(mode), mp, port, JaxbHelper.getHideFromBlock(block), Jaxb2Ast.extractBlocklyProperties(block));
        }
        return new DriveAction(factory.getDriveDirection(mode), mp, BlocklyConstants.EMPTY_PORT, null, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        if ( getProperty().getBlockType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_ON_FOR) ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.direction.toString() == "FOREWARD" ? this.direction.toString() : "BACKWARDS");
        } else {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.direction.toString());
        }
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.POWER, this.param.getSpeed());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, port);
        if ( this.hide != null ) {
            jaxbDestination.getHide().add(hide);
        }
        if ( this.param.getDuration() != null ) {
            Ast2Jaxb.addValue(jaxbDestination, this.param.getDuration().getType().toString(), this.param.getDuration().getValue());
        }
        return jaxbDestination;
    }
}
