package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "EXPR", blocklyNames = {"robSensors_get_bias"}, name = "NN_GET_BIAS")
public final class NNGetBias<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.NAME)
    public final String name;

    public NNGetBias(BlocklyBlockProperties properties, BlocklyComment comment, String name) {
        super(properties, comment);
        this.name = name;
        setReadOnly();
    }

}
