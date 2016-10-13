package de.fhg.iais.roberta.syntax.expr;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robConnection</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate color constant.<br/>
 * <br>
 * The client must provide the value of the color. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(String, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class ConnectConst<V> extends Expr<V> {
    private final String value;
    private final String dataValue;

    private ConnectConst(String dataValue, String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("CONNECTION_CONST"), properties, comment);
        Assert.isTrue(!value.equals(""));
        this.value = value;
        this.dataValue = dataValue;
        setReadOnly();
    }

    /**
     * creates instance of {@link ConnectConst}. This instance is read only and can not be modified.
     *
     * @param value of the numerical constant; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link NumConst}
     */
    public static <V> ConnectConst<V> make(String dataValue, String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ConnectConst<V>(dataValue, value, properties, comment);
    }

    /**
     * @return value of the numerical constant
     */
    public String getValue() {
        return this.value;
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.CONNECTION;
    }

    @Override
    public String toString() {
        return "ConnectConst [" + this.value + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitConnectConst(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        Data data = block.getData();
        String datum = data.getValue();
        String field = helper.extractField(fields, BlocklyConstants.CONNECTION);
        return ConnectConst.make(datum, field, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.CONNECTION, getValue());
        Data data = new Data();
        data.setValue(this.dataValue);
        jaxbDestination.setData(data);
        return jaxbDestination;
    }
}
