package de.fhg.iais.roberta.syntax.actors.arduino.sensebox;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "PLOT_CLEAR_ACTION", category = "ACTOR", blocklyNames = {"robactions_plot_clear"})
public final class PlotClearAction extends Action {
    @NepoField(name = "ACTORPORT", value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    public PlotClearAction(BlocklyProperties properties, String port) {
        super(properties);
        this.port = port;
        this.setReadOnly();
    }


}
