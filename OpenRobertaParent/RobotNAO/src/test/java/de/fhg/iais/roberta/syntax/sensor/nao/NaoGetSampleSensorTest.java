package de.fhg.iais.roberta.syntax.sensor.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class NaoGetSampleSensorTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleTouchHandLeftTouchHandBumperHead() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=188, y=138], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [NaoGetSampleSensor [Touchsensors [HAND, LEFT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [NaoGetSampleSensor [Touchsensors [HAND, RIGHT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [NaoGetSampleSensor [Touchsensors [BUMPER, RIGHT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [NaoGetSampleSensor [Touchsensors [BUMPER, LEFT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [NaoGetSampleSensor [Touchsensors [HEAD, FRONT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [NaoGetSampleSensor [Touchsensors [HEAD, LEFT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [NaoGetSampleSensor [Touchsensors [HEAD, RIGHT]]], BoolConst [true]]]\n"
                + ")]]]]";

        String result = this.h.generateTransformerString("/sensor/waitUntilTouch.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleTouchHandHeadBumper() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilTouch.xml");
    }

    @Test
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

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleTouchFaceDetect() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilDetectFace.xml");
    }

    @Test
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

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleDetectMarks() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilDetectMarks.xml");
    }

    @Test
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

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleSonar() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilSonar.xml");
    }

    @Test
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

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleGyro() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilGyro.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleAccelerometer() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=213, y=88], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [NaoGetSampleSensor [Accelerometer [X]]], NumConst [1]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [NaoGetSampleSensor [Accelerometer [Y]]], NumConst [1]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [NaoGetSampleSensor [Accelerometer [Z]]], NumConst [1]]]\n"
                + ")]]]]";

        String result = this.h.generateTransformerString("/sensor/waitUntilAccelerometer.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleAccelerometer() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilAccelerometer.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoGetSampleFSR() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=288, y=113], "
                + "MainTask [], "
                + "WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [NaoGetSampleSensor [ForceSensor [LEFT]]], NumConst [2]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [NaoGetSampleSensor [ForceSensor [RIGHT]]], NumConst [2]]]\n"
                + ")]]]]";

        String result = this.h.generateTransformerString("/sensor/waitUntilFSR.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoGetSampleFSR() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilFSR.xml");
    }

    @Test
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

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXMLNaoRecognizeWord() throws Exception {

        this.h.assertTransformationIsOk("/sensor/waitUntilRecognizeWord.xml");
    }
}