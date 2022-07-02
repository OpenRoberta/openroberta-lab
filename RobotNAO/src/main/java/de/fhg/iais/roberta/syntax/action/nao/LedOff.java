package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.mode.action.nao.Led;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "LED_OFF", category = "ACTOR", blocklyNames = {"naoActions_ledOff"})
public final class LedOff<V> extends Action<V> {

    @NepoField(name = "LED")
    public final Led led;

    public LedOff(BlocklyBlockProperties properties, BlocklyComment comment, Led led) {
        super(properties, comment);
        this.led = led;
        setReadOnly();
    }

}
