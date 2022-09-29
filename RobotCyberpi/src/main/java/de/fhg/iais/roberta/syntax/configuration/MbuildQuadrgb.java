package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "MBUILD_QUADRGB", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_mbuild_quadrgb"})
public final class MbuildQuadrgb extends ConfigurationComponent {
    private MbuildQuadrgb() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
