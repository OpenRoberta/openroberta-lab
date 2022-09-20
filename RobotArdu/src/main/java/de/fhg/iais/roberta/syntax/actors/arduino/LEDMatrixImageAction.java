package de.fhg.iais.roberta.syntax.actors.arduino;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "LED_MATRIX_IMAGE_ACTION", category = "ACTOR", blocklyNames = {"mBotActions_display_image"})
public final class LEDMatrixImageAction extends Action {

    @NepoMutation(fieldName = BlocklyConstants.TYPE)
    public final Mutation mutation;

    @NepoField(name = "TYPE")
    public final String displayImageMode;

    @NepoField(name = "ACTORPORT")
    public final String port;

    @NepoValue(name = "VALUE", type = BlocklyType.STRING)
    public final Expr valuesToDisplay;

    public LEDMatrixImageAction(BlocklyProperties properties, Mutation mutation, String displayImageMode, String port, Expr valuesToDisplay) {
        super(properties);
        Assert.isTrue(port != null && displayImageMode != null && valuesToDisplay != null);
        this.mutation = mutation;
        this.displayImageMode = displayImageMode;
        this.port = port;
        this.valuesToDisplay = valuesToDisplay;
        setReadOnly();
    }
}
