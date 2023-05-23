package de.fhg.iais.roberta.syntax.configuration.bus;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "MBUILD_PORT", category = "CONFIGURATION_BUS",
    blocklyNames = {"robConf_mbuild_port"})
public final class MbuildPortBus extends ConfigurationComponent {
    private MbuildPortBus() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
