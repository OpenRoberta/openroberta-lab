package de.fhg.iais.roberta.syntax.sensor.mbot2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(name = "JOYSTICK_SENSING", category = "SENSOR", blocklyNames = {"robSensors_joystickKeys_getSample"},
    sampleValues = {@F2M(field = "JOYSTICK_PRESSED", mode = "PRESSED")})
public final class Joystick extends Sensor implements WithUserDefinedPort {
    @NepoMutation
    public final Mutation mutation;
    @NepoField(name = BlocklyConstants.MODE)
    public final String mode;
    @NepoField(name = BlocklyConstants.SENSORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoField(name = BlocklyConstants.SLOT, value = BlocklyConstants.EMPTY_SLOT)
    public final String slot;
    @NepoHide
    public final Hide hide;

    public Joystick(BlocklyProperties properties, Mutation mutation, String mode, String port, String slot, Hide hide) {
        super(properties);
        Assert.nonEmptyString(port);
        this.mutation = mutation;
        this.mode = mode;
        this.port = port;
        this.slot = slot;
        this.hide = hide;
        setReadOnly();
    }

    public Joystick(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        this(properties, externalSensorBean.getMutation(), externalSensorBean.getMode(), externalSensorBean.getPort(), externalSensorBean.getSlot(), null);
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
