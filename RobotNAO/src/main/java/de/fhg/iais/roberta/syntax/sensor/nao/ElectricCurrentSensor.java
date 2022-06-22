package de.fhg.iais.roberta.syntax.sensor.nao;

import org.checkerframework.checker.units.qual.degrees;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

/**
 * This class represents the <b>naoActions_walk</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for making
 * the robot walk for a distance.<br/>
 * <br/>
 * The client must provide the {@link joint} and {@link degrees} (direction and distance to walk).
 */
@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName="ELECTRICCURRENT_VALUE",sensor="ELECTRIC_CURRENT",mode="VALUE")}, containerType = "ELECTRIC_CURRENT", category = "SENSOR", blocklyNames = {"robSensors_electriccurrent_getSample"})
@NepoExternalSensor
public final class ElectricCurrentSensor<V> extends ExternalSensor<V> {

    public ElectricCurrentSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}
