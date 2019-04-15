package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.IRelayMode;

public enum RelayMode implements IRelayMode {
    DEFAULT(), ON( "HIGH" ), OFF( "LOW" );

    private final String[] values;

    private RelayMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}