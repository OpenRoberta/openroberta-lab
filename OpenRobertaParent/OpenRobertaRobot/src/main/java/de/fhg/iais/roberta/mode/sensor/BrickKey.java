package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;

public enum BrickKey implements IBrickKey {
    ENTER( "ENTER", "2" ), LEFT( "LEFT", "1" ), RIGHT( "RIGHT", "3" ), ANY( "ANY", "123" ), BUTTON_A( "A" ), BUTTON_B( "B" ), UP(), DOWN(), ESCAPE();
    private final String[] values;

    private BrickKey(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

    @Override
    public String getOraName() {
        if ( this.values.length != 0 ) {
            return this.values[0];
        } else {
            return this.toString();
        }
    }

    @Override
    public String getCodeName() {
        // TODO Auto-generated method stub
        return null;
    }
}