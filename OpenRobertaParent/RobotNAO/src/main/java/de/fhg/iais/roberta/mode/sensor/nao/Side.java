package de.fhg.iais.roberta.mode.sensor.nao;

import de.fhg.iais.roberta.inter.mode.general.IMode;

public enum Side implements IMode {
    LEFT(), RIGHT(), FRONT(), REAR(), MIDDLE();

    private final String[] values;

    private Side(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}