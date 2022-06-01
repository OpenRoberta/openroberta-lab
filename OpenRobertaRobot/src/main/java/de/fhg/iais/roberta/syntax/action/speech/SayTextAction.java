package de.fhg.iais.roberta.syntax.action.speech;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(containerType = "SAY_TEXT")
public class SayTextAction<V> extends Action<V> {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.STRING)
    public final Expr<V> msg;

    public SayTextAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> msg) {
        super(kind, properties, comment);
        Assert.isTrue(msg != null);
        this.msg = msg;
        setReadOnly();
    }

    public static <V> SayTextAction<V> make(Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SayTextAction<>(BlockTypeContainer.getByName("SAY_TEXT"), properties, comment, msg);
    }

    /**
     * @return the message.
     */
    public Expr<V> getMsg() {
        return this.msg;
    }
}
