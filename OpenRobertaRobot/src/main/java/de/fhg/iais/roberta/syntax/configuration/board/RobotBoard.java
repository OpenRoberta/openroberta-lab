package de.fhg.iais.roberta.syntax.configuration.board;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ROBOT", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_robot"})
public final class RobotBoard extends ConfigurationComponent {
    private RobotBoard() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
