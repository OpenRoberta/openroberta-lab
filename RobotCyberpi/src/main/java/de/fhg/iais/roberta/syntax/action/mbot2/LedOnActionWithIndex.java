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

@NepoPhrase(containerType = "CYBERPI_LED_ON_ACTION")
public class LedOnActionWithIndex<V> extends Action<V> implements WithUserDefinedPort<V> {
    @NepoValue(name = BlocklyConstants.COLOR, type = BlocklyType.COLOR)
    public final Expr<V> color;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoField(name = BlocklyConstants.LED, value = BlocklyConstants.EMPTY_PORT)
    public final String led;
    @NepoHide
    public final Hide hide;

    public LedOnActionWithIndex(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> color, String port, String led, Hide hide) {
        super(kind, properties, comment);
        Assert.notNull(color);
        Assert.nonEmptyString(port);
        Assert.notNull(led);
        this.hide = hide;
        this.color = color;
        this.port = port;
        this.led = led;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
