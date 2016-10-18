package de.fhg.iais.roberta.mode.action.calliope;

import de.fhg.iais.roberta.inter.mode.action.IDisplayImageMode;

public enum DisplayImageMode implements IDisplayImageMode {
    IMAGE(), ANIMATION();

    private final String[] values;

    private DisplayImageMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
