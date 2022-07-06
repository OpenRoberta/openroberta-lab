package de.fhg.iais.roberta.syntax.actors.edison;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"edisonCommunication_ir_sendBlock"}, name = "IR_SEND")
public final class SendIRAction extends Action {

    @NepoValue(name = BlocklyConstants.MESSAGE, type = BlocklyType.NUMBER)
    public final Expr code;

    public SendIRAction(BlocklyProperties properties, Expr code) {
        super(properties);
        this.code = code;
        setReadOnly();
    }

}
