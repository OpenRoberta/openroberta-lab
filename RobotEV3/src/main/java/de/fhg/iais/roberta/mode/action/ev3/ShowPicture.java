package de.fhg.iais.roberta.mode.action.ev3;

import de.fhg.iais.roberta.inter.mode.action.IShowPicture;

public enum ShowPicture implements IShowPicture {

    OLDGLASSES( "BRILLE", "BrilleAugen Offen" ),
    EYESOPEN( "AUGENOFFEN", "" ),
    EYESCLOSED( "AUGENZU", "AUGEN ZU" ),
    FLOWERS( "BLUMEN", "Blumen" ),
    TACHO( "Tacho" ),
    STEERING("Steering"),
    CHECKMARK("Checkmark");

    private final String[] values;

    private ShowPicture(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}