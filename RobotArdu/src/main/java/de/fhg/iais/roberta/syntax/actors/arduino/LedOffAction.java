package de.fhg.iais.roberta.syntax.actors.arduino;

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

@NepoBasic(name = "BOB3_RGB_LED_OFF", category = "ACTOR", blocklyNames = {"makeblockActions_leds_off"})
public final class LedOffAction extends Action {
    public final String side;

    public LedOffAction(String side, BlocklyProperties properties) {
        super(properties);
        this.side = side;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "LedOffAction [ ]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String side = Jaxb2Ast.extractField(fields, BlocklyConstants.LED + BlocklyConstants.SIDE);
        return new LedOffAction(side, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.LED + BlocklyConstants.SIDE, this.side);

        return jaxbDestination;

    }
}
