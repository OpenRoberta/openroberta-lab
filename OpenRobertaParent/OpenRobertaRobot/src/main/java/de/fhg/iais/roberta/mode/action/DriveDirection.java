
package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;

public enum DriveDirection implements IDriveDirection {
    //TODO: rename FOREWARD first in blockly
    FOREWARD( "OFF", "FORWARD" ), BACKWARD( "ON", "BACKWARDS" );

    private final String[] values;

    private DriveDirection(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}