package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_brickLight_off</b> and <b>robActions_brickLight_reset</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for turning the light off or reset them.<br/>
 * <br/>
 * The client must provide the {@link Status}.
 */
public class LightStatusAction<V> extends Action<V> {
    private final Status status;

    private LightStatusAction(Status status) {
        super(Phrase.Kind.LIGHT_STATUS_ACTION);
        Assert.isTrue(status != null);
        this.status = status;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LightStatusAction}. This instance is read only and can not be modified.
     * 
     * @param status in which we want to set the lights (off or reset).
     * @return read only object of class {@link LightStatusAction}.
     */
    public static <V> LightStatusAction<V> make(Status status) {
        return new LightStatusAction<V>(status);
    }

    /**
     * @return status of the lights user wants to set.
     */
    public Status getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "LightStatusAction [" + this.status + "]";
    }

    @Override
    protected V accept(Visitor<V> visitor) {
        return visitor.visitLightStatusAction(this);
    }

    /**
     * Status in which user can set the lights.
     */
    public static enum Status {
        OFF, RESET;
    }
}
