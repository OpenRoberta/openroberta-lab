package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public class DropSensor<V> extends ExternalSensor<V> {

    public DropSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("DROP_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link DropSensor}.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link DropSensor}
     */
    public static <V> DropSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DropSensor<>(sensorMetaDataBean, properties, comment);
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ISensorVisitor<V>) visitor).visitDropSensor(this);
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
        return DropSensor.make(sensorData, helper.extractBlockProperties(block), helper.extractComment(block));
    }

}
