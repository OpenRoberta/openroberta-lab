package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_motor_on"}, name = "MOTOR_ON_ACTION")
public final class MotorOnAction extends ActionWithUserChosenName {
    @NepoValue(name = BlocklyConstants.POWER, type = BlocklyType.NUMBER)
    public final Expr power;
    @NepoField(name = "REGULATION")
    public final Boolean regulation;

    public MotorOnAction(
        BlocklyProperties properties,
        Expr power,
        Boolean regulation,
        String port) {
        super(properties, port);
        this.power = power;
        this.regulation = regulation;
        setReadOnly();
    }
}