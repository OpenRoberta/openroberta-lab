package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "EXPR", blocklyNames = {"robSensors_get_bias"}, name = "NN_GET_BIAS")
public final class NNGetBias extends Expr {
    @NepoField(name = BlocklyConstants.NAME)
    public final String name;

    public NNGetBias(BlocklyProperties properties, String name) {
        super(properties);
        this.name = name;
        setReadOnly();
    }

}
