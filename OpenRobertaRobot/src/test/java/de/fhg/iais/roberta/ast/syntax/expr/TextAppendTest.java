package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class TextAppendTest {
    @Test
    public void Test() throws Exception {
        String a = "item+String.valueOf(hal.isPressed(SensorPort.S1))item+String.valueOf(0)item+String.valueOf(\"aaa\")";

        Helper.assertCodeIsOk(a, "/syntax/text/text_append.xml");
    }
}
