package de.fhg.iais.roberta.syntax.action.serial;

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
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

public class SerialWriteAction<V> extends Action<V> {

    private final Expr<V> value;

    private SerialWriteAction(Expr<V> value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("WRITE_TO_SERIAL"), properties, comment);
        Assert.notNull(value);
        this.value = value;
        setReadOnly();
    }

    public static <V> SerialWriteAction<V> make(Expr<V> value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SerialWriteAction<>(value, properties, comment);
    }

    public Expr<V> getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "PinWriteValueSensor [" + this.value + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        helper.getDropdownFactory();
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Phrase<V> value = helper.extractValue(values, new ExprParam(BlocklyConstants.OUT, BlocklyType.ANY));
        return SerialWriteAction.make(Jaxb2Ast.convertPhraseToExpr(value), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.OUT, this.value);
        return jaxbDestination;
    }

}