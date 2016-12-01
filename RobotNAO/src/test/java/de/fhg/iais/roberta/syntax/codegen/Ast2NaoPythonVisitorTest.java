package de.fhg.iais.roberta.syntax.codegen;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.NAOConfiguration;
import de.fhg.iais.roberta.testutil.Helper;

public class Ast2NaoPythonVisitorTest {

    private static final String IMPORTS = "" //
        + "#!/usr/bin/python\n\n"
        + "import math\n"
        + "import time\n"
        + "from hal import Hal\n"
        + "h = Hal()\n\n"
        + "def run():\n";

    private static final String SUFFIX = "" //
        + "\ndef main():\n"
        + "    try:\n"
        + "        run()\n"
        + "    except Exception as e:\n"
        + "        h.say(\"Error!\")\n\n"
        + "if __name__ == \"__main__\":\n"
        + "    main()";

    private static Configuration brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        Configuration.Builder configuration = new NAOConfiguration.Builder();
        brickConfiguration = configuration.build();
    }

    @Test
    public void visitWalkDistance_ByDefault_ReturnsWalkForTwoMetersForwardPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.walk(2,0,0)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/walk_forwards.xml");
    }

    @Test
    public void visitWalkDistance_ByMissingNumber_ReturnsWalkForZeroMetersBackwardPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.walk(-0,0,0)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/walk_backward_missing_distance.xml");
    }

    @Test
    public void visitTurnDegrees_ByDefault_ReturnsTurnForTwentyDegreesLeftPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.turn(0,-20,0)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/turn_left.xml");
    }

    @Test
    public void visitTurnDegrees_ByMissingNumber_ReturnsTurnForTwentyDegreesRightPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.turn(0,0,0)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/turn_right_missing_degrees.xml");
    }

    @Test
    public void visitWalkTo_ByDefault_ReturnsWalkToZeroZeroZeroPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.walkTo(0,0,0)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/walk_to.xml");
    }

    @Test
    public void visitWalkTo_ByMissingNumber_ReturnsWalkToMissingCoordinatesPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.walkTo(0,0,0)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/walk_to_missing_coordinates.xml");
    }

    @Test
    public void visitStop_ByDefault_ReturnsStopPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.stop()\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/stop.xml");
    }

    @Test
    public void visitStandUp_ByDefault_ReturnsStandUpPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.standUp()\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/standUp.xml");
    }

    @Test
    public void visitSitDown_ByDefault_ReturnsSitDownPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.sitDown()\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/sitDown.xml");
    }

    @Test
    public void visitTaiChi_ByDefault_ReturnsTaiChiPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.taiChi()\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/taiChi.xml");
    }

    @Test
    public void visitWave_ByDefault_ReturnsWavePythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.wave()\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/wave.xml");
    }

    @Test
    public void visitWipeForehead_ByDefault_ReturnsWipeForeheadPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.wipeForehead()\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/wipeForehead.xml");
    }

    @Test
    public void visitApplyPosture_ByDefault_ReturnsApplyPostureStandPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.applyPosture(\"Stand\")\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/posture_stand.xml");
    }

    @Test
    public void visitStiffnessOn_ByDefault_ReturnsStiffnessOnPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.stiffnessOn()\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/stiffnessOn.xml");
    }

    @Test
    public void visitStiffnessOff_ByDefault_ReturnsStiffnessOffPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.stiffnessOff()\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/stiffnessOff.xml");
    }

    @Test
    public void visitLookAt_ByDefault_ReturnsLookAtTorsoZeroZeroZeroZeroPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.lookAt(0, 0, 0, \'torso\', 0)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/lookAt_torso.xml");
    }

    @Test
    public void visitLookAt_ByMissingNumber_ReturnsLookAtWorldZeroZeroZeroZeroPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.lookAt(0, 0, 0, \'world\', 0)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/lookAt_world_missing_coordinates.xml");
    }

    @Test
    public void visitpointAt_ByDefault_ReturnsLookAtTorsoZeroZeroZeroZeroPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.pointAt(0, 0, 0, \'torso\', 0)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/pointAt_torso.xml");
    }

    @Test
    public void visitpointAt_ByMissingNumber_ReturnsLookAtWorldZeroZeroZeroZeroPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.pointAt(0, 0, 0, \'world\', 0)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/pointAt_world_missing_coordinates.xml");
    }

    @Test
    public void visitPartialStiffnessOn_ByDefault_ReturnsPartialStiffnessOnPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.partialStiffnessOn(\"LArm\")\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/partialStiffnessOn_larm.xml");
    }

    @Test
    public void visitPartialStiffnessOff_ByDefault_ReturnsPartialStiffnessOffPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.partialStiffnessOff(\"LArm\")\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/partialStiffnessOff_larm.xml");
    }

    @Test
    public void visitSetVolume_ByDefault_ReturnsSetVolumePythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.setVolume(50)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/setVolume.xml");
    }

    @Test
    public void visitSetEyeColor_ByDefault_ReturnsSetEyeColorGreenPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.setEyeColor(\'green\')\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/setEyeColor.xml");
    }

    @Test
    public void visitSetEarIntensity_ByDefault_ReturnsSetEarIntensityPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.setEarIntensity(50)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/setEarIntensity.xml");
    }

    @Test
    public void visitBlink_ByDefault_ReturnsBlinkPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.blink()\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/blink.xml");
    }

    @Test
    public void visitLedOff_ByDefault_ReturnsLedOffPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.ledOff()\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/ledOff.xml");
    }

    @Test
    public void visitLedReset_ByDefault_ReturnsLedResetPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.ledReset()\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/ledReset.xml");
    }

    @Test
    public void visitRandomEyesDuration_ByDefault_ReturnsRandomEyesDurationTwoPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.randomEyes(2)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/randomEyes.xml");
    }

    @Test
    public void visitRastaDuration_ByDefault_ReturnsRastaDurationTwoPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.rasta(2)\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/rasta.xml");
    }

    @Test
    public void visitSetLanguage_ByDefault_ReturnsSetLanguageEnglishPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.setLanguage(\"English\")\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/setLanguage_English.xml");
    }

    @Test
    public void visitSayText_ByDefault_ReturnsSayTextHelloPythonScript() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "    h.say(\"Hello\")\n"
            + SUFFIX;

        assertCodeIsOk(expectedResult, "/action/sayText.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        String b = Helper.generatePython(fileName, brickConfiguration);
        Assert.assertEquals(a, b);
        //Assert.assertEquals(a.replaceAll("\\s+", ""), b.replaceAll("\\s+", ""));
    }
}
