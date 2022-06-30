package de.fhg.iais.roberta.syntax.action.nao;


import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "WALK_DISTANCE", category = "ACTOR", blocklyNames = {"naoActions_walk"})
public final class WalkDistance<V> extends Action<V> {
    @NepoField(name = "DIRECTION",value = BlocklyConstants.DIRECTION)
    public final DriveDirection walkDirection;
    @NepoValue(name = "POWER", type = BlocklyType.NUMBER_INT)
    public final Expr<V> distanceToWalk;

    public WalkDistance(BlocklyBlockProperties properties, BlocklyComment comment, DriveDirection walkDirection, Expr<V> distanceToWalk) {
        super(properties, comment);
        Assert.notNull(walkDirection, "Missing direction in WalkDistance block!");
        this.walkDirection = walkDirection;
        this.distanceToWalk = distanceToWalk;
        setReadOnly();
    }

    public static <V> WalkDistance<V> make(DriveDirection walkDirection, Expr<V> distanceToWalk, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new WalkDistance<V>(properties, comment, walkDirection, distanceToWalk);
    }

    public DriveDirection getWalkDirection() {
        return this.walkDirection;
    }

    public Expr<V> getDistanceToWalk() {
        return this.distanceToWalk;
    }

}
