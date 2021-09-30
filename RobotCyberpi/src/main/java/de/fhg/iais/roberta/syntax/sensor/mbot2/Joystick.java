package de.fhg.iais.roberta.syntax.sensor.mbot2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.syntax.action.mbot2.DisplaySetColourAction;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoHide;
import de.fhg.iais.roberta.transformer.NepoMutation;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robSensors_joystick_getSample</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * stopping every movement of the robot.<br/>
 * <br/>
 */

@NepoPhrase(containerType = "JOYSTICK_SENSING")
public class Joystick<V> extends Sensor<V> implements WithUserDefinedPort<V> {
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

    public Joystick(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Mutation mutation, String mode, String port, String slot, Hide hide) {
        super(kind, properties, comment);
        Assert.nonEmptyString(port);
        this.mutation = mutation;
        this.mode = mode;
        this.port = port;
        this.slot = slot;
        this.hide = hide;
        setReadOnly();
    }

    /**
     * Creates instance of {@link Joystick}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link DisplaySetColourAction}
     */
    public static <V> Joystick<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Joystick<>(BlockTypeContainer.getByName("JOYSTICK_SENSING"), properties, comment, sensorMetaDataBean.getMutation(), sensorMetaDataBean.getMode(), sensorMetaDataBean.getPort(), sensorMetaDataBean.getSlot(), null);
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
