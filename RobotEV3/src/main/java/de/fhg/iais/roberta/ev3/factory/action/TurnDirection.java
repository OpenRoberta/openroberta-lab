package de.fhg.iais.roberta.ev3.factory.action;

import de.fhg.iais.roberta.factory.ITurnDirection;

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