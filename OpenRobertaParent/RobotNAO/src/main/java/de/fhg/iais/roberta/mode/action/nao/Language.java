package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Language implements IMode {
    GERMAN(),
    ENGLISH(),
    FRENCH(),
    JAPANESE(),
    CHINESE(),
    SPANISH(),
    KOREAN(),
    ITALIAN(),
    DUTCH(),
    FINNISH(),
    POLISH(),
    RUSSIAN(),
    TURKISH(),
    ARABIC(),
    CZECH(),
    PORTUGUESE(),
    BRAZILIAN(),
    SWEDISH(),
    DANISH(),
    NORWEGIAN(),
    GREEK();

    private final String[] values;

    private Language(String... values) {
        this.values = values;
    }

    public static Language get(String language) {
        if ( language == null || language.isEmpty() ) {
            throw new DbcException("Invalid Language: " + language);
        }
        String sUpper = language.trim().toUpperCase(Locale.GERMAN);
        for ( Language l : Language.values() ) {
            if ( l.toString().equals(sUpper) ) {
                return l;
            }
            for ( String value : l.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return l;
                }
            }
        }
        throw new DbcException("Invalid Language: " + language);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}