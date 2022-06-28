package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoBasic(name = "NN_STEP_STMT", category = "STMT", blocklyNames = {"robactions_nnstep"})
public final class NNStepStmt<V> extends Stmt<V> {
    public final StmtList<V> ioNeurons;

    public NNStepStmt(BlocklyBlockProperties properties, BlocklyComment comment, StmtList<V> ioNeurons) {
        super(properties, comment);
        this.ioNeurons = ioNeurons;
        setReadOnly();
    }

    public StmtList<V> getIoNeurons() {
        return this.ioNeurons;
    }

    public List<Stmt<V>> getInputNeurons() {
        final List<Stmt<V>> inputNeurons = new ArrayList<>();
        for ( Stmt<V> ioNeuron : ioNeurons.get() ) {
            if ( ioNeuron.hasName("NN_INPUT_NEURON_STMT") ) {
                inputNeurons.add(ioNeuron);
            }
        }
        return inputNeurons;
    }

    /**
     * @return the output neuron statements
     */
    public List<Stmt<V>> getOutputNeurons() {
        final List<Stmt<V>> outputNeurons = new ArrayList<>();
        for ( Stmt<V> ioNeuron : ioNeurons.get() ) {
            if ( ioNeuron.hasName("NN_OUTPUT_NEURON_STMT") || ioNeuron.hasName("NN_OUTPUT_NEURON_WO_VAR_STMT") ) {
                outputNeurons.add(ioNeuron);
            }
        }
        return outputNeurons;
    }

    @Override
    public String toString() {
        return "nnStep()";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        helper.getDropdownFactory();
        final List<Statement> ioNeuronsWrapped = block.getStatement();
        final Data netDefinition = block.getData();
        final StmtList<V> ioNeurons = helper.extractStatement(ioNeuronsWrapped, "IONEURON");
        return new NNStepStmt<V>(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), ioNeurons);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addStatement(jaxbDestination, "IONEURON", ioNeurons);
        return jaxbDestination;
    }
}
