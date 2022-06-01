package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(containerType = "FOURDIGITDISPLAY_SHOW_ACTION")
public class FourDigitDisplayShowAction<V> extends Action<V> {
    @NepoValue(name = BlocklyConstants.VALUE, type = BlocklyType.NUMBER_INT)
    public final Expr<V> value;
    @NepoValue(name = BlocklyConstants.POSITION, type = BlocklyType.NUMBER_INT)
    public final Expr<V> position;
    @NepoValue(name = BlocklyConstants.COLON, type = BlocklyType.BOOLEAN)
    public final Expr<V> colon;

    public FourDigitDisplayShowAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> value, Expr<V> position, Expr<V> colon) {
        super(kind, properties, comment);
        Assert.isTrue(value != null && position != null && colon != null);
        this.value = value;
        this.position = position;
        this.colon = colon;
        setReadOnly();
    }

    public static <V> FourDigitDisplayShowAction<V> make(
        Expr<V> value,
        Expr<V> position,
        Expr<V> colon,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new FourDigitDisplayShowAction<>(BlockTypeContainer.getByName("FOURDIGITDISPLAY_SHOW_ACTION"), properties, comment, value, position, colon);
    }

    public Expr<V> getValue() {
        return this.value;
    }

    public Expr<V> getPosition() {
        return this.position;
    }

    public Expr<V> getColon() {
        return this.colon;
    }
}
