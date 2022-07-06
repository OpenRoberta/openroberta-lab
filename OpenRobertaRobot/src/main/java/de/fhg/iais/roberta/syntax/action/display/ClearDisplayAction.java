package de.fhg.iais.roberta.syntax.action.display;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>robActions_display_clear</b> block from Blockly into the AST (abstract syntax tree).
 */
@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_display_clear", "mbedActions_display_clear", "robActions_display_clear_oledssd1306i2c", "robActions_display_clear_i2c", "mBotActions_display_clear"}, name = "CLEAR_DISPLAY_ACTION")
public final class ClearDisplayAction extends Action {
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public ClearDisplayAction(BlocklyProperties properties, String port, Hide hide) {
        super(properties);
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }
}
