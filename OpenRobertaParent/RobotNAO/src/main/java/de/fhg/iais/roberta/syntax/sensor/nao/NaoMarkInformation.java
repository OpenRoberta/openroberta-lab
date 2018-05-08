package de.fhg.iais.roberta.syntax.sensor.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.nao.NaoAstVisitor;

/**
 * This class represents the <b>naoSensors_naoMark</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * detecting a NaoMark.<br/>
 * <br/>
 */
public final class NaoMarkInformation<V> extends Sensor<V> {

    private final Expr<V> naoMarkId;

    private NaoMarkInformation(Expr<V> naoMarkId, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("NAO_MARK_INFORMATION"), properties, comment);
        this.naoMarkId = naoMarkId;
        setReadOnly();
    }

    /**
     * Creates instance of {@link NaoMarkInformation}. This instance is read only and can not be modified.
     *
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link NaoMarkInformation}
     */
    static <V> NaoMarkInformation<V> make(Expr<V> naoMarkId, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NaoMarkInformation<>(naoMarkId, properties, comment);
    }

    @Override
    public String toString() {
        return "NaoMarkInformation [" + this.naoMarkId.toString() + "]";
    }

    public Expr<V> getNaoMarkId() {
        return this.naoMarkId;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitNaoMarkInformation(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);
        Phrase<V> naoMarkId = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NUMBER));
        return NaoMarkInformation.make(helper.convertPhraseToExpr(naoMarkId), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE, "");
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, this.naoMarkId);
        return jaxbDestination;
    }
}
