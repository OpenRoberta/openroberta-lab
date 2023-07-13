package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(name = "RGBLED_ON_ACTION", category = "ACTOR", blocklyNames = {"actions_rgbLed_on", "actions_rgbLed_on_mbot", "actions_rgbLed_on_nao", "actions_rgbLed_on_nibo", "actions_rgbled_on_nxt", "actions_rgbLed_on_thymio"})
public final class RgbLedOnAction extends Action implements WithUserDefinedPort {

    @NepoField(name = "ACTORPORT")
    public final String port;

    @NepoValue(name = "COLOUR", type = BlocklyType.COLOR)
    public final Expr color;

    public RgbLedOnAction(BlocklyProperties properties, String port, Expr color) {
        super(properties);
        this.port = port;
        this.color = color;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}