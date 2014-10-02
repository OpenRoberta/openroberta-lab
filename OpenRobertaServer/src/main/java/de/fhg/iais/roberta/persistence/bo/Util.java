package de.fhg.iais.roberta.persistence.bo;

import java.sql.Timestamp;
import java.util.Date;

public class Util {
    private Util() {
        // no objects
    }

    public static Timestamp getNow() {
        return new Timestamp(new Date().getTime());
    }

}
