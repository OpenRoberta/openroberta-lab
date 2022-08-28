package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "NEURAL_NETWORK_SETUP", category = "STMT", blocklyNames = {"robActions_aifes_setupneuralnet"})
public final class NeuralNetworkSetup extends Stmt {
    @NepoValue(name = "NN_NUMBER_OF_CLASSES", type = BlocklyType.NUMBER)
    public Expr numberOfClasses;
    @NepoValue(name = "NN_NUMBER_INPUT_NEURONS", type = BlocklyType.NUMBER)
    public Expr numberInputNeurons;
    @NepoValue(name = "NN_MAX_NUMBER_OF_NEURONS", type = BlocklyType.NUMBER)
    public Expr maxNumberOfNeurons;

    public NeuralNetworkSetup(
        BlocklyProperties properties,
        
        Expr numberOfClasses,
        Expr numberInputNeurons,
        Expr maxNumberOfNeurons) {
        super(properties);
        this.numberOfClasses = numberOfClasses;
        this.numberInputNeurons = numberInputNeurons;
        this.maxNumberOfNeurons = maxNumberOfNeurons;
        setReadOnly();
    }

}
