package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "BRICK_LIGHT_RESET_ACTION", category = "ACTOR", blocklyNames = {"robActions_brickLight_reset"})
public final class BrickLightResetAction extends Action {

    public BrickLightResetAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
