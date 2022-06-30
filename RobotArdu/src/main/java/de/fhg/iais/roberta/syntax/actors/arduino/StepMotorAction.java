package de.fhg.iais.roberta.syntax.actors.arduino;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "FESTOBIONIC_STEPMOTOR", category = "ACTOR", blocklyNames = {"festobionicActions_stepmotor"})
public final class StepMotorAction<V> extends Action<V> {
    public final Expr<V> stepMotorPos;

    public StepMotorAction(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> stepMotorPos) {
        super(properties, comment);
        Assert.notNull(stepMotorPos);
        this.stepMotorPos = stepMotorPos;
        setReadOnly();
    }

    public static <V> StepMotorAction<V> make(Expr<V> stepMotorPos, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new StepMotorAction<>(properties, comment, stepMotorPos);
    }

    public Expr<V> getStepMotorPos() {
        return this.stepMotorPos;
    }
}
