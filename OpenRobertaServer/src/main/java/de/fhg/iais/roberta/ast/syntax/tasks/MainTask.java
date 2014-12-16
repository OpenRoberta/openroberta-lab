package de.fhg.iais.roberta.ast.syntax.tasks;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;

/**
 * This class represents the <b>robControls_start</b> block from Blockly
 * into the AST (abstract syntax tree). Object from this class points to the main thread of the program.<br/>
 * <br/>
 */
public class MainTask<V> extends Task<V> {

    private MainTask(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.MAIN_TASK, properties, comment);
        setReadOnly();
    }

    /**
     * creates instance of {@link MainTask}. This instance is read only and cannot be modified.
     */
    public static <V> MainTask<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MainTask<V>(properties, comment);
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
        return "MainTask []";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }

}
