package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SensorTest extends EdisonAstTest {

    private String insertIntoResult(String s) {
        return "BlockAST [project=" + s + "]";
    }

    //Sensors

    @Test
    public void TestKeySensor() throws Exception {
        String expected = insertIntoResult("[[Location [x=393, y=214], KeysSensor [PLAY, PRESSED, EMPTY_SLOT]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/sensor/key.xml");
    }

    @Test
    public void TestObstacleDetectorSensor() throws Exception {
        String expected =
            insertIntoResult(
                "[[Location [x=305, y=50], MainTask [\n"
                    + "exprStmt VarDeclaration [BOOLEAN, Element, SensorExpr [InfraredSensor [FRONT, OBSTACLE, EMPTY_SLOT]], false, true]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/sensor/obstacledetector.xml");
    }

    @Test
    public void TestInfraredSeekerSensor() throws Exception {
        String expected = insertIntoResult("[[Location [x=276, y=155], IRSeekerSensor [IRLED, RCCODE, EMPTY_SLOT]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/sensor/irseeker.xml");
    }

    @Test
    public void TestLightSensor() throws Exception {
        String expected = insertIntoResult("[[Location [x=342, y=171], LightSensor [LINETRACKER, LIGHT, EMPTY_SLOT]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/sensor/light.xml");
    }

    @Test
    public void TestSoundSensor() throws Exception {
        String expected = insertIntoResult("[[Location [x=385, y=192], SoundSensor [TOP, SOUND, EMPTY_SLOT]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/sensor/sound.xml");
    }
}
