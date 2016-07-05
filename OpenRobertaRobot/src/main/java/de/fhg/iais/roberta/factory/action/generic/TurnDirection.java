package de.fhg.iais.roberta.factory.action.generic;

import de.fhg.iais.roberta.factory.action.ITurnDirection;

public enum TurnDirection implements ITurnDirection {
    RIGHT(), LEFT();

    private final String[] values;

    private TurnDirection(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}