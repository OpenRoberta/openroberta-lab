package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "CALLIBOT", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_callibot"})
public final class Board extends ConfigurationComponent {
    private Board() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
