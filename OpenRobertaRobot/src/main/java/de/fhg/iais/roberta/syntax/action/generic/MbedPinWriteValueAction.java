package de.fhg.iais.roberta.syntax.action.generic;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "PIN_WRITE_VALUE_MBED", category = "ACTOR", blocklyNames = {"mbedActions_write_to_pin"})
public final class MbedPinWriteValueAction extends Action {

    @NepoMutation
    public final Mutation mutation;

    @NepoField(name = "VALUETYPE")
    public final String pinValue;

    @NepoField(name = "PIN")
    public final String port;

    @NepoValue(name = "VALUE", type = BlocklyType.NUMBER_INT)
    public final Expr value;

    public MbedPinWriteValueAction(BlocklyProperties properties, Mutation mutation, String pinValue, String port, Expr value) {
        super(properties);
        Assert.notNull(pinValue);
        Assert.notNull(port);
        Assert.notNull(value);
        this.mutation = mutation;
        this.pinValue = pinValue;
        this.port = port;
        this.value = value;
        setReadOnly();
    }
}
