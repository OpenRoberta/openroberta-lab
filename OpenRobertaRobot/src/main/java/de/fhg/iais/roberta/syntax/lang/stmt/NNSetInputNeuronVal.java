package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_set_inputneuron_val"}, name = "NN_SET_INPUT_NEURON_STMT")
public final class NNSetInputNeuronVal extends Stmt {
    @NepoField(name = BlocklyConstants.NAME)
    public final String name;
    @NepoValue(name = BlocklyConstants.VALUE, type = BlocklyType.NUMBER)
    public final Expr value;

    public NNSetInputNeuronVal(BlocklyProperties properties, String name, Expr value) {
        super(properties);
        Assert.isTrue(value.isReadOnly() && value != null);
        this.name = name;
        this.value = value;
        setReadOnly();
    }

}
