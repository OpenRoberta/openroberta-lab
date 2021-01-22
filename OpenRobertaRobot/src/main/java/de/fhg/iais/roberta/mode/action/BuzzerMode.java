package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.IBuzzerMode;

public enum BuzzerMode implements IBuzzerMode {
    DEFAULT(), ON( "HIGH" ), OFF( "LOW" ), TOGGLE() ;

    private final String[] values;

    private BuzzerMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}