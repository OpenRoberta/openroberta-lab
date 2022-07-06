package de.fhg.iais.roberta.syntax.sensor.mbot2;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(name = "QUAD_COLOR_SENSING", category = "SENSOR", blocklyNames = {"robSensors_line_getSample", "robSensors_quadrgb_getSample"},
    sampleValues = {@F2M(field = "QUADRGB_COLOUR", mode = "COLOUR"), @F2M(field = "QUADRGB_AMBIENTLIGHT", mode = "AMBIENTLIGHT"), @F2M(field = "QUADRGB_LINE", mode = "LINE")})
public final class QuadRGBSensor extends Sensor implements WithUserDefinedPort {
    @NepoMutation(fieldName = BlocklyConstants.MODE)
    public final Mutation mutation;
    @NepoField(name = BlocklyConstants.MODE)
    public final String mode;
    @NepoField(name = BlocklyConstants.SENSORPORT)
    public final String sensorPort;
    @NepoField(name = BlocklyConstants.SLOT)
    public final String slot;

    public QuadRGBSensor(BlocklyProperties properties, Mutation mutation, String mode, String sensorPort, String slot) {
        super(properties);
        this.mutation = mutation;
        this.mode = mode;
        this.sensorPort = sensorPort;
        this.slot = slot;
        setReadOnly();
    }

    public QuadRGBSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        this(properties, externalSensorBean.getMutation(), externalSensorBean.getMode(), externalSensorBean.getPort(), externalSensorBean.getSlot());
    }

    @Override
    public String getUserDefinedPort() {
        return this.sensorPort;
    }

}
