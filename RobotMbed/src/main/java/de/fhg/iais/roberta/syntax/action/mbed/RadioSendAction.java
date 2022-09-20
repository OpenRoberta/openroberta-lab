package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "RADIO_SEND_ACTION", category = "ACTOR", blocklyNames = {"mbedCommunication_sendBlock"})
public final class RadioSendAction extends Action {

    @NepoMutation(fieldName = "datatype")
    public final Mutation mutation;

    @NepoField(name = "TYPE")
    public final String type;

    @NepoField(name = "POWER")
    public final String power;

    @NepoValue(name = "sendData", type = BlocklyType.STRING)
    public final Expr message;

    public RadioSendAction(BlocklyProperties properties, Mutation mutation, String type, String power, Expr msg) {
        super(properties);
        this.mutation = mutation;
        this.message = msg;
        this.type = type;
        this.power = power;
        setReadOnly();
    }
}
