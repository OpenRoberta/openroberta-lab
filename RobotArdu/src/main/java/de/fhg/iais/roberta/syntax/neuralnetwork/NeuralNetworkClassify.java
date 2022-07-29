package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "EXPR", blocklyNames = {"robActions_aifes_classify"}, name = "NEURAL_NETWORK_CLASSIFY")
public final class NeuralNetworkClassify extends Expr {
    @NepoValue(name = "NN_CLASS_PROBABILITIES", type = BlocklyType.ARRAY_NUMBER)
    public final Expr probabilities;

    public NeuralNetworkClassify(BlocklyProperties properties, Expr probabilities) {
        super(properties);
        this.probabilities = probabilities;
        setReadOnly();
    }
}
