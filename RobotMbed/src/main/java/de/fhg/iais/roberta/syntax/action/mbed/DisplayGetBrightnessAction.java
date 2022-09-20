package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "DISPLAY_GET_BRIGHTNESS", category = "ACTOR", blocklyNames = {"mbedActions_display_getBrightness"}, blocklyType = BlocklyType.NUMBER)
public final class DisplayGetBrightnessAction extends Action {

    public DisplayGetBrightnessAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
