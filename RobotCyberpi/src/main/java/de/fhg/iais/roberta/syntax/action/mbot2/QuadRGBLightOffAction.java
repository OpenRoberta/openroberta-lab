package de.fhg.iais.roberta.syntax.action.mbot2;


import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_quadRGB_led_off"}, name = "QUADRGB_LIGHT_OFF_ACTION")
public final class QuadRGBLightOffAction extends Action implements WithUserDefinedPort {
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    public QuadRGBLightOffAction(BlocklyProperties properties, String port) {
        super(properties);
        Assert.nonEmptyString(port);
        this.port = port;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
