package de.fhg.iais.roberta.syntax.sensor.nao;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.nao.NaoAstVisitor;

/**
 * This class represents the <b>naoSensors_detectFace</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * detecting a face previously saved in NAOs database.<br/>
 * <br/>
 */
public final class DetectFace<V> extends de.fhg.iais.roberta.syntax.sensor.Sensor<V> {

    private DetectFace(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DETECT_FACE"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link DETECTFACE}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     */
    static <V> DetectFace<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DetectFace<V>(properties, comment);
    }

    @Override
    public String toString() {
        return "DetectFace []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitDetectFace(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {

        return DetectFace.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        return jaxbDestination;
    }
}
