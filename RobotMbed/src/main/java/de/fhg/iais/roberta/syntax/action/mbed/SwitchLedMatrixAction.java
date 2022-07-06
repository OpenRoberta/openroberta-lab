package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.SC;

@NepoBasic(name = "SWITCH_LED_MATRIX", category = "ACTOR", blocklyNames = {"mbedActions_switch_led_matrix"})
public final class SwitchLedMatrixAction extends Action {
    public final boolean activated;

    public SwitchLedMatrixAction(BlocklyProperties properties, boolean activated) {
        super(properties);
        this.activated = activated;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "SwitchLedMatrixAction [" + this.activated + "]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);

        boolean activated = Jaxb2Ast.extractField(fields, BlocklyConstants.STATE).equals("ON");
        return new SwitchLedMatrixAction(Jaxb2Ast.extractBlocklyProperties(block), activated);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.STATE, (this.activated) ? SC.ON : SC.OFF);

        return jaxbDestination;
    }
}
