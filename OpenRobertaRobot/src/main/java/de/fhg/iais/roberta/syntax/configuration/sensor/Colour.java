package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "COLOUR", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_colourtcs3472", "robConf_colour"})
public final class Colour extends ConfigurationComponent {
    private Colour() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
