package de.fhg.iais.roberta.syntax.lang.blocksequence;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "MAIN_TASK", category = "TASK", blocklyNames = {"robControls_start_ardu", "robControls_start", "mbedcontrols_start"})
public final class MainTask<V> extends Task<V> {
    public final StmtList<V> variables;
    public final String debug;
    public final Data data;

    public MainTask(BlocklyBlockProperties properties, BlocklyComment comment, StmtList<V> variables, String debug, Data data) {
        super(properties, comment);
        Assert.isTrue(variables.isReadOnly() && variables != null);
        this.variables = variables;
        this.debug = debug;
        this.data = data;
        setReadOnly();
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

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        String debug = null;
        List<Field> fields = block.getField();
        if ( !fields.isEmpty() ) {
            debug = Jaxb2Ast.extractField(fields, "DEBUG");
        }
        if ( block.getMutation().isDeclare() == true ) {
            List<Statement> statements = Jaxb2Ast.extractStatements(block, (short) 1);
            StmtList<V> statement = helper.extractStatement(statements, BlocklyConstants.ST);
            return new MainTask<V>(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), statement, debug, block.getData());
        }
        StmtList<V> listOfVariables = new StmtList<V>();
        listOfVariables.setReadOnly();
        return new MainTask<V>(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), listOfVariables, debug, block.getData());
    }

    @Override
    public Block astToBlock() {
        boolean declare = !this.variables.get().isEmpty();

        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDeclare(declare);
        jaxbDestination.setMutation(mutation);
        jaxbDestination.setData(data);
        if ( this.debug != null ) {
            Ast2Jaxb.addField(jaxbDestination, "DEBUG", this.debug);
        }
        Ast2Jaxb.addStatement(jaxbDestination, BlocklyConstants.ST, this.variables);
        return jaxbDestination;
    }

}
