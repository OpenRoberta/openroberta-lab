package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "OTHER", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_actor"})
public final class Other extends ConfigurationComponent {
    private Other() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
