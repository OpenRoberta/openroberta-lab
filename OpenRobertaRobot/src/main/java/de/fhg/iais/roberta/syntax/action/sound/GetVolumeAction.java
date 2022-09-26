package de.fhg.iais.roberta.syntax.action.sound;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(name = "GET_VOLUME_ACTION", category = "ACTOR", blocklyNames = {"robActions_play_getVolume"}, blocklyType = BlocklyType.NUMBER)
public final class GetVolumeAction extends Action {

    @NepoField(name = "ACTORPORT", value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    @NepoHide
    public final Hide hide;

    public GetVolumeAction(BlocklyProperties properties, String port, Hide hide) {
        super(properties);
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }
}
