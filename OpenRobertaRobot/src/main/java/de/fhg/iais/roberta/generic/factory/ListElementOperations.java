package de.fhg.iais.roberta.generic.factory;

import de.fhg.iais.roberta.factory.IListElementOperations;

/**
 * This enumeration contains all the operations that can be performed over an element in list.
 *
 * @author kcvejoski
 */
public enum ListElementOperations implements IListElementOperations {
    GET( false ), GET_REMOVE( false ), REMOVE( true ), SET( true ), INSERT( true );

    private final String[] values;
    private final boolean statment;

    private ListElementOperations(boolean statment, String... strings) {
        this.values = strings;
        this.statment = statment;
    }

    @Override
    public boolean isStatment() {
        return this.statment;
    }

    @Override
    public String[] getValues() {

        return this.values;
    }
}
