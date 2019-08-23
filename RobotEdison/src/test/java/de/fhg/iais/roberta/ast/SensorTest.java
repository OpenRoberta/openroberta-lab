package de.fhg.iais.roberta.ast;

import de.fhg.iais.roberta.util.test.edison.HelperEdisonForXmlTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class SensorTest {


    HelperEdisonForXmlTest h = new HelperEdisonForXmlTest();

    private String insertIntoResult(String s) {
        return "BlockAST [project=" + s + "]";
    }

    //Sensors

    @Test
    public void TestKeySensor() throws Exception {
        String expected = insertIntoResult("[[Location [x=393, y=214], KeysSensor [PLAY, PRESSED, EMPTY_SLOT]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/sensor/key.xml"));
    }

    @Test
    public void TestObstacleDetectorSensor() throws Exception {
        String expected = insertIntoResult("[[Location [x=305, y=50], MainTask [\n"
            + "exprStmt VarDeclaration [BOOLEAN, Element, SensorExpr [InfraredSensor [FRONT, OBSTACLE, EMPTY_SLOT]], false, true]]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/sensor/obstacledetector.xml"));
    }

    @Test
    public void TestInfraredSeekerSensor() throws Exception {
        String expected = insertIntoResult("[[Location [x=276, y=155], IRSeekerSensor [IRLED, RCCODE, EMPTY_SLOT]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/sensor/irseeker.xml"));
    }

    @Test
    public void TestLightSensor() throws Exception {
        String expected = insertIntoResult("[[Location [x=342, y=171], LightSensor [LINETRACKER, LIGHT, EMPTY_SLOT]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/sensor/light.xml"));
    }

    @Test
    public void TestSoundSensor() throws Exception {
        String expected = insertIntoResult("[[Location [x=385, y=192], SoundSensor [TOP, SOUND, EMPTY_SLOT]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/sensor/sound.xml"));
    }
}
