package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.ILanguage;

/**
 * Maps the ISO 639-1 language codes to the languages
 *
 * @author boonto
 */

public enum Language implements ILanguage {
    GERMAN( "de" ),
    ENGLISH( "en" ),
    SPANISH( "es" ),
    PORTUGUESE( "pt" ),
    ITALIAN( "it" ),
    FRENCH( "fr" ),
    CATALAN( "ca" ),
    RUSSIAN( "ru" ),
    CZECH( "cs" ),
    POLISH( "pl" ),
    TURKISH( "te" ),
    DUTCH( "nl" ),
    FINNISH( "fi" ),
    DANISH( "da" ),
    JAPANESE( "ja" ),
    CHINESE( "zh" ),
    KOREAN( "ko" ),
    ARABIC( "ar" ),
    BRAZILIAN( "pt" ),
    SWEDISH( "sv" ),
    NORWEGIAN( "no" ),
    GREEK( "el" ),
    NOTSUPPORTED( "" );

    private final String[] values;

    private Language(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

    public static Language findByAbbr(String abbr) {
        for ( Language v : values() ) {
            if ( v.values[0].equals(abbr) ) {
                return v;
            }
        }
        return NOTSUPPORTED;
    }
}