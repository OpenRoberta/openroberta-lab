package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(category = "EXPR", blocklyNames = {"robSensors_get_outputneuron_val"}, name = "NN_GET_OUTPUT_NEURON_VAL", blocklyType = BlocklyType.NUMBER)
public final class NNGetOutputNeuronVal extends Expr {
    @NepoField(name = "NAME")
    public final String name;

    public NNGetOutputNeuronVal(BlocklyProperties properties, String name) {
        super(properties);
        this.name = name;
        setReadOnly();
    }

}
