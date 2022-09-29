package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "FOURDIGITDISPLAY", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_fourdigitdisplay"})
public final class Fourdigitdisplay extends ConfigurationComponent {
    private Fourdigitdisplay() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
