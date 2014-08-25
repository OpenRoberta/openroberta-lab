package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.codegen.lejos.Visitor;

/**
 * This class represents the <b>robActions_display_clear</b> block from Blockly into the AST (abstract syntax tree).
 */
public final class ClearDisplayAction extends Action {

    private ClearDisplayAction() {
        super(Kind.CLEAR_DISPLAY_ACTION);
        setReadOnly();
    }

    /**
     * Creates instance of {@link ClearDisplayAction}. This instance is read only and can not be modified.
     * 
     * @return read only object of class {@link ClearDisplayAction}.
     */
    public static ClearDisplayAction make() {
        return new ClearDisplayAction();
    }

    @Override
    public String toString() {
        return "ClearDisplayAction []";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitClearDisplayAction(this);
    }
}
