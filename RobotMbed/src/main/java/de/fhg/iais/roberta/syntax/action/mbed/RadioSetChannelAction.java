package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "RADIO_SET_CHANNEL_ACTION", category = "ACTOR", blocklyNames = {"mbedCommunication_setChannel"})
public final class RadioSetChannelAction<V> extends Action<V> {
    @NepoValue(name = "CONNECTION")
    public final Expr<V> channel;

    public RadioSetChannelAction(BlocklyProperties properties, Expr<V> channel) {
        super(properties);
        this.channel = channel;
        setReadOnly();
    }

}
