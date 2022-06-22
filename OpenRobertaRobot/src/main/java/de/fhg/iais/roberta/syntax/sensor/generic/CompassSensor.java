package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(sampleValues = {@NepoSampleValue(blocklyFieldName = "COMPASS_COMPASS", sensor = "COMPASS", mode = "COMPASS"), @NepoSampleValue(blocklyFieldName = "COMPASS_X", sensor = "COMPASS", mode = "X"), @NepoSampleValue(blocklyFieldName = "COMPASS_Y", sensor = "COMPASS", mode = "Y"), @NepoSampleValue(blocklyFieldName = "COMPASS_ANGLE", sensor = "COMPASS", mode = "ANGLE"), @NepoSampleValue(blocklyFieldName = "COMPASS_Z", sensor = "COMPASS", mode = "Z")}, containerType = "COMPASS_SENSING", category = "SENSOR", blocklyNames = {"robSensors_compass_getSample", "mbedsensors_compass_getsample", "robSensors_compass_calibrate"})
public final class CompassSensor<V> extends ExternalSensor<V> {

    public CompassSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        SensorMetaDataBean sensorMetaDataBean;
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_COMPASS_CALIBRATE) ) {
            List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
            String portName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT);
            sensorMetaDataBean =
                new SensorMetaDataBean(Jaxb2Ast.sanitizePort(portName), factory.getMode("CALIBRATE"), Jaxb2Ast.sanitizeSlot(BlocklyConstants.NO_SLOT), null);
            return new CompassSensor<>(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), sensorMetaDataBean);
        }
        SensorMetaDataBean sensorData = extractPortAndModeAndSlot(block, helper);
        return new CompassSensor<>(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), sensorData);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = super.astToBlock();
        if ( getMode().toString().equals("CALIBRATE") ) {
            jaxbDestination = new Block();
            Ast2Jaxb.setBasicProperties(this, jaxbDestination);
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, getUserDefinedPort());
        }
        return jaxbDestination;
    }
}
