package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "COLOUR_COLOUR", sensor = "COLOUR", mode = "COLOUR"), @NepoSampleValue(blocklyFieldName = "COLOUR_AMBIENTLIGHT", sensor = "COLOUR", mode = "AMBIENTLIGHT"), @NepoSampleValue(blocklyFieldName = "COLOUR_LIGHT", sensor = "COLOUR", mode = "LIGHT")}, category = "SENSOR", blocklyNames = {"robsensors_colourtcs3472_getsample", "sim_colour_getSample", "robSensors_colour_getSample"}, containerType = "COLOR_SENSING")
@NepoExternalSensor()
public final class ColorSensor<V> extends ExternalSensor<V> {

    public ColorSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }
}
