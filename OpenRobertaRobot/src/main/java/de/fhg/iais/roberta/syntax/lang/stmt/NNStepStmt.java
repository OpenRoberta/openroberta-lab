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
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoBasic(name = "NN_STEP_STMT", category = "STMT", blocklyNames = {"robactions_nnstep"})
public final class NNStepStmt extends Stmt {
    public final StmtList ioNeurons;
    public final Data netDefinition; // deprecated, only used to copy this value to the start block later

    public NNStepStmt(BlocklyProperties properties, StmtList ioNeurons, Data netDefinition) {
        super(properties);
        this.ioNeurons = ioNeurons;
        this.netDefinition = netDefinition;
        setReadOnly();
    }

    public StmtList getIoNeurons() {
        return this.ioNeurons;
    }

    public List<Stmt> getInputNeurons() {
        final List<Stmt> inputNeurons = new ArrayList<>();
        for ( Stmt ioNeuron : ioNeurons.get() ) {
            if ( ioNeuron.hasName("NN_INPUT_NEURON_STMT") ) {
                inputNeurons.add(ioNeuron);
            }
        }
        return inputNeurons;
    }

    /**
     * @return the output neuron statements
     */
    public List<Stmt> getOutputNeurons() {
        final List<Stmt> outputNeurons = new ArrayList<>();
        for ( Stmt ioNeuron : ioNeurons.get() ) {
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

    public static Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        helper.getDropdownFactory();
        final List<Statement> ioNeuronsWrapped = block.getStatement();
        final Data netDefinition = block.getData();
        final StmtList ioNeurons = helper.extractStatement(ioNeuronsWrapped, "IONEURON");
        return new NNStepStmt(Jaxb2Ast.extractBlocklyProperties(block), ioNeurons, netDefinition);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addStatement(jaxbDestination, "IONEURON", ioNeurons);
        jaxbDestination.setData(netDefinition);
        return jaxbDestination;
    }
}
