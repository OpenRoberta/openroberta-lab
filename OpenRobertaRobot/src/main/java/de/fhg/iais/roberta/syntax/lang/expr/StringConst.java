package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>text</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for string
 * constant.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(String, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
@NepoExpr(category = "EXPR", blocklyNames = {"text"}, containerType = "STRING_CONST", blocklyType = BlocklyType.STRING)
public final class StringConst<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.TEXT)
    public final String value;

    public StringConst(BlocklyBlockProperties properties, BlocklyComment comment, String value) {
        super(properties, comment);
        this.value = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link StringConst}. This instance is read only and can not be modified.
     *
     * @param value that the boolean constant will have,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link StringConst}
     */
    public static <V> StringConst<V> make(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new StringConst<V>(properties, comment, value);
    }

    /**
     * @return the value of the string constant
     */
    public String getValue() {
        return this.value;
    }
}
