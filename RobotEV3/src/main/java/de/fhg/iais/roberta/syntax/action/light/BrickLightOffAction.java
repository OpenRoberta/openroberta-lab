package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "BRICK_LIGHT_OFF_ACTION", category = "ACTOR", blocklyNames = {"actions_brickLight_off_ev3"})
public final class BrickLightOffAction extends Action {

    @NepoField(name = "MODE")
    public final String mode;

    public BrickLightOffAction(BlocklyProperties properties, String mode) {
        super(properties);
        this.mode = mode;
        setReadOnly();
    }
}
