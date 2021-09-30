package de.fhg.iais.roberta.syntax.action.mbot2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoHide;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(containerType = "CYBERPI_SET_BRIGHTNESS_ACTION")
public class LedBrightnessAction<V> extends Action<V> implements WithUserDefinedPort<V> {
    @NepoValue(name = BlocklyConstants.BRIGHTNESS, type = BlocklyType.NUMBER_INT)
    public final Expr<V> brightness;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public LedBrightnessAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> brightness, String port, Hide hide) {
        super(kind, properties, comment);
        Assert.notNull(brightness);
        Assert.notNull(port);
        this.brightness = brightness;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }
    
    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
    
}
