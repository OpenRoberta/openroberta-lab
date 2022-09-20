package de.fhg.iais.roberta.syntax.actors.arduino.sensebox;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "PLOT_POINT_ACTION", category = "ACTOR", blocklyNames = {"robActions_plot_point"})
public final class PlotPointAction extends Action {

    @NepoField(name = "ACTORPORT")
    public final String port;

    @NepoValue(name = "VALUE", type = BlocklyType.NUMBER_INT)
    public final Expr value;

    @NepoValue(name = "TICKMARK", type = BlocklyType.NUMBER_INT)
    public final Expr tickmark;

    public PlotPointAction(BlocklyProperties properties, String port, Expr value, Expr tickmark) {
        super(properties);
        this.port = port;
        this.value = value;
        this.tickmark = tickmark;
        this.setReadOnly();
    }
}
