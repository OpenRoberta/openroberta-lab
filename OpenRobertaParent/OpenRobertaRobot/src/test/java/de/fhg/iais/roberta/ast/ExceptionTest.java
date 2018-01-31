package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class ExceptionTest {
    AbstractHelperForXmlTest h = new GenericHelperForXmlTest();

    @Test(expected = DbcException.class)
    public void valueException() throws Exception {
        this.h.assertTransformationIsOk("/ast/exceptions/value_exception.xml");
    }

}
