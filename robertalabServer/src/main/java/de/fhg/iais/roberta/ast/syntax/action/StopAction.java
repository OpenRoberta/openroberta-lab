package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * This class represents the <b>robActions_motorDiff_stop</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code to stop the work of the motors.<br/>
 */
public class StopAction extends Action {

    private StopAction() {
        super(Phrase.Kind.StopAction);
        setReadOnly();
    }

    /**
     * Creates instance of {@link StopAction}. This instance is read only and can not be modified.
     * 
     * @return read only object of class {@link StopAction}.
     */
    public static StopAction make() {
        return new StopAction();
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "StopAction []";
    }

}
