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

}
