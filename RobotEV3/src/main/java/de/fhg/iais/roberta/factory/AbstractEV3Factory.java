package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.mode.action.ev3.ShowPicture;
import de.fhg.iais.roberta.util.PluginProperties;

public abstract class AbstractEV3Factory extends AbstractRobotFactory {

    public AbstractEV3Factory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    public IShowPicture getShowPicture(String picture) {
        return BlocklyDropdownFactoryHelper.getModeValue(picture, ShowPicture.class);
    }
}
