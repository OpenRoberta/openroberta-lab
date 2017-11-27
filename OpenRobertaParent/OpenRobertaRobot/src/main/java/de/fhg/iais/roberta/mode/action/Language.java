package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.ILanguage;

public enum Language implements ILanguage {
    GERMAN(),
    ENGLISH(),
    SPANISH(),
    PORTUGUESE(),
    ITALIAN(),
    FRENCH(),
    CATALAN(),
    RUSSIAN(),
    CZECH(),
    POLISH(),
    TURKISH(),
    DUTCH(),
    FINNISH(),
    DANISH(),
    JAPANESE(),
    CHINESE(),
    KOREAN(),
    ARABIC(),
    BRAZILIAN(),
    SWEDISH(),
    NORWEGIAN(),
    GREEK();

    private final String[] values;

    private Language(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}