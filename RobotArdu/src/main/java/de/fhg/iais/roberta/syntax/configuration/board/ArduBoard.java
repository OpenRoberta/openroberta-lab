package de.fhg.iais.roberta.syntax.configuration.board;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ARDU", category = "CONFIGURATION_BOARD",
    blocklyNames = {"robBrick_motor_ardu"})
public final class ArduBoard extends ConfigurationComponent {
    private ArduBoard() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
