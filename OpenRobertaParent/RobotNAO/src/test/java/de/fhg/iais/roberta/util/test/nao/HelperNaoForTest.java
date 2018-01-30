package de.fhg.iais.roberta.util.test.nao;

import de.fhg.iais.roberta.factory.nao.Factory;
import de.fhg.iais.roberta.util.RobertaProperties;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class HelperNaoForTest extends de.fhg.iais.roberta.util.test.AbstractHelperForTest {

    public HelperNaoForTest(RobertaProperties robertaProperties) {
        super(new Factory(robertaProperties));
    }
}
