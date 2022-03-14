package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

import java.util.List;

/**
 * This class represents the <b>nnStep</b> block from Blockly in the AST. An object of this class will generate a nnStep statement.<br/>
 */
public class ListenStepStmt<V> extends Stmt<V> {
   private final StmtList<V> intents;

    private ListenStepStmt(StmtList<V> intents, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LISTEN_STEP_STMT"), properties, comment);
         this.intents = intents;
        setReadOnly();
    }

    /**
     * create <b>nnStep</b> statement.
     *
     * @return read only object of class {@link ListenStepStmt}
     */
    public static <V> ListenStepStmt<V> make(StmtList<V> intents, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ListenStepStmt<>(intents, properties, comment);
    }

    public StmtList<V> getIntents() {
        return this.intents;
    }

    @Override
    public String toString() {
        return "listen()";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        helper.getDropdownFactory(); // TODO check!
        final List<Statement> intentsList = block.getStatement();
        final StmtList<V> intents = helper.extractStatement(intentsList, "LISTEN_STEP");
        return ListenStepStmt.make(intents, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addStatement(jaxbDestination, "LISTEN_STEP", intents);
        return jaxbDestination;
    }
}
