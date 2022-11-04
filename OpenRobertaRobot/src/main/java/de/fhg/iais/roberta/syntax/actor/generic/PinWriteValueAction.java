package de.fhg.iais.roberta.syntax.actor.generic;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.actor.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "PIN_WRITE_VALUE", category = "ACTOR", blocklyNames = {"robActions_write_pin"})
public final class PinWriteValueAction extends Action {

    @NepoMutation
    public final Mutation mutation;

    @NepoField(name = "MODE")
    public final String pinValue;

    @NepoField(name = "ACTORPORT")
    public final String port;

    @NepoValue(name = "VALUE", type = BlocklyType.NUMBER_INT)
    public final Expr value;

    public PinWriteValueAction(BlocklyProperties properties, Mutation mutation, String pinValue, String port, Expr value) {
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
