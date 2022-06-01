package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(containerType = "BOTH_MOTORS_ON_ACTION")
public final class BothMotorsOnAction<V> extends Action<V> {
    @NepoValue(name = BlocklyConstants.POWER_A, type = BlocklyType.NUMBER_INT)
    public final Expr<V> speedA;
    @NepoValue(name = BlocklyConstants.POWER_B, type = BlocklyType.NUMBER_INT)
    public final Expr<V> speedB;
    @NepoField(name = BlocklyConstants.A, value = BlocklyConstants.A)
    public final String portA;
    @NepoField(name = BlocklyConstants.B, value = BlocklyConstants.B)
    public final String portB;

    public BothMotorsOnAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> speedA, Expr<V> speedB, String portA, String portB) {
        super(kind, properties, comment);
        Assert.isTrue((speedA != null) && speedA.isReadOnly());
        Assert.isTrue((speedB != null) && speedB.isReadOnly());
        this.portA = portA;
        this.portB = portB;
        this.speedA = speedA;
        this.speedB = speedB;
        setReadOnly();
    }

    public static <V> BothMotorsOnAction<V> make(
        String portA,
        String portB,
        Expr<V> speedA,
        Expr<V> speedB,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new BothMotorsOnAction<V>(BlockTypeContainer.getByName("BOTH_MOTORS_ON_ACTION"), properties, comment, speedA, speedB, portA, portB);
    }

    public Expr<V> getSpeedA() {
        return this.speedA;
    }

    public Expr<V> getSpeedB() {
        return this.speedB;
    }

    public String getPortA() {
        return this.portA;
    }

    public String getPortB() {
        return this.portB;
    }
}
