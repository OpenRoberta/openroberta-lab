package de.fhg.iais.roberta.syntax.sensor.nao;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(sampleValues = {@F2M(field = "FSR_VALUE", mode = "VALUE")}, name = "FSR_SENSOR", category = "SENSOR", blocklyNames = {"robSensors_fsr_getSample"})
@NepoExternalSensor
public final class FsrSensor<V> extends ExternalSensor<V> {
    public FsrSensor(BlocklyBlockProperties properties, BlocklyComment comment, ExternalSensorBean externalSensorBean) {
        super(properties, comment, externalSensorBean);
        setReadOnly();
    }

}
