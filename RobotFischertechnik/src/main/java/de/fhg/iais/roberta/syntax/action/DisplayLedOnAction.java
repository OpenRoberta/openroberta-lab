package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_display_led_on_txt"}, name = "DISPLAY_LED_ON_ACTION")
public final class DisplayLedOnAction extends Action {
    @NepoValue(name = "COLOUR", type = BlocklyType.COLOR)
    public final Expr colour;
    @NepoField(name = "DISPLAY")
    public final String mode;
    @NepoField(name = "ACTORPORT")
    public final String port;

    public DisplayLedOnAction(BlocklyProperties properties, Expr colour, String mode, String port) {
        super(properties);
        Assert.notNull(colour);
        Assert.notNull(port);
        this.colour = colour;
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }
}
