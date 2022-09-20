package de.fhg.iais.roberta.syntax.sensors.arduino.bob3;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "SENSOR", blocklyNames = {"bob3Sensors_getCode", "robSensors_code_getSample"}, name = "BOB3_CODEPAD", blocklyType = BlocklyType.NUMBER)
public final class CodePadSensor extends Sensor {

    @NepoMutation(fieldName = BlocklyConstants.MODE)
    public final Mutation mutation;

    @NepoField(name = BlocklyConstants.MODE)
    public final String mode;

    @NepoField(name = BlocklyConstants.SENSORPORT)
    public final String sensorPort;

    @NepoField(name = BlocklyConstants.SLOT)
    public final String slot;

    public CodePadSensor(BlocklyProperties properties, Mutation mutation, String mode, String sensorPort, String slot) {
        super(properties);
        this.mutation = mutation;
        this.mode = mode;
        this.sensorPort = sensorPort;
        this.slot = slot;
        setReadOnly();
    }
}
