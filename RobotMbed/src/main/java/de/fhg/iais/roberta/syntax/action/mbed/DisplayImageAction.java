package de.fhg.iais.roberta.syntax.action.mbed;

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

@NepoPhrase(name = "DISPLAY_IMAGE_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_display_image"})
public final class DisplayImageAction extends Action {

    @NepoMutation(fieldName = "TYPE")
    public final Mutation mutation;

    @NepoField(name = "TYPE")
    public final String displayImageMode;

    @NepoValue(name = "VALUE", type = BlocklyType.STRING)
    public final Expr valuesToDisplay;

    public DisplayImageAction(BlocklyProperties properties, Mutation mutation, String displayImageMode, Expr valuesToDisplay) {
        super(properties);
        Assert.isTrue(displayImageMode != null && valuesToDisplay != null);
        this.mutation = mutation;
        this.displayImageMode = displayImageMode;
        this.valuesToDisplay = valuesToDisplay;
        setReadOnly();
    }
}
