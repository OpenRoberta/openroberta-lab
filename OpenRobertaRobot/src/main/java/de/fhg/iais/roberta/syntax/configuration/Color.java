package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "COLOR", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_colour"})
public final class Color extends ConfigurationComponent {
    private Color() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
