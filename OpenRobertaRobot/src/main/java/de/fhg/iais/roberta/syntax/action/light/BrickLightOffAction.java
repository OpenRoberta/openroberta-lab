package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "BRICK_LIGHT_OFF_ACTION", category = "ACTOR", blocklyNames = {"robActions_brickLight_off"})
public final class BrickLightOffAction extends Action {

    public BrickLightOffAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
