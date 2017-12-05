package de.fhg.iais.roberta.syntax.sensor;

import java.util.List;
import java.util.function.Function;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISlot;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;

public abstract class ExternalSensor<V> extends Sensor<V> {
    private final ISensorPort port;
    private final IMode mode;
    private final ISlot slot;

    /**
     * This constructor set the kind of the sensor object used in the AST (abstract syntax tree). All possible kinds can be found in {@link BlockType}.
     *
     * @param kind of the the sensor object used in AST,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment of the user for the specific block
     */
    public ExternalSensor(SensorMetaDataBean metaDataBean, BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
        Assert.isTrue(metaDataBean.getMode() != null && metaDataBean.getPort() != null && metaDataBean.getSlot() != null);
        this.port = metaDataBean.getPort();
        this.mode = metaDataBean.getMode();
        this.slot = metaDataBean.getSlot();
    }

    /**
     * @return get the port on which the sensor is connected. See enum {@link SensorPort} for all possible sensor ports
     */
    public ISensorPort getPort() {
        return this.port;
    }

    /**
     * @return get the mode of sensor. See enum {@link IMode} for all possible modes that the sensor have
     */
    public IMode getMode() {
        return this.mode;
    }

    public ISlot getSlot() {
        return this.slot;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [" + getPort() + ", " + getMode() + ", " + getSlot() + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> SensorMetaDataBean extractPortAndMode(Block block, Jaxb2AstTransformer<V> helper, Function<String, IMode> getMode) {
        List<Field> fields = helper.extractFields(block, (short) 3);
        IRobotFactory factory = helper.getModeFactory();
        String portName = helper.extractField(fields, BlocklyConstants.SENSORPORT, BlocklyConstants.NO_PORT);
        String modeName = helper.extractField(fields, BlocklyConstants.MODE, BlocklyConstants.DEFAULT);
        String slotName = helper.extractField(fields, BlocklyConstants.SLOT, BlocklyConstants.NO_SLOT);

        return new SensorMetaDataBean(factory.getSensorPort(portName), getMode.apply(modeName), factory.getSlot(slotName));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        if ( !this.mode.toString().equals(BlocklyConstants.DEFAULT) ) {
            Mutation mutation = new Mutation();
            mutation.setMode(getMode().toString());
            jaxbDestination.setMutation(mutation);
            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE, getMode().toString());
        }
        if ( !this.getPort().toString().equals(BlocklyConstants.NO_PORT) ) {
            String fieldValue = getPort().getPortNumber();
            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        }
        if ( !this.getSlot().toString().equals(BlocklyConstants.NO_SLOT) ) {
            String fieldValue = getPort().getPortNumber();
            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SLOT, fieldValue);
        }
        return jaxbDestination;
    }
}
