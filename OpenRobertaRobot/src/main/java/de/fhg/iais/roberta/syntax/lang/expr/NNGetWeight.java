package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "EXPR", blocklyNames = {"robSensors_get_weight"}, name = "NN_GET_WEIGHT")
public final class NNGetWeight<V> extends Expr<V> {
    @NepoField(name = "FROM")
    public final String from;

    @NepoField(name = "TO")
    public final String to;

    public NNGetWeight(BlocklyProperties properties, String from, String to) {
        super(properties);
        this.from = from;
        this.to = to;
        setReadOnly();
    }

}
