package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "MOTIONKIT_DUAL_SET_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_motionkit_dual_set"})
public final class MotionKitDualSetAction<V> extends Action<V> {
    public final String directionLeft;
    public final String directionRight;

    public MotionKitDualSetAction(String directionLeft, String directionRight, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        Assert.notNull(directionLeft);
        Assert.notNull(directionRight);
        this.directionLeft = directionLeft;
        this.directionRight = directionRight;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "MotionKitDualSetAction [" + this.directionLeft + ", " + this.directionRight + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String directionL = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION_LEFT);
        String directionR = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION_RIGHT);
        return new MotionKitDualSetAction<>(factory.getMode(directionL), factory.getMode(directionR), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION_LEFT, this.directionLeft);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION_RIGHT, this.directionRight);
        return jaxbDestination;
    }
}
