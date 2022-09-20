package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "POINT_LOOK_AT", category = "ACTOR", blocklyNames = {"naoActions_pointLookAt"})
public final class PointLookAt extends Action {

    @NepoField(name = "MODE")
    public final String pointLook;

    @NepoField(name = "DIRECTION")
    public final String frame;

    @NepoValue(name = "X", type = BlocklyType.NUMBER_INT)
    public final Expr pointX;

    @NepoValue(name = "Y", type = BlocklyType.NUMBER_INT)
    public final Expr pointY;

    @NepoValue(name = "Z", type = BlocklyType.NUMBER_INT)
    public final Expr pointZ;

    @NepoValue(name = "Speed", type = BlocklyType.NUMBER_INT)
    public final Expr speed;

    public PointLookAt(BlocklyProperties properties, String pointLook, String frame, Expr pointX, Expr pointY, Expr pointZ, Expr speed) {
        super(properties);
        //Assert.notNull(frame, "Missing frame in PointLookAt block!");
        //Assert.notNull(pointLook, "Missing point look in PointLookAt block!");
        this.pointLook = pointLook;
        this.frame = frame;
        this.pointX = pointX;
        this.pointY = pointY;
        this.pointZ = pointZ;
        this.speed = speed;
        setReadOnly();
    }
}
