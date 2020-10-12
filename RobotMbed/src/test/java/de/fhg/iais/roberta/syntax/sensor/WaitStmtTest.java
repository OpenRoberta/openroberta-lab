package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class WaitStmtTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfGetSampleSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=80, y=92], MainTask [], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [KeysSensor [A, PRESSED, EMPTY_SLOT]]], BoolConst [true]]]\n"
                + ")], DisplayTextAction [TEXT, StringConst [Hallo]], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [GetSampleSensor [GyroSensor [_G, ANGLE, X]]], NumConst [90]]]\n"
                + ")], DisplayTextAction [TEXT, StringConst [Hallo]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/wait_stmt_two_cases.xml");
    }

    // TODO: add generation of empty slot in xml
    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/wait_stmt_two_cases.xml");
    }

}
