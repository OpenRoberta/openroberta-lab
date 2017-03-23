package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.testutil.Helper;

public class ExceptionTest {

    @Test(expected = DbcException.class)
    public void valueException() throws Exception {
        Helper.assertTransformationIsOk("/ast/exceptions/value_exception.xml");
    }

}
