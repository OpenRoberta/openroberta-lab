package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"mbedActions_pin_set_pull"}, containerType = "PIN_SET_PULL")
public class PinSetPullAction<V> extends Action<V> {
    @NepoField(name = BlocklyConstants.PIN_PULL)
    public final String pinPull;
    @NepoField(name = BlocklyConstants.PIN_PORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    public PinSetPullAction(BlocklyBlockProperties properties, BlocklyComment comment, String pinPull, String port) {
        super(properties, comment);
        Assert.notNull(pinPull);
        Assert.notNull(port);
        this.pinPull = pinPull;
        this.port = port;
        setReadOnly();
    }

    public static <V> PinSetPullAction<V> make(String pinPull, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PinSetPullAction<>(properties, comment, pinPull, port);
    }

    public String getMode() {
        return this.pinPull;
    }

    public String getPort() {
        return this.port;
    }
}
