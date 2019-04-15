package de.fhg.iais.roberta.mode.general;

import de.fhg.iais.roberta.inter.mode.general.IDirection;

public enum Direction implements IDirection {
    UP(), DOWN(), LEFT(), RIGHT();

    private final String[] values;

    private Direction(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}