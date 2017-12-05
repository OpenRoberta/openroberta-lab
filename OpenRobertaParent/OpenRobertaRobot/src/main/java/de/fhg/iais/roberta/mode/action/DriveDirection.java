
package de.fhg.iais.roberta.mode.action;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum DriveDirection implements IDriveDirection {
    //TODO: rename FOREWARD first in blockly
    FOREWARD( "OFF", "FORWARD" ), BACKWARD( "ON", "BACKWARDS" );

    private final String[] values;

    private DriveDirection(String... values) {
        this.values = values;
    }

    public static DriveDirection get(String direction) {
        if ( (direction == null) || direction.isEmpty() ) {
            throw new DbcException("Invalid Walk Direction: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( DriveDirection wd : DriveDirection.values() ) {
            if ( wd.toString().equals(sUpper) ) {
                return wd;
            }
            for ( String value : wd.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return wd;
                }
            }
        }
        throw new DbcException("Invalid Walk Direction: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}