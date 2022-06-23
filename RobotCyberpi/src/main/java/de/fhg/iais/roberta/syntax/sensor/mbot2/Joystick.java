package de.fhg.iais.roberta.syntax.sensor.mbot2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

/**
 * This class represents the <b>robSensors_joystick_getSample</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * stopping every movement of the robot.<br/>
 * <br/>
 */

@NepoPhrase(name = "JOYSTICK_SENSING", category = "SENSOR", blocklyNames = {"robSensors_joystickKeys_getSample"},
    sampleValues = {@F2M(field = "JOYSTICK_PRESSED", mode = "PRESSED")})
public final class Joystick<V> extends Sensor<V> implements WithUserDefinedPort<V> {
    @NepoMutation
    public final Mutation mutation;
    @NepoField(name = BlocklyConstants.MODE)
    public final String mode;
    @NepoField(name = BlocklyConstants.SENSORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoField(name = BlocklyConstants.SLOT, value = BlocklyConstants.EMPTY_SLOT)
    public final String slot;
    @NepoHide
    public final Hide hide;

    public Joystick(BlocklyBlockProperties properties, BlocklyComment comment, Mutation mutation, String mode, String port, String slot, Hide hide) {
        super(properties, comment);
        Assert.nonEmptyString(port);
        this.mutation = mutation;
        this.mode = mode;
        this.port = port;
        this.slot = slot;
        this.hide = hide;
        setReadOnly();
    }

    public Joystick(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        this(properties, comment, sensorMetaDataBean.getMutation(), sensorMetaDataBean.getMode(), sensorMetaDataBean.getPort(), sensorMetaDataBean.getSlot(), null);
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
