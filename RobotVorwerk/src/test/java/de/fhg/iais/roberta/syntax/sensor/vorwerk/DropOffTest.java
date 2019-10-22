package de.fhg.iais.roberta.syntax.sensor.vorwerk;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class DropOffTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfAnimationClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=349, y=50], "
                + "MainTask [\n"
                + "exprStmt VarDeclaration [NUMBER, item, NumConst [0], false, true]], \n"
                + "Var [item] := SensorExpr [DropOffSensor [LEFT_DROPOFF, DISTANCE, EMPTY_SLOT]]\n, \n"
                + "Var [item] := SensorExpr [DropOffSensor [RIGHT_DROPOFF, DISTANCE, EMPTY_SLOT]]\n"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensors/drop_off.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensors/drop_off.xml");
    }
}