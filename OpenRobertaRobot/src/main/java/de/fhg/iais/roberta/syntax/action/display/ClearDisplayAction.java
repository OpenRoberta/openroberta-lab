package de.fhg.iais.roberta.syntax.action.display;

import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;

/**
 * This class represents the <b>robActions_display_clear</b> block from Blockly into the AST (abstract syntax tree).
 */
@NepoPhrase(containerType = "CLEAR_DISPLAY_ACTION")
public final class ClearDisplayAction<V> extends Action<V> {
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    public ClearDisplayAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String port) {
        super(kind, properties, comment);
        this.port = port;
        setReadOnly();
    }

    // TODO: remove, if Transformer is better engineered
    public static <V> ClearDisplayAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment, String port) {
        return new ClearDisplayAction(BlockTypeContainer.getByName("CLEAR_DISPLAY_ACTION"), properties, comment, port);
    }
}
