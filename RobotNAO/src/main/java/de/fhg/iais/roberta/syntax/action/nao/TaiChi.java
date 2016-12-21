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
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

/**
 * This class represents the <b>naoActions_taiChi</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for making the NAO perform a short tai chi session.<br/>
 * <br/>
 */
public final class TaiChi<V> extends Action<V> {

    private TaiChi(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("TAI_CHI"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link TaiChi}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link TaiChi}
     */
    private static <V> TaiChi<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TaiChi<V>(properties, comment);
    }

    @Override
    public String toString() {
        return "TaiChi []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitTaiChi(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {

        return TaiChi.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        return jaxbDestination;
    }
}
