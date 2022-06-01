package de.fhg.iais.roberta.syntax.sensor;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.SensorMetaDataBean;

public abstract class ExternalSensor<V> extends Sensor<V> implements WithUserDefinedPort<V> {
    private final SensorMetaDataBean metaDataBean;

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
        this.metaDataBean = metaDataBean;
    }

    /**
     * @return get the port on which the sensor is connected. See enum {@link SensorPort} for all possible sensor ports
     */
    public String getUserDefinedPort() {
        return metaDataBean.getPort();
    }

    public String getMode() {
        return metaDataBean.getMode();
    }

    public String getSlot() {
        return metaDataBean.getSlot();
    }

    public Mutation getMutation() {
        return metaDataBean.getMutation();
    }

    public List<Hide> getHide() {
        return metaDataBean.getHide();
    }

    public SensorMetaDataBean getSensorMetaDataBean() {
        return metaDataBean;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [" + this.getUserDefinedPort() + ", " + this.getMode() + ", " + this.getSlot() + "]";
    }

    /**
     * extract info about external sensor and put all relevant data into a bean: port, mode, slot, mutation; but no hide
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return an {@link SensorMetaDataBean}
     */
    public static <V> SensorMetaDataBean extractPortAndModeAndSlot(Block block, Jaxb2ProgramAst<V> helper) {
        return extractPortModeSlotMutationHide(block, helper);
    }

    /**
     * extract info about external sensor and put all relevant data into a bean
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return an {@link SensorMetaDataBean}
     */
    public static <V> SensorMetaDataBean extractPortModeSlotMutationHide(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 3);
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        String portName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT, BlocklyConstants.EMPTY_PORT);
        String modeName = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE, BlocklyConstants.DEFAULT);
        String slotName = Jaxb2Ast.extractField(fields, BlocklyConstants.SLOT, BlocklyConstants.EMPTY_SLOT);
        return new SensorMetaDataBean(Jaxb2Ast.sanitizePort(portName), factory.getMode(modeName), Jaxb2Ast.sanitizeSlot(slotName), block.getMutation(), block.getHide());
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        if ( !this.getMode().equals(BlocklyConstants.DEFAULT) ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, this.getMode());
        }
        if ( getMutation() != null ) {
            jaxbDestination.setMutation(getMutation());
        }
        List<Hide> hide = getHide();
        if ( hide != null ) {
            jaxbDestination.getHide().addAll(hide);
        }
        String portValue = this.getUserDefinedPort();
        if ( portValue.equals(BlocklyConstants.EMPTY_PORT) ) {
            portValue = "";
        }
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, portValue);
        String slotValue = this.getSlot();
        if ( slotValue.equals(BlocklyConstants.NO_SLOT) || slotValue.equals(BlocklyConstants.EMPTY_SLOT) ) {
            slotValue = "";
        }
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SLOT, slotValue);
        return jaxbDestination;
    }

    /**
     * this is an incredible bad design and must be refactored
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        SensorMetaDataBean sensorData = extractPortAndModeAndSlot(block, helper);
        return InfraredSensor.make(sensorData, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

}
