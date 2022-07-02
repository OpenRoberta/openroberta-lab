package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(name = "SERVO_SET_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_servo_set"})
public final class ServoSetAction<V> extends Action<V> implements WithUserDefinedPort<V> {
    @NepoField(name = "PIN_PORT")
    public final String port;
    @NepoValue(name = "VALUE", type = BlocklyType.NUMBER_INT)
    public final Expr<V> value;

    public ServoSetAction(BlocklyBlockProperties properties, BlocklyComment comment, String port, Expr<V> value) {
        super(properties, comment);
        Assert.notNull(port);
        Assert.notNull(value);
        this.port = port;
        this.value = value;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }

}
