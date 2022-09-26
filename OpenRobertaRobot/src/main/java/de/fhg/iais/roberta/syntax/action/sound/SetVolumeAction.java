package de.fhg.iais.roberta.syntax.action.sound;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "SET_VOLUME_ACTION", category = "ACTOR", blocklyNames = {"robActions_play_setVolume"})
public final class SetVolumeAction extends Action {

    @NepoField(name = "ACTORPORT", value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    @NepoValue(name = "VOLUME", type = BlocklyType.NUMBER_INT)
    public final Expr volume;

    @NepoHide
    public final Hide hide;

    public SetVolumeAction(BlocklyProperties properties, String port, Expr volume, Hide hide) {
        super(properties);
        Assert.isTrue(volume != null && volume.isReadOnly());
        this.port = port;
        this.volume = volume;
        this.hide = hide;
        setReadOnly();
    }
}
