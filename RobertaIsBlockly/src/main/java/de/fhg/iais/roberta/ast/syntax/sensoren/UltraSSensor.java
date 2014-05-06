package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.dbc.Assert;

public class UltraSSensor extends Sensor {
    private final UType utype;

    private UltraSSensor(UType utype) {
        Assert.isTrue(utype != null);
        this.utype = utype;
        setReadOnly();
    }

    public static UltraSSensor make(UType utype) {
        return new UltraSSensor(utype);
    }

    public UType getUtype() {
        return this.utype;
    }

    @Override
    public Kind getKind() {
        return Kind.UltraSSensor;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "UltraSSensor [" + this.utype + "]";
    }

    public static enum UType {
        distance, presence;
    }

}
