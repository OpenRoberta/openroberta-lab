package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "MBUILD_ULTRASONIC2", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_mbuild_ultrasonic2"})
public final class MbuildUltrasonic2 extends ConfigurationComponent {
    private MbuildUltrasonic2() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
