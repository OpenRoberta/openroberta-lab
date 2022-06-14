package de.fhg.iais.roberta.syntax.actors.edison;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"edisonCommunication_ir_sendBlock"}, containerType = "IR_SEND")
public class SendIRAction<V> extends Action<V> {

    @NepoValue(name = BlocklyConstants.MESSAGE, type = BlocklyType.NUMBER)
    public final Expr<V> code;

    public SendIRAction(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> code) {
        super(properties, comment);
        this.code = code;
        setReadOnly();
    }

    public static <V> SendIRAction<V> make(Expr<V> code, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SendIRAction<V>(properties, comment, code);
    }

    public Expr<V> getCode() {
        return this.code;
    }
}
