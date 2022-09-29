package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "PIN_VALUE", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robSensors_pin_getSample"})
public final class PinValue extends ConfigurationComponent {
    private PinValue() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
