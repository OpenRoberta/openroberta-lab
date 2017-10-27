package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;

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