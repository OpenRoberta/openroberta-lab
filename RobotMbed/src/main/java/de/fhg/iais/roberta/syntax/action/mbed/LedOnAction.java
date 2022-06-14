package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(category = "ACTOR", blocklyNames = {"mbedActions_leds_on"}, containerType = "LED_ON_ACTION")
public class LedOnAction<V> extends Action<V> implements WithUserDefinedPort<V> {
    @NepoValue(name = BlocklyConstants.COLOR, type = BlocklyType.COLOR)
    public final Expr<V> ledColor;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public LedOnAction(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> ledColor, String port, Hide hide) {
        super(properties, comment);
        Assert.isTrue(ledColor != null && ledColor.isReadOnly());
        Assert.nonEmptyString(port);
        this.ledColor = ledColor;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    public static <V> LedOnAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> ledColor, String port, Hide hide) {
        return new LedOnAction<>(properties, comment, ledColor, port, hide);
    }

    public String getUserDefinedPort() {
        return this.port;
    }

    public Expr<V> getLedColor() {
        return this.ledColor;
    }
}
