package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "NEURAL_NETWORK_SETUP", category = "STMT", blocklyNames = {"robActions_aifes_setupneuralnet"})
public final class NeuralNetworkSetup<V> extends Stmt<V> {
    @NepoValue(name = "NN_NUMBER_OF_CLASSES", type = BlocklyType.NUMBER)
    public Expr<V> numberOfClasses;
    @NepoValue(name = "NN_NUMBER_INPUT_NEURONS", type = BlocklyType.NUMBER)
    public Expr<V> numberInputNeurons;
    @NepoValue(name = "NN_MAX_NUMBER_OF_NEURONS", type = BlocklyType.NUMBER)
    public Expr<V> maxNumberOfNeurons;

    public NeuralNetworkSetup(
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        Expr<V> numberOfClasses,
        Expr<V> numberInputNeurons,
        Expr<V> maxNumberOfNeurons) {
        super(properties, comment);
        this.numberOfClasses = numberOfClasses;
        this.numberInputNeurons = numberInputNeurons;
        this.maxNumberOfNeurons = maxNumberOfNeurons;
        setReadOnly();
    }

}
