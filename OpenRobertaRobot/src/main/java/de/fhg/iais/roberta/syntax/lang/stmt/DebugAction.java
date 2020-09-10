package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

public class DebugAction<V> extends Stmt<V> {

    private final Expr<V> value;

    private DebugAction(Expr<V> value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DEBUG_STMT"), properties, comment);
        Assert.notNull(value);
        this.value = value;
        setReadOnly();
    }

    public static <V> DebugAction<V> make(Expr<V> value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DebugAction<>(value, properties, comment);
    }

    public Expr<V> getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "DebugAction [" + this.value + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        helper.getDropdownFactory();
        List<Value> values = helper.extractValues(block, (short) 1);
        Phrase<V> value = helper.extractValue(values, new ExprParam(BlocklyConstants.OUT, BlocklyType.ANY));
        return DebugAction.make(helper.convertPhraseToExpr(value), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.OUT, value);
        return jaxbDestination;
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitDebugAction(this);
    }

}