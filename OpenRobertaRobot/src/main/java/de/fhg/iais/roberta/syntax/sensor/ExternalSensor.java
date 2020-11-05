package de.fhg.iais.roberta.syntax.sensor;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.util.dbc.Assert;

public abstract class ExternalSensor<V> extends Sensor<V> {
    private final String port;
    private final String mode;
    private final String slot;
    private final boolean isPortInMutation;

    /**
     * This constructor set the kind of the sensor object used in the AST (abstract syntax tree). All possible kinds can be found in {@link BlockType}.
     *
     * @param kind of the the sensor object used in AST,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment of the user for the specific block
     */
    public ExternalSensor(SensorMetaDataBean metaDataBean, BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
        Assert.notNull(metaDataBean.getMode());
        Assert.notNull(metaDataBean.getPort());
        Assert.notNull(metaDataBean.getSlot());
        this.port = metaDataBean.getPort();
        this.mode = metaDataBean.getMode();
        this.slot = metaDataBean.getSlot();
        this.isPortInMutation = metaDataBean.isPortInMutation();
    }

    /**
     * @return get the port on which the sensor is connected. See enum {@link SensorPort} for all possible sensor ports
     */
    public String getPort() {
        return this.port;
    }

    /**
     * @return get the mode of sensor. See enum {@link IMode} for all possible modes that the sensor have
     */
    public String getMode() {
        return this.mode;
    }

    public String getSlot() {
        return this.slot;
    }

    public boolean isPortInMutation() {
        return this.isPortInMutation;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [" + this.getPort() + ", " + this.getMode() + ", " + this.getSlot() + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> SensorMetaDataBean extractPortAndModeAndSlot(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = AbstractJaxb2Ast.extractFields(block, (short) 3);
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        String portName = AbstractJaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT, BlocklyConstants.NO_PORT);
        String modeName = AbstractJaxb2Ast.extractField(fields, BlocklyConstants.MODE, BlocklyConstants.DEFAULT);
        String slotName = AbstractJaxb2Ast.extractField(fields, BlocklyConstants.SLOT, BlocklyConstants.NO_SLOT);
        boolean isPortInMutation = (block.getMutation() != null) && (block.getMutation().getPort() != null);
        return new SensorMetaDataBean(factory.sanitizePort(portName), factory.getMode(modeName), factory.sanitizeSlot(slotName), isPortInMutation);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        boolean addMutation = false;
        Mutation mutation = new Mutation();
        if ( !this.mode.toString().equals(BlocklyConstants.DEFAULT) ) {
            mutation.setMode(this.getMode().toString());
            addMutation = true;
            Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MODE, this.getMode().toString());
            if ( this.isPortInMutation ) {
                mutation.setPort(this.getPort().toString());
            }
        }
        if ( !this.getPort().toString().equals(BlocklyConstants.NO_PORT) ) {
            String fieldValue = this.getPort();
            Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        }
        if ( !this.getSlot().toString().equals(BlocklyConstants.NO_SLOT) ) {
            String fieldValue = this.getSlot();

            //TODO: Remove as soon as possible. This is only for deprecated XML resources for unit tests.
            if ( fieldValue.equals(BlocklyConstants.EMPTY_SLOT) ) {
                fieldValue = "";
            }
            Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.SLOT, fieldValue);
        }
        if ( addMutation ) {
            jaxbDestination.setMutation(mutation);
        }
        return jaxbDestination;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        SensorMetaDataBean sensorData = extractPortAndModeAndSlot(block, helper);
        return InfraredSensor.make(sensorData, AbstractJaxb2Ast.extractBlockProperties(block), AbstractJaxb2Ast.extractComment(block));
    }

}
