package de.fhg.iais.roberta.syntax.sensor.nao;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

/**
 * This class represents the <b>naoSensors_detectFace</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * detecting a face previously saved in NAOs database.<br/>
 * <br/>
 */
@NepoExpr(name = "DETECT_FACE", category = "SENSOR", blocklyNames = {"robSensors_detectface_getSample"},
    sampleValues = {@F2M(field = "DETECTFACE_NAMEONE", mode = "NAMEONE")})
@NepoExternalSensor
public final class DetectFaceSensor<V> extends ExternalSensor<V> {

    public DetectFaceSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}
