package de.fhg.iais.roberta.mode.sensor.nao;

import de.fhg.iais.roberta.inter.mode.general.IMode;

public enum Part implements IMode {
    HAND(), BUMPER();

    private final String[] values;

    private Part(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}