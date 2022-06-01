package de.fhg.iais.roberta.syntax.sensors.arduino.bob3;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoMutation;
import de.fhg.iais.roberta.transformer.NepoPhrase;

/**
 * This class represents the <b>robSensors_code_getSample</b> Blockly block<br/>
 */
@NepoPhrase(containerType = "BOB3_CODEPAD")
public class CodePadSensor<V> extends Sensor<V> {
    @NepoMutation(fieldName = BlocklyConstants.MODE)
    public final Mutation mutation;
    @NepoField(name = BlocklyConstants.MODE)
    public final String mode;
    @NepoField(name = BlocklyConstants.SENSORPORT)
    public final String sensorPort;
    @NepoField(name = BlocklyConstants.SLOT)
    public final String slot;

    public CodePadSensor(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Mutation mutation, String mode, String sensorPort, String slot) {
        super(kind, properties, comment);
        this.mutation = mutation;
        this.mode = mode;
        this.sensorPort = sensorPort;
        this.slot = slot;
        setReadOnly();
    }
}
