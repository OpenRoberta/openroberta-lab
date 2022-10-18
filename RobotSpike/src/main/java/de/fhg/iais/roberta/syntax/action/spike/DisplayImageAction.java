package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_display_image"}, name = "DISPLAY_IMAGE_ACTION")
public final class DisplayImageAction extends ActionWithoutUserChosenName {
    @NepoMutation(fieldName = "TYPE")
    public final Mutation mutation;
    @NepoField(name = "TYPE")
    public final String displayImageMode;
    @NepoValue(name = "VALUE", type = BlocklyType.STRING)
    public final Expr valuesToDisplay;

    public DisplayImageAction(BlocklyProperties properties, Mutation mutation, String displayImageMode, Expr valuesToDisplay, Hide hide) {
        super(properties, hide);
        Assert.isTrue(displayImageMode != null && valuesToDisplay != null);
        this.mutation = mutation;
        this.displayImageMode = displayImageMode;
        this.valuesToDisplay = valuesToDisplay;
        setReadOnly();
    }

}