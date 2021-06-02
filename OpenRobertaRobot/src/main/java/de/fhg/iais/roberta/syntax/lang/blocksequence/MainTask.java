package de.fhg.iais.roberta.syntax.lang.blocksequence;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robControls_start</b> block from Blockly into the AST (abstract syntax tree). Object from this class points to the main thread
 * of the program.<br/>
 * <br/>
 * <b>In this block are defined all global variables that are used in the program.</b>
 */
public class MainTask<V> extends Task<V> {
    private final StmtList<V> variables;
    private final String debug;

    private MainTask(StmtList<V> variables, String debug, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("MAIN_TASK"), properties, comment);
        Assert.isTrue(variables.isReadOnly() && variables != null);
        this.variables = variables;
        this.debug = debug;
        setReadOnly();
    }

    /**
     * creates instance of {@link MainTask}. This instance is read only and cannot be modified.
     *
     * @param variables read only list of declared variables
     */
    public static <V> MainTask<V> make(StmtList<V> variables, String debug, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MainTask<V>(variables, debug, properties, comment);
    }

    /**
     * @return the variables
     */
    public StmtList<V> getVariables() {
        return this.variables;
    }

    public String getDebug() {
        return this.debug;
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public Assoc getAssoc() {
        return null;
    }

    @Override
    public String toString() {
        return "MainTask [" + this.variables + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        String debug = null;
        List<Field> fields = block.getField();
        if ( !fields.isEmpty() ) {
            debug = Jaxb2Ast.extractField(fields, "DEBUG");
        }
        if ( block.getMutation().isDeclare() == true ) {
            List<Statement> statements = Jaxb2Ast.extractStatements(block, (short) 1);
            StmtList<V> statement = helper.extractStatement(statements, BlocklyConstants.ST);
            return MainTask.make(statement, debug, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
        }
        StmtList<V> listOfVariables = StmtList.make();
        listOfVariables.setReadOnly();
        return MainTask.make(listOfVariables, debug, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        boolean declare = !this.variables.get().isEmpty();

        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDeclare(declare);
        jaxbDestination.setMutation(mutation);
        if ( getDebug() != null ) {
            Ast2Jaxb.addField(jaxbDestination, "DEBUG", getDebug());
        }
        Ast2Jaxb.addStatement(jaxbDestination, BlocklyConstants.ST, this.variables);
        return jaxbDestination;
    }

}
