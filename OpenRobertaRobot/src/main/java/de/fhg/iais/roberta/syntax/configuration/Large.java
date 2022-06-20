package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "LARGE", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_motor_big"})
public class Large extends ConfigurationComponent {
    private Large() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
