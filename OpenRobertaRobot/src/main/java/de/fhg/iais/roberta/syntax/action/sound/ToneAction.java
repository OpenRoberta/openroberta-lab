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

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_play_tone", "mbedActions_play_tone"}, name = "TONE_ACTION")
public final class ToneAction extends Action {
    @NepoValue(name = BlocklyConstants.FREQUENCE, type = BlocklyType.NUMBER_INT)
    public final Expr frequency;
    @NepoValue(name = BlocklyConstants.DURATION, type = BlocklyType.NUMBER_INT)
    public final Expr duration;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public ToneAction(BlocklyProperties properties, Expr frequency, Expr duration, String port, Hide hide) {
        super(properties);
        Assert.isTrue(frequency.isReadOnly() && duration.isReadOnly() && (frequency != null) && (duration != null));
        this.frequency = frequency;
        this.duration = duration;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }


}
