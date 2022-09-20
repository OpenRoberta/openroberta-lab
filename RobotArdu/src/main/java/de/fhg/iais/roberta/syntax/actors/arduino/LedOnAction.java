package de.fhg.iais.roberta.syntax.actors.arduino;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "BOB3_RGB_LED_ON", category = "ACTOR", blocklyNames = {"makeblockActions_leds_on"})
public final class LedOnAction extends Action {

    @NepoField(name = "LEDSIDE")
    public final String side;

    @NepoValue(name = "COLOR", type = BlocklyType.COLOR)
    public final Expr ledColor;

    public LedOnAction(BlocklyProperties properties, String side, Expr ledColor) {
        super(properties);
        Assert.notNull(ledColor);
        this.side = side;
        this.ledColor = ledColor;
        setReadOnly();
    }
}
