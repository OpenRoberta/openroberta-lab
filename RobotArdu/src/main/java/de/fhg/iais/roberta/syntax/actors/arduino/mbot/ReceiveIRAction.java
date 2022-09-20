package de.fhg.iais.roberta.syntax.actors.arduino.mbot;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "IR_RECEIVER", category = "ACTOR", blocklyNames = {"robCommunication_ir_receiveBlock"}, blocklyType = BlocklyType.STRING)
public final class ReceiveIRAction extends Action {

    public ReceiveIRAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
