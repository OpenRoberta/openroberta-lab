package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.mode.action.nao.Led;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

/**
 * This class represents the <b>naoActions_ledReset</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * resetting all LEDs to their standard state.<br/>
 * <br/>
 */
@NepoPhrase(name = "LED_RESET", category = "ACTOR", blocklyNames = {"naoActions_ledReset"})
public final class LedReset<V> extends Action<V> {
    @NepoField(name = "LED")
    public final Led led;

    public LedReset(BlocklyBlockProperties properties, BlocklyComment comment, Led led) {
        super(properties, comment);
        this.led = led;
        setReadOnly();
    }

    public Led getLed() {
        return this.led;
    }
}
