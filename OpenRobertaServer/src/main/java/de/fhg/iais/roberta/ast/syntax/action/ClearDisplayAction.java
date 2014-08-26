package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.codegen.lejos.Visitor;

/**
 * This class represents the <b>robActions_display_clear</b> block from Blockly into the AST (abstract syntax tree).
 */
public final class ClearDisplayAction<V> extends Action<V> {

    private ClearDisplayAction() {
        super(Kind.CLEAR_DISPLAY_ACTION);
        setReadOnly();
    }

    /**
     * Creates instance of {@link ClearDisplayAction}. This instance is read only and can not be modified.
     * 
     * @return read only object of class {@link ClearDisplayAction}.
     */
    public static <V> ClearDisplayAction<V> make() {
        return new ClearDisplayAction<V>();
    }

    @Override
    public String toString() {
        return "ClearDisplayAction []";
    }

    @Override
    protected V accept(Visitor<V> visitor) {
        return visitor.visitClearDisplayAction(this);
    }
}
