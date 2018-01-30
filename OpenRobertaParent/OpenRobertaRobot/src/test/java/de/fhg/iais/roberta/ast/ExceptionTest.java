package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.test.GenericHelper;
import de.fhg.iais.roberta.util.test.AbstractHelperForTest;

public class ExceptionTest {
    AbstractHelperForTest h = new GenericHelper();

    @Test(expected = DbcException.class)
    public void valueException() throws Exception {
        this.h.assertTransformationIsOk("/ast/exceptions/value_exception.xml");
    }

}
