package de.fhg.iais.roberta.syntax.actors.arduino;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "FESTOBIONIC_STEPMOTOR", category = "ACTOR", blocklyNames = {"festobionicActions_stepmotor"})
public final class StepMotorAction extends Action {
    @NepoValue(name = "VALUE")
    public final Expr stepMotorPos;

    public StepMotorAction(BlocklyProperties properties, Expr stepMotorPos) {
        super(properties);
        Assert.notNull(stepMotorPos);
        this.stepMotorPos = stepMotorPos;
        setReadOnly();
    }

}
