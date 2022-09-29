package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "HT_COLOR", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_HiTechnic_colour", "robBrick_htcolour"})
public final class HtColor extends ConfigurationComponent {
    private HtColor() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
