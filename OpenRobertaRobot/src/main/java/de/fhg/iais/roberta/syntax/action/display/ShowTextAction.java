package de.fhg.iais.roberta.syntax.action.display;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_display_text", "robActions_display_text_i2c", "robActions_display_text_oledssd1306i2c"}, name = "SHOW_TEXT_ACTION")
public final class ShowTextAction<V> extends Action<V> {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.STRING)
    public final Expr<V> msg;
    @NepoValue(name = BlocklyConstants.COL, type = BlocklyType.NUMBER_INT)
    public final Expr<V> x;
    @NepoValue(name = BlocklyConstants.ROW, type = BlocklyType.NUMBER_INT)
    public final Expr<V> y;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public ShowTextAction(
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        Expr<V> msg,
        Expr<V> column,
        Expr<V> row,
        String port,
        Hide hide) {
        super(properties, comment);
        Assert.isTrue((msg != null) && (column != null) && (row != null));
        this.msg = msg;
        this.x = column;
        this.y = row;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

}