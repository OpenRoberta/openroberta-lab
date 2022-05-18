package de.fhg.iais.roberta.syntax.action.thymio;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_led_sound"}, name = "LED_SOUND_ON_ACTION")
public final class LedSoundOnAction extends Action {
    @NepoValue(name = "LED1", type = BlocklyType.NUMBER)
    public final Expr led1;

    public LedSoundOnAction(BlocklyProperties properties, Expr led1) {
        super(properties);
        this.led1 = led1;
        setReadOnly();
    }
}
