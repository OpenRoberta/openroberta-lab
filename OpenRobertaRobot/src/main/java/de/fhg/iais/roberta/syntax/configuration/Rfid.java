package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "RFID", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_rfid", "robBrick_rfide"})
public class Rfid extends ConfigurationComponent {
    private Rfid() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
