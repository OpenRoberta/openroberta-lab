package de.fhg.iais.roberta.syntax.sensors.arduino.mbot;

import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

//TODO: check whether this sensor is used. If not, can be deleted...
@NepoExpr(name = "FLAMESENSOR_GET_SAMPLE", category = "SENSOR", blocklyNames = {"robSensors_flame_getSample"}, blocklyType = BlocklyType.NUMBER)
public final class FlameSensor extends Sensor {

    @NepoField(name = "SENSORPORT")
    public final String port;

    public FlameSensor(BlocklyProperties properties, String port) {
        super(properties);
        this.port = port;
        setReadOnly();
    }
}
