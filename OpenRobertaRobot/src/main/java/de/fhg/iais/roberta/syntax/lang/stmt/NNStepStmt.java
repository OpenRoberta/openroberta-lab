package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This class represents the <b>nnStep</b> block from Blockly in the AST. An object of this class will generate a nnStep statement.<br/>
 */
public class NNStepStmt<V> extends Stmt<V> {
    private final Data netDefinition;
    private final StmtList<V> ioNeurons;

    private NNStepStmt(Data netDefinition, StmtList<V> ioNeurons, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("NNSTEP_STMT"), properties, comment);
        this.netDefinition = netDefinition;
        this.ioNeurons = ioNeurons;
        setReadOnly();
    }

    /**
     * create <b>nnStep</b> statement.
     *
     * @return read only object of class {@link NNStepStmt}
     */
    public static <V> NNStepStmt<V> make(Data netDefinition, StmtList<V> ioNeurons, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NNStepStmt<>(netDefinition, ioNeurons, properties, comment);
    }

    public Data getNetDefinition() {
        return this.netDefinition;
    }

    public StmtList<V> getIoNeurons() {
        return this.ioNeurons;
    }

    public List<Stmt<V>> getInputNeurons() {
        final List<Stmt<V>> inputNeurons = new ArrayList<>();
        for ( Stmt<V> ioNeuron : ioNeurons.get() ) {
            if ( ioNeuron.getKind().getName().equals("NN_INPUT_NEURON_STMT") ) {
                inputNeurons.add(ioNeuron);
            }
        }
        return inputNeurons;
    }

    public List<Stmt<V>> getOutputNeurons() {
        final List<Stmt<V>> outputNeurons = new ArrayList<>();
        for ( Stmt<V> ioNeuron : ioNeurons.get() ) {
            if ( ioNeuron.getKind().getName().equals("NN_OUTPUT_NEURON_STMT") ) {
                outputNeurons.add(ioNeuron);
            }
        }
        return outputNeurons;
    }

    @Override
    public String toString() {
        return "nnStep()";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        helper.getDropdownFactory();
        final List<Statement> ioNeuronsWrapped = block.getStatement();
        final Data netDefinition = block.getData();
        final StmtList<V> ioNeurons = helper.extractStatement(ioNeuronsWrapped, "IONEURON");
        return NNStepStmt.make(netDefinition, ioNeurons, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        jaxbDestination.setData(netDefinition);
        Ast2Jaxb.addStatement(jaxbDestination, "IONEURON", ioNeurons);
        return jaxbDestination;
    }

}
