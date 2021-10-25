package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoHide;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(containerType = "LED_ON_ACTION")
public class LedOnAction<V> extends Action<V> implements WithUserDefinedPort {
    @NepoValue(name = BlocklyConstants.COLOR, type = BlocklyType.COLOR)
    public final Expr<V> ledColor;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public LedOnAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> ledColor, String port, Hide hide) {
        super(kind, properties, comment);
        Assert.isTrue(ledColor != null && ledColor.isReadOnly());
        Assert.nonEmptyString(port);
        this.ledColor = ledColor;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LedOnAction}. This instance is read only and can not be modified.
     *
     * @param ledColor {@link ColorConst} color of the led; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LedOnAction}
     */
    public static <V> LedOnAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> ledColor, String port, Hide hide) {
        return new LedOnAction(BlockTypeContainer.getByName("LED_ON_ACTION"), properties, comment, ledColor, port, hide);
    }

    public String getUserDefinedPort() {
        return this.port;
    }

    public Expr<V> getLedColor() {
        return this.ledColor;
    }
}
