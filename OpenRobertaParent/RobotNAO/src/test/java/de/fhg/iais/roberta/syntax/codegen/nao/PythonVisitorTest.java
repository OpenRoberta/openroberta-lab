package de.fhg.iais.roberta.syntax.codegen.nao;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class PythonVisitorTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    @Test
    public void rgbColorVisit_returnsCorrectPythonCodeConvertingRgb2Hex() throws Exception {
        String correct_code = "item=BlocklyMethods.rgba2hex(0, 100, 68, 0)defrun():globalitem";

        this.h.assertCodeIsOk(correct_code, "/expr/create_rgb_variable.xml", false);
    }
}
