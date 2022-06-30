package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoExpr(category = "EXPR", blocklyNames = {"robSensors_get_outputneuron_val"}, name = "NN_GET_OUTPUT_NEURON_VAL", blocklyType = BlocklyType.NUMBER)
public final class NNGetOutputNeuronVal<V> extends Expr<V> {
    @NepoField(name = "NAME")
    public final String name;

    public NNGetOutputNeuronVal(BlocklyBlockProperties properties, BlocklyComment comment, String name) {
        super(properties, comment);
        this.name = name;
        setReadOnly();
    }

    public static <V> NNGetOutputNeuronVal<V> make(BlocklyBlockProperties properties, BlocklyComment comment, String name) {
        return new NNGetOutputNeuronVal<>(properties, comment, name);
    }

    public static <V> NNGetOutputNeuronVal<V> make(String name) {
        return new NNGetOutputNeuronVal<>(BlocklyBlockProperties.make("NN_GET_OUTPUT_NEURON_VAL", "1"), null, name);
    }

    public String getName() {
        return this.name;
    }
}
