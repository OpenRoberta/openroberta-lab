package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "SINGLE_MOTOR_ON_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_single_motor_on"})
public final class SingleMotorOnAction<V> extends Action<V> {
    @NepoValue(name = "POWER",type = BlocklyType.NUMBER_INT)
    public final Expr<V> speed;

    public SingleMotorOnAction(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> speed) {
        super(properties, comment);
        Assert.isTrue((speed != null) && speed.isReadOnly());
        this.speed = speed;

        setReadOnly();
    }

    public static <V> SingleMotorOnAction<V> make(Expr<V> speed, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SingleMotorOnAction<>(properties, comment, speed);
    }

    public Expr<V> getSpeed() {
        return this.speed;
    }


}
