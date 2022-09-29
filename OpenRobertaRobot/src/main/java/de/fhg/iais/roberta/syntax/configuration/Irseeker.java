package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "IRSEEKER", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_irseeker"})
public final class Irseeker extends ConfigurationComponent {
    private Irseeker() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
