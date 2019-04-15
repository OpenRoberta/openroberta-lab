package de.fhg.iais.roberta.mode.general;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IWorkingState;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum WorkingState implements IWorkingState {
    ON(), OFF();

    private final String[] values;

    private WorkingState(String... values) {
        this.values = values;
    }

    public static WorkingState get(String workingState) {
        if ( (workingState == null) || workingState.isEmpty() ) {
            throw new DbcException("Invalid Modus: " + workingState);
        }
        String sUpper = workingState.trim().toUpperCase(Locale.GERMAN);
        for ( WorkingState p : WorkingState.values() ) {
            if ( p.toString().equals(sUpper) ) {
                return p;
            }
            for ( String value : p.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return p;
                }
            }
        }
        throw new DbcException("Invalid Modus: " + workingState);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}