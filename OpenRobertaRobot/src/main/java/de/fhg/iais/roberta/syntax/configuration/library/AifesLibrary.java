package de.fhg.iais.roberta.syntax.configuration.library;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "AIFES", category = "CONFIGURATION_LIBRARY",
    blocklyNames = {"robConf_aifes"})
public class AifesLibrary extends ConfigurationComponent {

    public AifesLibrary() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}