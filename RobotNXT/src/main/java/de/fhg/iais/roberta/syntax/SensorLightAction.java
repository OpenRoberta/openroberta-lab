package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.LightMode;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "SENSOR_LIGHT_ACTION", category = "ACTOR", blocklyNames = {"robActions_sensorLight_on"})
public final class SensorLightAction extends Action {

    @NepoField(name = "SWITCH_COLOR")
    public final BrickLedColor color;

    @NepoField(name = "SWITCH_STATE")
    public final LightMode mode;

    @NepoField(name = "SENSORPORT")
    public final String port;

    public SensorLightAction(BlocklyProperties properties, BrickLedColor color, LightMode mode, String port) {
        super(properties);
        this.color = color;
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }
}
