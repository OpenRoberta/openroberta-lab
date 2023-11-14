package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ENCODERMOTOR", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_encodermotor_txt4"})
public final class EncoderMotor extends ConfigurationComponent {
    private EncoderMotor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
