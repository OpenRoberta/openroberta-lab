package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "RADIO_SET_CHANNEL_ACTION", category = "ACTOR", blocklyNames = {"mbedCommunication_setChannel"})
public final class RadioSetChannelAction<V> extends Action<V> {
    @NepoValue(name = "CONNECTION")
    public final Expr<V> channel;

    public RadioSetChannelAction(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> channel) {
        super(properties, comment);
        this.channel = channel;
        setReadOnly();
    }

    /**
     * Creates instance of {@link RadioSetChannelAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link RadioSetChannelAction}
     */
    public static <V> RadioSetChannelAction<V> make(Expr<V> channel, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RadioSetChannelAction<>(properties, comment, channel);
    }

    public Expr<V> getChannel() {
        return this.channel;
    }
}
