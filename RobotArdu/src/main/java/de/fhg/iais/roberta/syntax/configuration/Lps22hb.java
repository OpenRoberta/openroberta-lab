package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "LPS22HB", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_lps22hb"})
public class Lps22hb extends ConfigurationComponent {
    private Lps22hb() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
