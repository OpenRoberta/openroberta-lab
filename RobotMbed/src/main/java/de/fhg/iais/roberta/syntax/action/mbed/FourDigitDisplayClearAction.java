package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "FOURDIGITDISPLAY_CLEAR_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_fourDigitDisplay_clear"})
public final class FourDigitDisplayClearAction extends Action {

    public FourDigitDisplayClearAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
