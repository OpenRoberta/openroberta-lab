package de.fhg.iais.roberta.syntax.action.nao;


import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "WALK_DISTANCE", category = "ACTOR", blocklyNames = {"naoActions_walk"})
public final class WalkDistance extends Action {
    @NepoField(name = "DIRECTION",value = BlocklyConstants.DIRECTION)
    public final DriveDirection walkDirection;
    @NepoValue(name = "POWER", type = BlocklyType.NUMBER_INT)
    public final Expr distanceToWalk;

    public WalkDistance(BlocklyProperties properties, DriveDirection walkDirection, Expr distanceToWalk) {
        super(properties);
        Assert.notNull(walkDirection, "Missing direction in WalkDistance block!");
        this.walkDirection = walkDirection;
        this.distanceToWalk = distanceToWalk;
        setReadOnly();
    }

}
