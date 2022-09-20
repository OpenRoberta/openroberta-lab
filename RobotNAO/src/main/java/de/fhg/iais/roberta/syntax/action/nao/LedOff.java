package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "LED_OFF", category = "ACTOR", blocklyNames = {"naoActions_ledOff"})
public final class LedOff extends Action {

    @NepoField(name = "LED")
    public final String led;

    public LedOff(BlocklyProperties properties, String led) {
        super(properties);
        this.led = led;
        setReadOnly();
    }
}
