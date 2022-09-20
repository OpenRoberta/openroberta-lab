package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "MOVE_JOINT", category = "ACTOR", blocklyNames = {"naoActions_moveJoint"})
public final class MoveJoint extends Action {

    @NepoField(name = "joint")
    public final String joint;

    @NepoField(name = "MODE")
    public final String relativeAbsolute;

    @NepoValue(name = "POWER", type = BlocklyType.NUMBER_INT)
    public final Expr degrees;

    public MoveJoint(BlocklyProperties properties, String joint, String relativeAbsolute, Expr degrees) {
        super(properties);
        Assert.notNull(joint, "Missing joint in MoveJoint block!");
        Assert.notNull(relativeAbsolute, "Missing relativeAbsolute in MoveJoint block!");
        this.joint = joint;
        this.relativeAbsolute = relativeAbsolute;
        this.degrees = degrees;
        setReadOnly();
    }
}
