package de.fhg.iais.roberta.syntax.action.thymio;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_led_proxh"}, name = "LED_PROXH_ON_ACTION")
public final class LedProxHOnAction extends Action {
    @NepoValue(name = "LED1", type = BlocklyType.NUMBER)
    public final Expr led1;
    @NepoValue(name = "LED2", type = BlocklyType.NUMBER)
    public final Expr led2;
    @NepoValue(name = "LED3", type = BlocklyType.NUMBER)
    public final Expr led3;
    @NepoValue(name = "LED4", type = BlocklyType.NUMBER)
    public final Expr led4;
    @NepoValue(name = "LED5", type = BlocklyType.NUMBER)
    public final Expr led5;
    @NepoValue(name = "LED6", type = BlocklyType.NUMBER)
    public final Expr led6;
    @NepoValue(name = "LED7", type = BlocklyType.NUMBER)
    public final Expr led7;
    @NepoValue(name = "LED8", type = BlocklyType.NUMBER)
    public final Expr led8;

    public LedProxHOnAction(BlocklyProperties properties, Expr led1, Expr led2, Expr led3, Expr led4, Expr led5, Expr led6, Expr led7, Expr led8) {
        super(properties);
        this.led1 = led1;
        this.led2 = led2;
        this.led3 = led3;
        this.led4 = led4;
        this.led5 = led5;
        this.led6 = led6;
        this.led7 = led7;
        this.led8 = led8;
        setReadOnly();
    }
}
