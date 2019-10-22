package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SetLedsTest extends NaoAstTest {

    @Test
    public void testLedsOnColor() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=163], SetLeds [EYES, ColorConst [#ffcc33], ], "
                + "SetLeds [LEFTEYE, ColorConst [#ffcc66], ], SetLeds [RIGHTEYE, ColorConst [#66cccc], ], "
                + "SetLeds [LEFTFOOT, ColorConst [#336666], ], SetLeds [RIGHTFOOT, ColorConst [#ccccff], ], "
                + "SetLeds [ALL, ColorConst [#663366], ]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/ledsOnColor.xml");

    }
}