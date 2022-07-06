package de.fhg.iais.roberta.syntax.sensor.nao;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(sampleValues = {@F2M(field = "ELECTRICCURRENT_VALUE", mode = "VALUE")}, name = "ELECTRIC_CURRENT", category = "SENSOR", blocklyNames = {"robSensors_electriccurrent_getSample"})
@NepoExternalSensor
public final class ElectricCurrentSensor<V> extends ExternalSensor<V> {

    public ElectricCurrentSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
