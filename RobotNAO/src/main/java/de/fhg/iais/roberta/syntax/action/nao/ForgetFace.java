package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>naoActions_sayText</b> block from Blockly into the AST (abstract syntax tree). Objects from this class will generate code for
 * making the robot say the text.<br>
 * <br>
 * <br>
 */
public class ForgetFace<V> extends Action<V> {
    private final Expr<V> faceName;

    private ForgetFace(Expr<V> faceName, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("FORGET_FACE"), properties, comment);
        Assert.isTrue(faceName != null);
        this.faceName = faceName;
        setReadOnly();
    }

    /**
     * Creates instance of {@link DisplayTextAction}. This instance is read only and can not be modified.
     *
     * @param faceName {@link msg} that will be printed on the display of the brick; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link DisplayTextAction}
     */
    private static <V> ForgetFace<V> make(Expr<V> faceName, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ForgetFace<>(faceName, properties, comment);
    }

    /**
     * @return the message.
     */
    public Expr<V> getFaceName() {
        return this.faceName;
    }

    @Override
    public String toString() {
        return "ForgetFace [" + this.faceName + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Phrase<V> msg = helper.extractValue(values, new ExprParam(BlocklyConstants.NAME, BlocklyType.STRING));
        return ForgetFace.make(Jaxb2Ast.convertPhraseToExpr(msg), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.NAME, this.faceName);

        return jaxbDestination;
    }

}
