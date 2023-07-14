package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.LightMode;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "BRICK_LIGHT_ON_ACTION", category = "ACTOR", blocklyNames = {"actions_brickLight_on_ev3"})
public final class BrickLightOnAction extends Action {

    @NepoField(name = "MODE")
    public final LightMode mode;

    @NepoField(name = "COLOUR")
    public final BrickLedColor colour;

    public BrickLightOnAction(BlocklyProperties properties, LightMode mode, BrickLedColor colour) {
        super(properties);
        this.mode = mode;
        this.colour = colour;
        setReadOnly();
    }
}
