package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class SetLedsTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    @Test
    public void testLedsOnColor() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=163], SetLeds [EYES, ColorConst [#ffcc33], ], "
                + "SetLeds [LEFTEYE, ColorConst [#ffcc66], ], SetLeds [RIGHTEYE, ColorConst [#66cccc], ], "
                + "SetLeds [LEFTFOOT, ColorConst [#336666], ], SetLeds [RIGHTFOOT, ColorConst [#ccccff], ], "
                + "SetLeds [ALL, ColorConst [#663366], ]]]]";

        String result = this.h.generateTransformerString("/action/ledsOnColor.xml");

        Assert.assertEquals(expectedResult, result);
    }
}