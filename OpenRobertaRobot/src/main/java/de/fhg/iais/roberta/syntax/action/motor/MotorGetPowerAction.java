package de.fhg.iais.roberta.syntax.action.motor;

import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_motor_getPower"}, name = "MOTOR_GET_POWER_ACTION")
public final class MotorGetPowerAction<V> extends MoveAction<V> {
    @NepoField(name = BlocklyConstants.MOTORPORT)
    public final String port;

    public MotorGetPowerAction(BlocklyBlockProperties properties, BlocklyComment comment, String port) {
        super(properties, comment, port);
        Assert.isTrue(port != null);
        this.port = port;
        setReadOnly();
    }

    public static <V> MotorGetPowerAction<V> make(String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MotorGetPowerAction<V>(properties, comment, port);
    }

}
