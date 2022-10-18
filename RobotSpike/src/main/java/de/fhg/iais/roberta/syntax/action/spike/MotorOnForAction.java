package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_motor_on_for"}, name = "MOTOR_ON_FOR_ACTION")
public final class MotorOnForAction extends ActionWithUserChosenName {
    @NepoValue(name = BlocklyConstants.POWER, type = BlocklyType.NUMBER)
    public final Expr power;
    @NepoField(name = "UNIT")
    public final String unit;
    @NepoValue(name = BlocklyConstants.VALUE, type = BlocklyType.NUMBER)
    public final Expr value;

    public MotorOnForAction(
        BlocklyProperties properties,
        Expr power,
        String unit,
        Expr value,
        String port) {
        super(properties, port);
        this.power = power;
        this.unit = unit;
        this.value = value;
        setReadOnly();
    }

}