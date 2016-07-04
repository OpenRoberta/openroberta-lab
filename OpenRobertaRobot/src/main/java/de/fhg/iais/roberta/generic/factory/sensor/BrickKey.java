package de.fhg.iais.roberta.generic.factory.sensor;

import de.fhg.iais.roberta.factory.sensor.IBrickKey;

public enum BrickKey implements IBrickKey {
    ENTER(), UP(), DOWN(), LEFT(), RIGHT(), ESCAPE(), ANY();

    private final String[] values;

    private BrickKey(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}