package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.LightMode;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "BRICK_LIGHT_ON_ACTION", category = "ACTOR", blocklyNames = {"robActions_brickLight_on"})
public final class BrickLightOnAction extends Action {

    @NepoField(name = "SWITCH_COLOR")
    public final BrickLedColor color;

    @NepoField(name = "SWITCH_BLINK")
    public final LightMode mode;

    public BrickLightOnAction(BlocklyProperties properties, BrickLedColor color, LightMode mode) {
        super(properties);
        this.color = color;
        this.mode = mode;
        setReadOnly();
    }
}
