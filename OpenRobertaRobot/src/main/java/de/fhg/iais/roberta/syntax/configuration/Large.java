package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LARGE", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_motor_big"})
public final class Large extends ConfigurationComponent {
    private Large() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
