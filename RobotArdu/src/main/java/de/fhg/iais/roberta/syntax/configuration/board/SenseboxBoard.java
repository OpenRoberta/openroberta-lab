package de.fhg.iais.roberta.syntax.configuration.board;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "SENSEBOX", category = "CONFIGURATION_BOARD",
    blocklyNames = {"robBrick_senseBox-Brick"})
public final class SenseboxBoard extends ConfigurationComponent {
    private SenseboxBoard() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
