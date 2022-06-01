package de.fhg.iais.roberta.syntax.actors.edison;

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

@NepoPhrase(containerType = "IR_SEND")
public class SendIRAction<V> extends Action<V> {

    @NepoValue(name = BlocklyConstants.MESSAGE, type = BlocklyType.NUMBER)
    public final Expr<V> code;

    public SendIRAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> code) {
        super(kind, properties, comment);
        this.code = code;
        setReadOnly();
    }

    public static <V> SendIRAction<V> make(Expr<V> code, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SendIRAction<V>(BlockTypeContainer.getByName("IR_SEND"), properties, comment, code);
    }

    public Expr<V> getCode() {
        return this.code;
    }
}
