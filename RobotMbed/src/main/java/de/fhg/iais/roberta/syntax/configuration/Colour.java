package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "COLOUR", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_colourtcs3472", "robConf_colour"})
public final class Colour extends ConfigurationComponent {
    private Colour() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
