package de.fhg.iais.roberta.syntax.actors.arduino.sensebox;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "PLOT_CLEAR_ACTION", category = "ACTOR", blocklyNames = {"robactions_plot_clear"})
public final class PlotClearAction<V> extends Action<V> {
    @NepoField(name = "ACTORPORT", value = BlocklyConstants.EMPTY_PORT)
    public final String port;

     public PlotClearAction(BlocklyBlockProperties properties, BlocklyComment comment, String port) {
        super(properties, comment);
        this.port = port;
        this.setReadOnly();
    }

    public static <V> PlotClearAction<V> make(String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PlotClearAction<>(properties, comment, port);
    }

    public String getPort() {
        return this.port;
    }

}
