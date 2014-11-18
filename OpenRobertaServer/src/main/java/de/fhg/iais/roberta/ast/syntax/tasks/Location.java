package de.fhg.iais.roberta.ast.syntax.tasks;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;

public class Location<V> extends Task<V> {

    private final String x;
    private final String y;

    private Location(String x, String y) {
        super(Phrase.Kind.LOCATION, null, null);
        this.x = x;
        this.y = y;
        setReadOnly();
    }

    /**
     * creates instance of {@link Location}. This instance is read only and cannot be modified.
     */
    public static <V> Location<V> make(String x, String y) {
        return new Location<V>(x, y);
    }

    public String getX() {
        return this.x;
    }

    public String getY() {
        return this.y;
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public Assoc getAssoc() {
        return null;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitLocation(this);
    }

    @Override
    public String toString() {
        return "Location [x=" + this.x + ", y=" + this.y + "]";
    }

    @Override
    public Block astToBlock() {
        return null;
    }

}
