package de.fhg.iais.roberta.syntax.codegen.edison;

import de.fhg.iais.roberta.util.test.edison.HelperEdisonForXmlTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class PythonVisitorTest {
    private final HelperEdisonForXmlTest h = new HelperEdisonForXmlTest();

    @Test
    public void visitSoundSensorTest() throws Exception {
        String expectedResult = "item=Trueitem=Ed.ReadClapSensor()==Ed.CLAP_DETECTED";
        h.assertCodeIsOk(expectedResult, "/syntax/sensor/sound_sensor.xml");
    }

    /**
     * Tests forward and backward drive block
     * @throws Exception
     */
    @Test
    public void visitDriveActionTest() throws Exception {
        String expectedResult = "_diffDrive(Ed.FORWARD,33,22)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/drive_1.xml");

        expectedResult = "_diffDrive(Ed.BACKWARD,33,22)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/drive_2.xml");
    }

    @Test
    public void visitMathOnListFunctTest() throws Exception {
        String expectedResult =
            "item=Ed.List(3,[1,2,3])\n"
            + "item2=0\n"
            + "item2=sum(item)";
        h.assertCodeIsOk(expectedResult, "/syntax/math/math_on_list.xml");
    }

    @Test
    public void visitMainTaskTest() throws Exception {
        String expectedResult =
                  "import Ed\n"
                      + "Ed.EdisonVersion = Ed.V2\n"
                      + "Ed.DistanceUnits = Ed.CM\n"
                      + "Ed.Tempo = Ed.TEMPO_SLOW\n"
                      + "obstacleDetectionOn = False\n"
                      + "Ed.LineTrackerLed(Ed.ON)\n"
                      + "Ed.ReadClapSensor()\n"
                      + "Ed.ReadLineState()\n"
                      + "Ed.TimeWait(250, Ed.TIME_MILLISECONDS)\n"
                      + "\n"
                      + "\n"
                      + "\n";

        String generatedResult = h.generatePython("/syntax/task/start.xml");
        Assert.assertEquals(expectedResult, generatedResult);
    }

    @Test
    public void visitMathNumPropFunctTest() throws Exception {
        //even
        String expectedResult = "item=True\n\nitem=(42%2)==0";
        h.assertCodeIsOk(expectedResult, "/syntax/math/num_prop_1.xml");

        //odd
        expectedResult = "item=True\n\nitem=(42%2)==1";
        h.assertCodeIsOk(expectedResult, "/syntax/math/num_prop_2.xml");

        //prime
        expectedResult = "item=True\n\nitem=_isPrime(42)";
        h.assertCodeIsOk(expectedResult, "/syntax/math/num_prop_3.xml");

        //positive
        expectedResult = "item=True\n\nitem=42>0";
        h.assertCodeIsOk(expectedResult, "/syntax/math/num_prop_4.xml");

        //negative
        expectedResult = "item=True\n\nitem=42<0";
        h.assertCodeIsOk(expectedResult, "/syntax/math/num_prop_5.xml");

        //divisible by
        expectedResult = "item=True\n\nitem=(42%13)==0";
        h.assertCodeIsOk(expectedResult, "/syntax/math/num_prop_6.xml");
    }

    @Test
    public void visitListCreateTest() throws Exception {
        String expectedResult = "item=Ed.List(4,[5,3,0,4])";
        h.assertCodeIsOk(expectedResult, "/syntax/lists/list_create.xml");
    }

    @Test
    public void visitKeysSensorTest() throws Exception {
        String expectedResult = "item=True\n\nitem=Ed.ReadKeypad()==Ed.KEYPAD_ROUND\nitem=Ed.ReadKeypad()==Ed.KEYPAD_TRIANGLE";
        h.assertCodeIsOk(expectedResult, "/syntax/sensor/keys.xml");
    }

    @Test
    public void visitSensorResetTest() throws Exception {
        String expectedResult = "Ed.ReadObstacleDetection()Ed.ReadKeypad()Ed.ReadClapSensor()Ed.ReadRemote()Ed.ReadIRData()Ed.ReadRemote()Ed.ReadIRData()";
        h.assertCodeIsOk(expectedResult, "/syntax/sensor/reset.xml");
    }

    @Test
    public void visitInfraredSensorTest() throws Exception {
        String expectedResult =
              "irvar=True"
            + "irvar=_obstacleDetection(Ed.OBSTACLE_LEFT)"
            + "irvar=_obstacleDetection(Ed.OBSTACLE_AHEAD)"
            + "irvar=_obstacleDetection(Ed.OBSTACLE_RIGHT)";
        h.assertCodeIsOk(expectedResult, "/syntax/sensor/infrared.xml");
    }

    @Test
    public void visitIRSeekerSensorTest() throws Exception {
        String expectedResult = "item=0item=_irSeek(1)";
        h.assertCodeIsOk(expectedResult, "/syntax/sensor/irseeker.xml");
    }

    @Test
    public void visitLightSensorTest() throws Exception {
        String expectedResult = "item=0"
            + "item=Ed.ReadLeftLightLevel()/10"
            + "item=Ed.ReadRightLightLevel()/10"
            + "item=Ed.ReadLineTracker()/10";
        h.assertCodeIsOk(expectedResult, "/syntax/sensor/light.xml");
    }

    @Test
    public void visitSensorWaitUntilTest() throws Exception {
        String expectedResult =
              "whileTrue:if(Ed.ReadKeypad()==Ed.KEYPAD_TRIANGLE)==True:breakpasswhileTrue:if(Ed.ReadKeypad()==Ed.KEYPAD_ROUND)==True:breakpasswhileTrue:if(_obstacleDetection(Ed.OBSTACLE_LEFT))==True:breakpasswhileTrue:if(_obstacleDetection(Ed.OBSTACLE_RIGHT))==True:breakpasswhileTrue:if(_obstacleDetection(Ed.OBSTACLE_AHEAD))==True:breakpasswhileTrue:if(_irSeek(1))<30:breakpasswhileTrue:if(Ed.ReadLeftLightLevel()/10)<30:breakpasswhileTrue:if(Ed.ReadRightLightLevel()/10)<30:breakpasswhileTrue:if(Ed.ReadLineTracker()/10)<30:breakpasswhileTrue:if(Ed.ReadLineState()==Ed.LINE_ON_BLACK)==True:breakpasswhileTrue:if(Ed.ReadClapSensor()==Ed.CLAP_DETECTED)==True:breakpass";
        h.assertCodeIsOk(expectedResult, "/syntax/sensor/wait.xml");
    }

    @Test
    public void visitCurveActionTest() throws Exception {
        String expectedResult = "_diffCurve(Ed.BACKWARD,42,69,17)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/steer.xml");
    }

    @Test
    public void visitCurveActionUnlimitedTest() throws Exception {
        String expectedResult = "_diffCurve(Ed.FORWARD,10,30,Ed.DISTANCE_UNLIMITED)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/steer_unlimited.xml");
    }

    @Test
    public void visitTurnActionTest() throws Exception {
        String expectedResult = "_diffTurn(Ed.SPIN_RIGHT,42,13)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/turn.xml");
    }

    @Test
    public void visitTurnActionUnlimitedTest() throws Exception {
        String expectedResult = "_diffTurn(Ed.SPIN_RIGHT,42,Ed.DISTANCE_UNLIMITED)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/turn_unlimited.xml");
    }

    @Test
    public void visitMotorOnTest() throws Exception {
        String expectedResult = "_motorOn(0,23,Ed.DISTANCE_UNLIMITED)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/motor_on.xml");
    }

    @Test
    public void visitMotorStopTest() throws Exception {
        String expectedResult = "Ed.DriveRightMotor(Ed.STOP,Ed.SPEED_1,1)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/motor_stop.xml");
    }

    @Test
    public void visitMotorDriveStopTest() throws Exception {
        String expectedResult = "Ed.Drive(Ed.STOP,Ed.SPEED_1,1)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/motor_drive_stop.xml");
    }

    @Test
    public void visitLengthOfIsEmptyFunctTest() throws Exception {
        String expectedResult = "item=len(Ed.List(0,[]))";
        h.assertCodeIsOk(expectedResult, "/syntax/lists/length_of_is_empty.xml");
    }

    @Test
    public void visitWaitTimeStmtTest() throws Exception {
        String expectedResult = "Ed.TimeWait(234,Ed.TIME_MILLISECONDS)";
        h.assertCodeIsOk(expectedResult, "/syntax/task/wait_time_stmt.xml");
    }

    @Test
    public void visitListGetIndexTest() throws Exception {
        String expectedResult = "item=Ed.List(1,[-30])item2=0item2=item[0]";
        h.assertCodeIsOk(expectedResult, "/syntax/lists/get_index.xml");
    }

    @Test
    public void visitListSetIndexTest() throws Exception {
        String expectedResult = "item=Ed.List(2,[3,9])\n\nitem[1]=7";
        h.assertCodeIsOk(expectedResult, "/syntax/lists/set_index.xml");
    }

    @Test
    public void visitLightActionTest() throws Exception {
        String expectedResult = "Ed.RightLed(Ed.ON)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/light_action.xml");
    }

    @Test
    public void visitLightStatusActionTest() throws Exception {
        String expectedResult = "Ed.RightLed(Ed.OFF)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/light_status_action.xml");
    }

    @Test
    public void visitToneActionTest() throws Exception {
        String expectedResult = "Ed.PlayTone(8000000/300,0)Ed.TimeWait(0,Ed.TIME_MILLISECONDS)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/tone_action.xml");
    }

    @Test
    public void visitPlayNoteActionTest() throws Exception {
        String expectedResult = "Ed.PlayTone(4000000/130,2000)Ed.TimeWait(2000,Ed.TIME_MILLISECONDS)"
            + "Ed.PlayTone(4000000/349,1000)Ed.TimeWait(1000,Ed.TIME_MILLISECONDS)"
            + "Ed.PlayTone(4000000/466,500)Ed.TimeWait(500,Ed.TIME_MILLISECONDS)"
            + "Ed.PlayTone(4000000/466,250)Ed.TimeWait(250,Ed.TIME_MILLISECONDS)"
            + "Ed.PlayTone(4000000/987,125)Ed.TimeWait(125,Ed.TIME_MILLISECONDS)";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/play_note_action.xml");
    }

    @Test
    public void visitPlayFileActionTest() throws Exception {
        String expectedResult =
              "___soundfile5=Ed.TuneString(55,\"c4d4e4f4g2g2a4a4a4a4g2R2a4a4a4a4g2f4f4f4e2e2g4g4g4g4c1z\")"
            + "Ed.PlayTune(___soundfile5)"
            + "while(Ed.ReadMusicEnd()==Ed.MUSIC_NOT_FINISHED):"
                + "pass";
        h.assertCodeIsOk(expectedResult, "/syntax/actor/play_file_action.xml");
    }
}
