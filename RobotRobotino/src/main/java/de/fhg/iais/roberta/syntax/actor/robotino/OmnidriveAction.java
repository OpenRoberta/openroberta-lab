package de.fhg.iais.roberta.syntax.actor.robotino;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;


@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_motorOmni_curve"}, name = "MOTOR_OMNIDRIVE_ACTION")
public final class OmnidriveAction extends Action implements WithUserDefinedPort {
    @NepoValue(name = BlocklyConstants.X, type = BlocklyType.NUMBER)
    public final Expr xVel;
    @NepoValue(name = BlocklyConstants.Y, type = BlocklyType.NUMBER)
    public final Expr yVel;
    @NepoValue(name = "THETA", type = BlocklyType.NUMBER)
    public final Expr thetaVel;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public OmnidriveAction(BlocklyProperties properties, Expr xVel, Expr yVel, Expr thetaVel, String port, Hide hide) {
        super(properties);
        Assert.nonEmptyString(port);

        this.hide = hide;
        this.xVel = xVel;
        this.yVel = yVel;
        this.thetaVel = thetaVel;
        this.port = port;

        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}

