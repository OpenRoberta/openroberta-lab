package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.INaoVisitor;

/**
 * This class represents the <b>naoActions_sayText</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * making the robot say the text.<br>
 * <br>
 * <br>
 */
public class PlayFile<V> extends Action<V> {
    private final Expr<V> msg;

    private PlayFile(Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PLAY_FILE"), properties, comment);
        Assert.isTrue(msg != null);
        this.msg = msg;
        setReadOnly();
    }

    /**
     * Creates instance of {@link DisplayTextAction}. This instance is read only and can not be modified.
     *
     * @param msg {@link msg} that will be printed on the display of the brick; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link DisplayTextAction}
     */
    private static <V> PlayFile<V> make(Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PlayFile<>(msg, properties, comment);
    }

    /**
     * @return the message.
     */
    public Expr<V> getMsg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return "PlayFile [" + this.msg + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((INaoVisitor<V>) visitor).visitPlayFile(this);

    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);
        Phrase<V> msg = helper.extractValue(values, new ExprParam(BlocklyConstants.OUT, BlocklyType.STRING));
        return PlayFile.make(helper.convertPhraseToExpr(msg), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.OUT, this.msg);

        return jaxbDestination;
    }

}
