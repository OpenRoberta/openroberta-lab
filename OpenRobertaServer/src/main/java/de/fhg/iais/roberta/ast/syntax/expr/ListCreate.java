package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;

/**
 * This class represents <b>robLists_create_with</b> and <b>lists_create_with</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code numerical value.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(String, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class ListCreate<V> extends Expr<V> {
    private final ExprList<V> exprList;

    private ListCreate(ExprList<V> exprList, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.LIST_CREATE, properties, comment);
        this.exprList = exprList;
        setReadOnly();
    }

    /**
     * creates instance of {@link ListCreate}. This instance is read only and can not be modified.
     *
     * @param exprList,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ListCreate}.
     */
    public static <V> ListCreate<V> make(ExprList<V> exprList, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ListCreate<V>(exprList, properties, comment);
    }

    /**
     * @return value of the numerical constant
     */
    public ExprList<V> getValue() {
        return this.exprList;
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
    protected V accept(AstVisitor<V> visitor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return "ListCreate [" + this.exprList + "]";
    }

}
