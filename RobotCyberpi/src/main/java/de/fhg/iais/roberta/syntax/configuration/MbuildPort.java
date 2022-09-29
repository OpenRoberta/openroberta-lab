package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "MBUILD_PORT", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_mbuild_port"})
public final class MbuildPort extends ConfigurationComponent {
    private MbuildPort() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
