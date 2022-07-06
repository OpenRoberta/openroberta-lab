package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"mbedActions_motors_on"}, name = "BOTH_MOTORS_ON_ACTION")
public final class BothMotorsOnAction extends Action {
    @NepoValue(name = BlocklyConstants.POWER_A, type = BlocklyType.NUMBER_INT)
    public final Expr speedA;
    @NepoValue(name = BlocklyConstants.POWER_B, type = BlocklyType.NUMBER_INT)
    public final Expr speedB;
    @NepoField(name = BlocklyConstants.A, value = BlocklyConstants.A)
    public final String portA;
    @NepoField(name = BlocklyConstants.B, value = BlocklyConstants.B)
    public final String portB;

    public BothMotorsOnAction(BlocklyProperties properties, Expr speedA, Expr speedB, String portA, String portB) {
        super(properties);
        Assert.isTrue((speedA != null) && speedA.isReadOnly());
        Assert.isTrue((speedB != null) && speedB.isReadOnly());
        this.portA = portA;
        this.portB = portB;
        this.speedA = speedA;
        this.speedB = speedB;
        setReadOnly();
    }

}
