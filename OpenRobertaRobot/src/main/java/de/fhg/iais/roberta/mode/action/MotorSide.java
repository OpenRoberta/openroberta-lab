package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.IMotorSide;

public enum MotorSide implements IMotorSide {
    RIGHT( "right" ), LEFT( "left" ), NONE( "" );

    private final String[] values;

    private MotorSide(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

    @Override
    public String getText() {
        return this.values[0];
    }

}