package de.fhg.iais.roberta.syntax.action.mbot2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "RGBLED_ON_HIDDEN_MBOT2_ACTION", category = "ACTOR", blocklyNames = {"actions_rgbLed_hidden_on_mbot2"})
public final class Mbot2RgbLedOnHiddenAction extends Action {
    @NepoValue(name = "COLOUR", type = BlocklyType.COLOR)
    public final Expr color;
    @NepoField(name = "SLOT")
    public final String slot;
    @NepoHide
    public final Hide hide;

    public Mbot2RgbLedOnHiddenAction(BlocklyProperties properties, Expr color, String slot, Hide hide) {
        super(properties);
        this.hide = hide;
        this.color = color;
        this.slot = slot;
        setReadOnly();
    }
}
