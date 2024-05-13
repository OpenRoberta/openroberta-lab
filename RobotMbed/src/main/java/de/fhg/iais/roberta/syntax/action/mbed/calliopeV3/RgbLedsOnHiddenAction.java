package de.fhg.iais.roberta.syntax.action.mbed.calliopeV3;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "RGBLEDS_ON_HIDDEN_ACTION", category = "ACTOR", blocklyNames = {"actions_rgbLeds_hidden_on_calliopev3"})
public final class RgbLedsOnHiddenAction extends Action {
    @NepoValue(name = "COLOUR", type = BlocklyType.COLOR)
    public final Expr colour;
    @NepoField(name = "SLOT")
    public final String slot;
    @NepoHide
    public final Hide hide;

    public RgbLedsOnHiddenAction(BlocklyProperties properties, Expr colour, String slot, Hide hide) {
        super(properties);
        this.hide = hide;
        this.colour = colour;
        this.slot = slot;
        setReadOnly();
    }
}
