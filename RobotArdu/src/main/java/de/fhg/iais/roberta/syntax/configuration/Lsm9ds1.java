package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "LSM9DS1", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_lsm9ds1"})
public class Lsm9ds1 extends ConfigurationComponent {
    private Lsm9ds1() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
