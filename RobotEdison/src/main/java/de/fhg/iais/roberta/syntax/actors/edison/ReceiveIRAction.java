package de.fhg.iais.roberta.syntax.actors.edison;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(category = "ACTOR", blocklyNames = {"edisonCommunication_ir_receiveBlock"}, name = "IR_RECV", blocklyType = BlocklyType.NUMBER)
public final class ReceiveIRAction extends Action {

    public ReceiveIRAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}