package de.fhg.iais.roberta.syntax.action.mbed.joycar;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_rgbLed_on_joycar"}, name = "RGB_LED_ON_ACTION_JOYCAR")
public final class RgbLedOnActionJoycar extends Action implements WithUserDefinedPort {
    @NepoValue(name = BlocklyConstants.COLOUR)
    public final Expr colour;
    @NepoField(name = "SLOT")
    public final String slot;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;


    public RgbLedOnActionJoycar(BlocklyProperties properties, Expr colour, String slot, String port, Hide hide) {
        super(properties);
        Assert.nonEmptyString(port);
        this.hide = hide;
        this.colour = colour;
        this.slot = slot;
        this.port = port;
        setReadOnly();
    }

    @Override
    public final String getUserDefinedPort() {
        return this.port;
    }

}