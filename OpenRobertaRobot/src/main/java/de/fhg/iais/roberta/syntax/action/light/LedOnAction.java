package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "LED_ON_ACTION", category = "ACTOR", blocklyNames = {"robActions_led_on"})
public final class LedOnAction extends Action {

    @NepoField(name = "ACTORPORT", value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    public LedOnAction(BlocklyProperties properties, String port) {
        super(properties);
        this.port = port;
        setReadOnly();
    }
}