package de.fhg.iais.roberta.syntax.action.display;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoHide;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robActions_display_text</b> block
 */
@NepoPhrase(containerType = "SHOW_TEXT_ACTION")
public class ShowTextAction<V> extends Action<V> {
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

    public ShowTextAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> msg, Expr<V> column, Expr<V> row, String port, Hide hide) {
        super(kind, properties, comment);
        Assert.isTrue((msg != null) && (column != null) && (row != null));
        this.msg = msg;
        this.x = column;
        this.y = row;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    public static <V> ShowTextAction<V> make(Expr<V> msg, Expr<V> column, Expr<V> row, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ShowTextAction<>(BlockTypeContainer.getByName("SHOW_TEXT_ACTION"),properties,comment,msg,column,row,port, null);
    }
}