package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoBasic(name = "DISPLAY_GET_BRIGHTNESS", category = "ACTOR", blocklyNames = {"mbedActions_display_getBrightness"})
public final class DisplayGetBrightnessAction<V> extends Action<V> {

    public DisplayGetBrightnessAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }

    @Override
    public String toString() {
        return "DisplayGetBrightnessAction []";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {

        return new DisplayGetBrightnessAction<>(Jaxb2Ast.extractBlocklyProperties(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        return jaxbDestination;
    }
}
