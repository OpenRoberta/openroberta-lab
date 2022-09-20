package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "TURN_DEGREES", category = "ACTOR", blocklyNames = {"naoActions_turn"})
public final class TurnDegrees extends Action {

    @NepoField(name = "DIRECTION")
    public final TurnDirection turnDirection;

    @NepoValue(name = "POWER", type = BlocklyType.NUMBER_INT)
    public final Expr degreesToTurn;

    public TurnDegrees(BlocklyProperties properties, TurnDirection turnDirection, Expr degreesToTurn) {
        super(properties);
        Assert.notNull(turnDirection, "Missing degrees in TurnDegrees block!");
        this.turnDirection = turnDirection;
        this.degreesToTurn = degreesToTurn;
        setReadOnly();
    }
}
