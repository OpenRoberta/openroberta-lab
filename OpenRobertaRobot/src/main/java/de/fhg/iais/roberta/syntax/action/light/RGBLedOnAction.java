package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "RGBLED_ON_ACTION", category = "ACTOR", blocklyNames = {"robActions_rgbled_on"})
public final class RGBLedOnAction extends Action {

    @NepoField(name = "ACTORPORT", value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    @NepoValue(name = "COLOR", type = BlocklyType.COLOR)
    public final Expr rgbLedColor;

    public RGBLedOnAction(BlocklyProperties properties, String port, Expr rgbLedColor) {
        super(properties);
        this.port = port;
        this.rgbLedColor = rgbLedColor;
        setReadOnly();
    }
}