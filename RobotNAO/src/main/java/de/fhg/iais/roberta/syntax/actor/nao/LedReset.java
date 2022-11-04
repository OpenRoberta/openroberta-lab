package de.fhg.iais.roberta.syntax.actor.nao;

import de.fhg.iais.roberta.syntax.actor.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "LED_RESET", category = "ACTOR", blocklyNames = {"naoActions_ledReset"})
public final class LedReset extends Action {

    @NepoField(name = "LED")
    public final String led;

    public LedReset(BlocklyProperties properties, String led) {
        super(properties);
        this.led = led;
        setReadOnly();
    }
}
