package de.fhg.iais.roberta.syntax.actor.thymio;

import de.fhg.iais.roberta.syntax.actor.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_led_proxv"}, name = "LED_PROXV_ON_ACTION")
public final class LedProxVOnAction extends Action {
    @NepoValue(name = "LED1", type = BlocklyType.NUMBER)
    public final Expr led1;
    @NepoValue(name = "LED2", type = BlocklyType.NUMBER)
    public final Expr led2;

    public LedProxVOnAction(BlocklyProperties properties, Expr led1, Expr led2) {
        super(properties);
        this.led1 = led1;
        this.led2 = led2;
        setReadOnly();
    }
}
