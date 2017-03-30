package de.fhg.iais.roberta.mode.general.nxt;

import de.fhg.iais.roberta.inter.mode.action.IWorkingState;

/**
 * All colors that are legal.
 */
public enum WorkingState implements IWorkingState {

    ON(), OFF();

    private final String[] values;

    private WorkingState(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return values;
    }
}
