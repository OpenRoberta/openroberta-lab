package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.nao.NaoAstVisitor;

/**
 * This class represents the <b>naoActions_getLanguage</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * getting the languages that are installed on NAO<br/>
 * <br/>
 */
public final class GetLanguage<V> extends Action<V> {

    private GetLanguage(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("GET_LANGUAGE"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link GetLanguage}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link GetLanguage}
     */
    private static <V> GetLanguage<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new GetLanguage<V>(properties, comment);
    }

    @Override
    public String toString() {
        return "Get Language []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitGetLanguage(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {

        return GetLanguage.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        return jaxbDestination;
    }
}
