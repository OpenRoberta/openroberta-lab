package de.fhg.iais.roberta.syntax.action.ev3;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "SHOW_PICTURE_ACTION", category = "ACTOR", blocklyNames = {"robActions_display_picture", "robActions_display_picture_new"})
public final class ShowPictureAction extends Action {

    @NepoField(name = "PICTURE")
    public final String pic;

    @NepoValue(name = "X", type = BlocklyType.NUMBER_INT)
    public final Expr x;

    @NepoValue(name = "Y", type = BlocklyType.NUMBER_INT)
    public final Expr y;

    public ShowPictureAction(BlocklyProperties properties, String pic, Expr x, Expr y) {
        super(properties);
        Assert.isTrue(pic != null && x != null && y != null);
        this.pic = pic;
        this.x = x;
        this.y = y;
        setReadOnly();
    }
}
