package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_outputneuron_wo_var"}, name = "NN_OUTPUT_NEURON_WO_VAR_STMT")
public final class NNOutputNeuronWoVarStmt<V> extends Stmt<V> {
    @NepoField(name = "NAME")
    public final String name;

    public NNOutputNeuronWoVarStmt(BlocklyProperties properties, String name) {
        super(properties);
        this.name = name;
        setReadOnly();
    }

}
