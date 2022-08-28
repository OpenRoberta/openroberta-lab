package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "MOTIONKIT_SINGLE_SET_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_motionkit_single_set"})
public final class MotionKitSingleSetAction extends Action {
    @NepoField(name = "MOTORPORT")
    public final String port;

    @NepoField(name = "DIRECTION")
    public final String direction;

    public MotionKitSingleSetAction(BlocklyProperties properties, String port, String direction) {
        super(properties);
        Assert.notNull(port);
        Assert.notNull(direction);
        this.port = port;
        this.direction = direction;
        setReadOnly();
    }

}
