package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "SENSEBOX", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_senseBox-Brick"})
public class Sensebox extends ConfigurationComponent {
    private Sensebox() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
