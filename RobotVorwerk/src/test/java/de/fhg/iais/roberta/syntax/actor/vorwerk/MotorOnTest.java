package de.fhg.iais.roberta.syntax.actor.vorwerk;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.VorwerkAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorOnTest extends VorwerkAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfAnimationClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], MotorOnAction [LEFT, MotionParam [speed=NumConst [30], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]], "
                + "MotorOnAction [RIGHT, MotionParam [speed=NumConst [30], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/actors/motor_on.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/actors/motor_on.xml");
    }
}