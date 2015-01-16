package de.fhg.iais.roberta.ast.syntax.tasks;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robControls_start</b> block from Blockly
 * into the AST (abstract syntax tree). Object from this class points to the main thread of the program.<br/>
 * <br/>
 * <b>In this block are defined all global variables that are used in the program.</b>
 */
public class MainTask<V> extends Task<V> {
    private final StmtList<V> variables;

    private MainTask(StmtList<V> variables, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.MAIN_TASK, properties, comment);
        Assert.isTrue(variables.isReadOnly() && variables != null);
        this.variables = variables;
        setReadOnly();
    }

    /**
     * creates instance of {@link MainTask}. This instance is read only and cannot be modified.
     *
     * @param variables read only list of declared variables
     */
    public static <V> MainTask<V> make(StmtList<V> variables, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MainTask<V>(variables, properties, comment);
    }

    /**
     * @return the variables
     */
    public StmtList<V> getVariables() {
        return this.variables;
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMainTask(this);
    }

    @Override
    public String toString() {
        return "MainTask [" + this.variables + "]";
    }

    @Override
    public Block astToBlock() {
        boolean declare = this.variables.get().size() == 0 ? false : true;

        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDeclare(declare);
        jaxbDestination.setMutation(mutation);
        AstJaxbTransformerHelper.addStatement(jaxbDestination, BlocklyConstants.ST, this.variables);
        return jaxbDestination;
    }

}
