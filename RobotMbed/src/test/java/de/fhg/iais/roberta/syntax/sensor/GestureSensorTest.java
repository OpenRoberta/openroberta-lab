package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class GestureSensorTest extends AstTest {

    @Ignore("Test is ignored until next commit")
    @Test
    public void make_ByDefault_ReturnInstanceOfDisplayImageActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=187, y=38], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [GestureSensor [ FACE_DOWN ]]], "
                + "DisplayTextAction [TEXT, SensorExpr [GestureSensor [ LEFT ]]]"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/check_gesture.xml");

    }

    @Ignore("Test is ignored until next commit")
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/check_gesture.xml");
    }

}
