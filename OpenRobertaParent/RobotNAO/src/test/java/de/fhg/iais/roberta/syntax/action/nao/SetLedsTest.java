package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class SetLedsTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    @Test
    public void testLedsOnColor() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=163], SetLeds [EYES, ColorHexString [#ffcc33], ], "
                + "SetLeds [LEFTEYE, ColorHexString [#ffcc66], ], SetLeds [RIGHTEYE, ColorHexString [#66cccc], ], "
                + "SetLeds [LEFTFOOT, ColorHexString [#336666], ], SetLeds [RIGHTFOOT, ColorHexString [#ccccff], ], "
                + "SetLeds [ALL, ColorHexString [#663366], ]]]]";

        String result = this.h.generateTransformerString("/action/ledsOnColor.xml");

        Assert.assertEquals(expectedResult, result);
    }
}