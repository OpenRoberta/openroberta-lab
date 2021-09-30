package de.fhg.iais.roberta.syntax.sensor.mbot2;

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
import de.fhg.iais.roberta.transformer.NepoMutation;
import de.fhg.iais.roberta.transformer.NepoPhrase;

@NepoPhrase(containerType = "QUAD_COLOR_SENSING")
public class QuadRGBSensor<V> extends Sensor<V> implements WithUserDefinedPort<V> {
    @NepoMutation(fieldName = BlocklyConstants.MODE)
    public final Mutation mutation;
    @NepoField(name = BlocklyConstants.MODE)
    public final String mode;
    @NepoField(name = BlocklyConstants.SENSORPORT)
    public final String sensorPort;
    @NepoField(name = BlocklyConstants.SLOT)
    public final String slot;

    public QuadRGBSensor(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Mutation mutation, String mode, String sensorPort, String slot) {
        super(kind, properties, comment);
        this.mutation = mutation;
        this.mode = mode;
        this.sensorPort = sensorPort;
        this.slot = slot;
        setReadOnly();
    }

    /**
     * Creates instance of {@link QuadRGBSensor}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link DisplaySetColourAction}
     */
    public static <V> QuadRGBSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new QuadRGBSensor<>(BlockTypeContainer.getByName("QUAD_COLOR_SENSING"), properties, comment,sensorMetaDataBean.getMutation(), sensorMetaDataBean.getMode(), sensorMetaDataBean.getPort(),sensorMetaDataBean.getSlot());
    }

    @Override
    public String getUserDefinedPort() {
        return this.sensorPort;
    }

}
