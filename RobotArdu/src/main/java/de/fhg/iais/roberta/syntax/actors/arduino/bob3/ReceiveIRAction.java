package de.fhg.iais.roberta.syntax.actors.arduino.bob3;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "BOB3_RECVIR", category = "ACTOR", blocklyNames = {"bob3Communication_receiveBlock"}, blocklyType = BlocklyType.NUMBER)
public final class ReceiveIRAction extends Action {

    public ReceiveIRAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
