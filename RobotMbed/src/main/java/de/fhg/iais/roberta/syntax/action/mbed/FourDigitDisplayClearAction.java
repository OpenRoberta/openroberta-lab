package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoBasic(name = "FOURDIGITDISPLAY_CLEAR_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_fourDigitDisplay_clear"})
public final class FourDigitDisplayClearAction<V> extends Action<V> {

    public FourDigitDisplayClearAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }

    @Override
    public String toString() {
        return "FourDigitDisplayClearAction []";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        return new FourDigitDisplayClearAction<>(Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        return jaxbDestination;
    }

}
