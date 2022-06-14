package de.fhg.iais.roberta.syntax.action.display;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>robActions_display_clear</b> block from Blockly into the AST (abstract syntax tree).
 */
@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_display_clear", "mbedActions_display_clear", "robActions_display_clear_oledssd1306i2c", "robActions_display_clear_i2c", "mBotActions_display_clear"}, containerType = "CLEAR_DISPLAY_ACTION")
public final class ClearDisplayAction<V> extends Action<V> {
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public ClearDisplayAction(BlocklyBlockProperties properties, BlocklyComment comment, String port, Hide hide) {
        super(properties, comment);
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    // TODO: remove, if Transformer is better engineered
    public static <V> ClearDisplayAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment, String port) {
        return new ClearDisplayAction(properties, comment, port, null);
    }

    public String getPort() {
        return this.port;
    }
}
