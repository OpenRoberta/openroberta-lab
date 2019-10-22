package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class AutonomousLifeOffTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfAutonomousClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "SetStiffness [, OFF]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/autonomousOff.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/autonomousOff.xml");
    }
}