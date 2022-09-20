package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "MOTIONKIT_DUAL_SET_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_motionkit_dual_set"})
public final class MotionKitDualSetAction extends Action {

    @NepoField(name = "DIRECTION_LEFT")
    public final String directionLeft;

    @NepoField(name = "DIRECTION_RIGHT")
    public final String directionRight;

    public MotionKitDualSetAction(BlocklyProperties properties, String directionLeft, String directionRight) {
        super(properties);
        Assert.notNull(directionLeft);
        Assert.notNull(directionRight);
        this.directionLeft = directionLeft;
        this.directionRight = directionRight;
        setReadOnly();
    }
}
