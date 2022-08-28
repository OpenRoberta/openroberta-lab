package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "FLAMESENSOR", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_flame"})
public class Flamesensor extends ConfigurationComponent {
    private Flamesensor() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
