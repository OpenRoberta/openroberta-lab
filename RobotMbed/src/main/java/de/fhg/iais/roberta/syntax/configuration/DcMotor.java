package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "DCMOTOR", category = "CONFIGURATION_BLOCK", blocklyNames = {"robConf_dcmotor"})
public class DcMotor extends ConfigurationComponent {
    private DcMotor() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
