package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "HTS221", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_hts221"})
public final class Hts221 extends ConfigurationComponent {
    private Hts221() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
