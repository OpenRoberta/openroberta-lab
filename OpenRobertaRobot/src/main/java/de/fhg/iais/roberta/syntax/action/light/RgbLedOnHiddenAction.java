package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "RGBLED_ON_HIDDEN_ACTION", category = "ACTOR", blocklyNames = {"actions_rgbLed_hidden_on", "actions_rgbLed_hidden_on_calliope"})
public final class RgbLedOnHiddenAction extends Action {
    @NepoValue(name = "COLOUR", type = BlocklyType.COLOR)
    public final Expr colour;

    @NepoHide
    public final Hide hide;

    public RgbLedOnHiddenAction(BlocklyProperties properties, Expr colour, Hide hide) {
        super(properties);
        this.colour = colour;
        this.hide = hide;
        setReadOnly();
    }
}