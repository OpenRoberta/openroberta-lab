package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "DIFFERENTIALDRIVE", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_differentialdrive"})
public final class DifferentialDriveActor extends ConfigurationComponent {
    private DifferentialDriveActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
