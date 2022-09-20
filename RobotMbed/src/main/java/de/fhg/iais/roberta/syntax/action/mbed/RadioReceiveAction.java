package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "RADIO_RECEIVE_ACTION", category = "ACTOR", blocklyNames = {"mbedCommunication_receiveBlock"}, blocklyType = BlocklyType.STRING)
public final class RadioReceiveAction extends Action {

    @NepoMutation(fieldName = "datatype")
    public final Mutation mutation;

    @NepoField(name = "TYPE")
    public final String type;

    public RadioReceiveAction(BlocklyProperties properties, Mutation mutation, String type) {
        super(properties);
        this.mutation = mutation;
        this.type = type;
        setReadOnly();
    }
}
