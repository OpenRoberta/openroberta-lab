package de.fhg.iais.roberta.mode.general;

import de.fhg.iais.roberta.inter.mode.general.IMode;

/**
 * This enumeration contains all the operations that can be performed over an element in list.
 *
 * @author kcvejoski
 */
public enum ListElementOperations implements IMode {
    GET(false), GET_REMOVE(false), REMOVE(true), SET(true), INSERT(true);

    private final String[] values;
    private final boolean statement;

    private ListElementOperations(boolean statement, String... strings) {
        this.values = strings;
        this.statement = statement;
    }

    public boolean isStatement() {
        return this.statement;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}
