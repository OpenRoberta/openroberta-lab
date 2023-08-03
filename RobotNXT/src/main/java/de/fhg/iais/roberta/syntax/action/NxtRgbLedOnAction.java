package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "NXT_RGBLED_ON_ACTION", category = "ACTOR", blocklyNames = {"actions_rgbled_on_nxt"})
public final class NxtRgbLedOnAction extends Action {

    @NepoField(name = "ACTORPORT")
    public final String port;

    @NepoField(name = "COLOUR")
    public final BrickLedColor colour;

    public NxtRgbLedOnAction(BlocklyProperties properties, String port, BrickLedColor colour) {
        super(properties);
        this.port = port;
        this.colour = colour;
        setReadOnly();
    }
}
