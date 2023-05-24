package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.mode.action.LightMode;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "BUILT_IN_LED_ACTION", category = "ACTOR", blocklyNames = {"robActions_inbuilt_led"})
public final class BuiltInLedAction extends Action {

    @NepoField(name = "ACTORPORT", value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    @NepoField(name = "SWITCH_BLINK")
    public final LightMode mode;

    public BuiltInLedAction(BlocklyProperties properties, String port, LightMode mode) {
        super(properties);
        this.port = port;
        this.mode = mode;
        setReadOnly();
    }
}
