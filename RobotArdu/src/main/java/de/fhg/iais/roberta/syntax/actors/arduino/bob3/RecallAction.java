package de.fhg.iais.roberta.syntax.actors.arduino.bob3;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "BOB3_RECALL", category = "ACTOR", blocklyNames = {"bob3Actions_recall"}, blocklyType = BlocklyType.NUMBER)
public final class RecallAction extends Action {

    public RecallAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
