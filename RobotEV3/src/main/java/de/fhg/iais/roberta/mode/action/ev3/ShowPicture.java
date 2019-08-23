package de.fhg.iais.roberta.mode.action.ev3;

import de.fhg.iais.roberta.inter.mode.action.IShowPicture;

public class ShowPicture implements IShowPicture {

    private final String[] values;

    public ShowPicture(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}