package de.fhg.iais.roberta.syntax.sensor.mbot2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoHide;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robSensors_gyro_axis_reset</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * stopping every movement of the robot.<br/>
 * <br/>
 */

@NepoPhrase(containerType = "GYRO_RESET_AXIS")
public class GyroResetAxis<V> extends Sensor<V> implements WithUserDefinedPort<V> {
    @NepoField(name = BlocklyConstants.SENSORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoField(name = BlocklyConstants.SLOT, value = BlocklyConstants.EMPTY_SLOT)
    public final String slot;
    @NepoHide
    public final Hide hide;

    public GyroResetAxis(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String port, String slot, Hide hide) {
        super(kind, properties, comment);
        Assert.nonEmptyString(port);
        this.port = port;
        this.slot = slot;
        this.hide = hide;
        setReadOnly();
    }
    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
