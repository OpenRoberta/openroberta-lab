package de.fhg.iais.roberta.syntax.sensor.vorwerk;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class WallTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfAnimationClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=349, y=50], "
                + "MainTask [\n"
                + "exprStmt VarDeclaration [NUMBER, item, NumConst [0], false, true]], \n"
                + "Var [item] := SensorExpr [WallSensor [NO_PORT, DISTANCE, EMPTY_SLOT]]\n"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensors/wall.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensors/wall.xml");
    }
}