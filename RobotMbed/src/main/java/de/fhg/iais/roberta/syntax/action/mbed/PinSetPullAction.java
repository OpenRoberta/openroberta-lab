package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(containerType = "PIN_SET_PULL")
public class PinSetPullAction<V> extends Action<V> {
    @NepoField(name = BlocklyConstants.PIN_PULL)
    public final String pinPull;
    @NepoField(name = BlocklyConstants.PIN_PORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    public PinSetPullAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String pinPull, String port) {
        super(kind, properties, comment);
        Assert.notNull(pinPull);
        Assert.notNull(port);
        this.pinPull = pinPull;
        this.port = port;
        setReadOnly();
    }

    public static <V> PinSetPullAction<V> make(String pinPull, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PinSetPullAction<>(BlockTypeContainer.getByName("PIN_SET_PULL"), properties, comment, pinPull, port);
    }

    public String getMode() {
        return this.pinPull;
    }

    public String getPort() {
        return this.port;
    }
}
