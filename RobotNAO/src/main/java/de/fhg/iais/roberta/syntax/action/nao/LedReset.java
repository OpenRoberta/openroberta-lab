package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.mode.action.nao.Led;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "LED_RESET", category = "ACTOR", blocklyNames = {"naoActions_ledReset"})
public final class LedReset<V> extends Action<V> {
    @NepoField(name = "LED")
    public final Led led;

    public LedReset(BlocklyProperties properties, Led led) {
        super(properties);
        this.led = led;
        setReadOnly();
    }

}
