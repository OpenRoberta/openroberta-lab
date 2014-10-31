package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>lists_repeat</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link Functions} contains all allowed functions.
 */
public class ListRepeat<V> extends Function<V> {
    private final List<Expr<V>> param;

    private ListRepeat(List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Kind.LIST_REPEAT_FUNCT, properties, comment);
        Assert.isTrue(param != null);
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ListRepeat}. This instance is read only and can not be modified.
     *
     * @param param list of parameters for the function,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link ListRepeat}
     */
    public static <V> ListRepeat<V> make(List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ListRepeat<V>(param, properties, comment);
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
    }

    @Override
    public int getPrecedence() {
        return 10;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return "ListRepeat [" + this.param + "]";
    }
}
