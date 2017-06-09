package de.fhg.iais.roberta.util.test.nao;

import de.fhg.iais.roberta.factory.NAOFactory;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class Helper extends de.fhg.iais.roberta.util.test.Helper {

    public Helper() {
        super();
        this.robotFactory = new NAOFactory(null);
    }
}
