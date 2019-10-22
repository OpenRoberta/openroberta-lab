package de.fhg.iais.roberta.syntax.sensor.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class GetSampleSensorTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleAccelerometer() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [GetSampleSensor [AccelerometerSensor [X, DEFAULT, EMPTY_SLOT]]], NumConst [512]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [GetSampleSensor [AccelerometerSensor [Y, DEFAULT, EMPTY_SLOT]]], NumConst [512]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [GetSampleSensor [AccelerometerSensor [Z, DEFAULT, EMPTY_SLOT]]], NumConst [512]]]\n"
                + ")]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/waitUntilAccelerometer.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleAccelerometer() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/waitUntilAccelerometer.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleElectricCurrent() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [HEAD, VALUE, YAW]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [HEAD, VALUE, PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [SHOULDER, VALUE, LEFT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [SHOULDER, VALUE, LEFT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [SHOULDER, VALUE, RIGHT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [SHOULDER, VALUE, RIGHT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [ELBOW, VALUE, LEFT_YAW]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [ELBOW, VALUE, LEFT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [ELBOW, VALUE, RIGHT_YAW]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [ELBOW, VALUE, RIGHT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [WRIST, VALUE, LEFT_YAW]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [WRIST, VALUE, RIGHT_YAW]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [HAND, VALUE, LEFT]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [HAND, VALUE, RIGHT]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [HIP, VALUE, LEFT_YAW_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [HIP, VALUE, LEFT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [HIP, VALUE, LEFT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [HIP, VALUE, RIGHT_YAW_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [HIP, VALUE, RIGHT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [HIP, VALUE, RIGHT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [KNEE, VALUE, LEFT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [KNEE, VALUE, RIGHT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [ANKLE, VALUE, LEFT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [ANKLE, VALUE, LEFT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [ANKLE, VALUE, RIGHT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrentSensor [ANKLE, VALUE, RIGHT_ROLL]]], NumConst [30]]]\n"
                + ")]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/waitUntilElectricCurrent.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleElectricCurrent() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/waitUntilElectricCurrent.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleTouchHandLeftTouchHandBumperHead() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [HEAD, PRESSED, FRONT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [HEAD, PRESSED, MIDDLE]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [HEAD, PRESSED, REAR]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [HAND, PRESSED, LEFT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [HAND, PRESSED, RIGHT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [BUMPER, PRESSED, LEFT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [BUMPER, PRESSED, RIGHT]]], BoolConst [true]]]\n"
                + ")]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/waitUntilTouch.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleTouchHandHeadBumper() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/waitUntilTouch.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleFaceDetect() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=349, y=50], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [DetectFaceSensor [NO_PORT, NAMEONE, EMPTY_SLOT]]], StringConst [Roberta]]]\n"
                + ")]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/waitUntilDetectFace.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleTouchFaceDetect() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/waitUntilDetectFace.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleDetectMarks() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=349, y=50], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [DetectMarkSensor [NO_PORT, IDONE, EMPTY_SLOT]]], NumConst [84]]]\n"
                + ")]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/waitUntilDetectMarks.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleDetectMarks() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/waitUntilDetectMarks.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleSonar() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=349, y=50], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [UltrasonicSensor [NO_PORT, DISTANCE, EMPTY_SLOT]]], NumConst [30]]]\n"
                + ")]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/waitUntilSonar.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleSonar() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/waitUntilSonar.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleGyro() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=349, y=50], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [GetSampleSensor [GyroSensor [X, ANGLE, EMPTY_SLOT]]], NumConst [90]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [GetSampleSensor [GyroSensor [Y, ANGLE, EMPTY_SLOT]]], NumConst [90]]]\n"
                + ")]"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/waitUntilGyro.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleGyro() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/waitUntilGyro.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleFSR() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=349, y=50], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [GetSampleSensor [FsrSensor [LEFT, VALUE, EMPTY_SLOT]]], NumConst [10]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [GetSampleSensor [FsrSensor [RIGHT, VALUE, EMPTY_SLOT]]], NumConst [10]]]\n"
                + ")]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/waitUntilFSR.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleFSR() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/waitUntilFSR.xml");
    }
}