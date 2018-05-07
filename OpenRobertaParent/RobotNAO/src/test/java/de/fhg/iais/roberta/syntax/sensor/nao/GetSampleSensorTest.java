package de.fhg.iais.roberta.syntax.sensor.nao;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class GetSampleSensorTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

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

        String result = this.h.generateTransformerString("/sensor/waitUntilAccelerometer.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleAccelerometer() throws Exception {
        this.h.assertTransformationIsOk("/sensor/waitUntilAccelerometer.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleElectricCurrent() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [HEAD, VALUE, YAW]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [HEAD, VALUE, PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [SHOULDER, VALUE, LEFT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [SHOULDER, VALUE, LEFT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [SHOULDER, VALUE, RIGHT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [SHOULDER, VALUE, RIGHT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [ELBOW, VALUE, LEFT_YAW]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [ELBOW, VALUE, LEFT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [ELBOW, VALUE, RIGHT_YAW]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [ELBOW, VALUE, RIGHT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [WRIST, VALUE, LEFT_YAW]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [WRIST, VALUE, RIGHT_YAW]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [HAND, VALUE, LEFT]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [HAND, VALUE, RIGHT]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [HIP, VALUE, LEFT_YAW_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [HIP, VALUE, LEFT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [HIP, VALUE, LEFT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [HIP, VALUE, RIGHT_YAW_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [HIP, VALUE, RIGHT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [HIP, VALUE, RIGHT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [KNEE, VALUE, LEFT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [KNEE, VALUE, RIGHT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [ANKLE, VALUE, LEFT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [ANKLE, VALUE, LEFT_ROLL]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [ANKLE, VALUE, RIGHT_PITCH]]], NumConst [30]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [LT, SensorExpr [GetSampleSensor [ElectricCurrent [ANKLE, VALUE, RIGHT_ROLL]]], NumConst [30]]]\n"
                + ")]]]]";

        String result = this.h.generateTransformerString("/sensor/waitUntilElectricCurrent.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleElectricCurrent() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilElectricCurrent.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleTouchHandLeftTouchHandBumperHead() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [HEAD, TOUCH, FRONT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [HEAD, TOUCH, MIDDLE]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [HEAD, TOUCH, REAR]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [HAND, TOUCH, LEFT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [HAND, TOUCH, RIGHT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [BUMPER, TOUCH, LEFT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [BUMPER, TOUCH, RIGHT]]], BoolConst [true]]]\n"
                + ")]]]]";

        String result = this.h.generateTransformerString("/sensor/waitUntilTouch.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleTouchHandHeadBumper() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilTouch.xml");
    }

    @Ignore
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleFaceDetect() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=63, y=138], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [NaoGetSampleSensor [DetectFace []]], StringConst [Roberta]]]\n"
                + ")]]]]";

        String result = this.h.generateTransformerString("/sensor/waitUntilDetectFace.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleTouchFaceDetect() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilDetectFace.xml");
    }

    @Ignore
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleDetectMarks() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=113, y=113], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [NaoGetSampleSensor [NaoMark []]], NumConst [114]]]\n"
                + ")]]]]";

        String result = this.h.generateTransformerString("/sensor/waitUntilDetectMarks.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleDetectMarks() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilDetectMarks.xml");
    }

    @Ignore
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleSonar() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=113, y=113], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [NaoGetSampleSensor [Sonar []]], NumConst [30]]]\n"
                + ")]]]]";

        String result = this.h.generateTransformerString("/sensor/waitUntilSonar.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleSonar() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilSonar.xml");
    }

    @Ignore
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleGyro() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=113, y=113], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [NaoGetSampleSensor [Gyrometer [X]]], NumConst [1]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [NaoGetSampleSensor [Gyrometer [Y]]], NumConst [1]]]\n"
                + ")]"
                + "]]]";

        String result = this.h.generateTransformerString("/sensor/waitUntilGyro.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleGyro() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilGyro.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleFSR() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=101, y=79], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [GetSampleSensor [FsrSensor [LEFT, VALUE, EMPTY_SLOT]]], NumConst [90]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [GetSampleSensor [FsrSensor [RIGHT, VALUE, EMPTY_SLOT]]], NumConst [90]]]\n"
                + ")]]]]";

        String result = this.h.generateTransformerString("/sensor/waitUntilFSR.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleFSR() throws Exception {
        this.h.assertTransformationIsOk("/sensor/waitUntilFSR.xml");
    }

    @Ignore
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleRecognizeWord() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=188, y=113], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [NaoGetSampleSensor [RecognizeWord [ListCreate [STRING, StringConst [OpenRoberta]]]]], StringConst [OpenRoberta]]]\n"
                + ")]]]]";

        String result = this.h.generateTransformerString("/sensor/waitUntilRecognizeWord.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoRecognizeWord() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilRecognizeWord.xml");
    }
}