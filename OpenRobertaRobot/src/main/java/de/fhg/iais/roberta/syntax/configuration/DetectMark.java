package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "DETECT_MARK", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"naoSensors_naoMark"})
public final class DetectMark extends ConfigurationComponent {
    private DetectMark() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
