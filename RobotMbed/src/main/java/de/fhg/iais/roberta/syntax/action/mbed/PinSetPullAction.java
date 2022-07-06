package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"mbedActions_pin_set_pull"}, name = "PIN_SET_PULL")
public final class PinSetPullAction extends Action {
    @NepoField(name = BlocklyConstants.PIN_PULL)
    public final String pinPull;
    @NepoField(name = BlocklyConstants.PIN_PORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    public PinSetPullAction(BlocklyProperties properties, String pinPull, String port) {
        super(properties);
        Assert.notNull(pinPull);
        Assert.notNull(port);
        this.pinPull = pinPull;
        this.port = port;
        setReadOnly();
    }

}
