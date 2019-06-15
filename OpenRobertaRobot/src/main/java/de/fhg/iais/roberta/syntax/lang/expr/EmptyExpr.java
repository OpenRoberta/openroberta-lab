package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * {@link EmptyExpr} is used when in binary or unary expressions, expression is missing. When create instance from this class we pass as parameter the type of
 * the value should have the missing expression.
 */
public class EmptyExpr<V> extends Expr<V> {

    private final BlocklyType defVal;

    private EmptyExpr(BlocklyType defVal) {
        super(BlockTypeContainer.getByName("EMPTY_EXPR"), BlocklyBlockProperties.make("1", "1"), null);
        Assert.isTrue(defVal != null);
        this.defVal = defVal;
        setReadOnly();
    }

    /**
     * create read only instance from {@link EmptyExpr}.
     *
     * @param defVal type of the value that the missing expression should have.
     * @return read only object of class {@link EmptyExpr}.
     */
    public static <V> EmptyExpr<V> make(BlocklyType defVal) {
        return new EmptyExpr<V>(defVal);
    }

    /**
     * factory method: create read only instance from {@link EmptyExpr}.<br>
     * <b>Main use: either testing or textual representation of programs (because in these cases no graphical regeneration is required.</b>
     *
     * @param name of the blockly type of the value that the missing expression should have.
     * @return read only object of class {@link EmptyExpr}.
     */
    public static <V> EmptyExpr<V> make(String blocklyName) {
        if ( blocklyName.trim().toUpperCase().equals("ANY") ) {
            return new EmptyExpr<V>(BlocklyType.ANY);
        }
        if ( blocklyName.trim().toUpperCase().equals("COMPARABLE") ) {
            return new EmptyExpr<V>(BlocklyType.COMPARABLE);
        }
        if ( blocklyName.trim().toUpperCase().equals("ADDABLE") ) {
            return new EmptyExpr<V>(BlocklyType.ADDABLE);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY") ) {
            return new EmptyExpr<V>(BlocklyType.ARRAY);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_NUMBER") ) {
            return new EmptyExpr<V>(BlocklyType.ARRAY_NUMBER);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_STRING") ) {
            return new EmptyExpr<V>(BlocklyType.ARRAY_STRING);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_COLOUR") ) {
            return new EmptyExpr<V>(BlocklyType.ARRAY_COLOUR);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_BOOLEAN") ) {
            return new EmptyExpr<V>(BlocklyType.ARRAY_BOOLEAN);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_IMAGE") ) {
            return new EmptyExpr<V>(BlocklyType.ARRAY_IMAGE);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_CONNECTION") ) {
            return new EmptyExpr<V>(BlocklyType.ARRAY_CONNECTION);
        }
        if ( blocklyName.trim().toUpperCase().equals("BOOLEAN") ) {
            return new EmptyExpr<V>(BlocklyType.BOOLEAN);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER") ) {
            return new EmptyExpr<V>(BlocklyType.NUMBER);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER_INT") ) {
            return new EmptyExpr<V>(BlocklyType.NUMBER_INT);
        }
        if ( blocklyName.trim().toUpperCase().equals("STRING") ) {
            return new EmptyExpr<V>(BlocklyType.STRING);
        }
        if ( blocklyName.trim().toUpperCase().equals("COLOR") ) {
            return new EmptyExpr<V>(BlocklyType.COLOR);
        }
        if ( blocklyName.trim().toUpperCase().equals("IMAGE") ) {
            return new EmptyExpr<V>(BlocklyType.IMAGE);
        }
        if ( blocklyName.trim().toUpperCase().equals("PREDEFINED_IMAGE") ) {
            return new EmptyExpr<V>(BlocklyType.PREDEFINED_IMAGE);
        }
        if ( blocklyName.trim().toUpperCase().equals("NULL") ) {
            return new EmptyExpr<V>(BlocklyType.NULL);
        }
        if ( blocklyName.trim().toUpperCase().equals("REF") ) {
            return new EmptyExpr<V>(BlocklyType.REF);
        }
        if ( blocklyName.trim().toUpperCase().equals("PRIM") ) {
            return new EmptyExpr<V>(BlocklyType.PRIM);
        }
        if ( blocklyName.trim().toUpperCase().equals("NOTHING") ) {
            return new EmptyExpr<V>(BlocklyType.NOTHING);
        }
        if ( blocklyName.trim().toUpperCase().equals("VOID") ) {
            return new EmptyExpr<V>(BlocklyType.VOID);
        }
        if ( blocklyName.trim().toUpperCase().equals("CONNECTION") ) {
            return new EmptyExpr<V>(BlocklyType.CONNECTION);
        }
        if ( blocklyName.trim().toUpperCase().equals("CAPTURED_TYPE") ) {
            return new EmptyExpr<V>(BlocklyType.CAPTURED_TYPE);
        }
        if ( blocklyName.trim().toUpperCase().equals("R") ) {
            return new EmptyExpr<V>(BlocklyType.R);
        }
        if ( blocklyName.trim().toUpperCase().equals("S") ) {
            return new EmptyExpr<V>(BlocklyType.S);
        }
        if ( blocklyName.trim().toUpperCase().equals("T") ) {
            return new EmptyExpr<V>(BlocklyType.T);
        }
        return null;
    }

    /**
     * @return type of the value that the missing expression should have.
     */
    public BlocklyType getDefVal() {
        return this.defVal;
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
        return BlocklyType.NOTHING;
    }

    @Override
    public String toString() {
        return "EmptyExpr [defVal=" + this.defVal + "]";
    }

    @Override
    protected V accept(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitEmptyExpr(this);
    }

    @Override
    public Block astToBlock() {
        return null;
    }

}
