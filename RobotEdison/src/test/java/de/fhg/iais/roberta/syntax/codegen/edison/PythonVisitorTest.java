package de.fhg.iais.roberta.syntax.codegen.edison;

import org.junit.Test;

import de.fhg.iais.roberta.ast.EdisonAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PythonVisitorTest extends EdisonAstTest {

    @Test
    public void visitSoundSensorTest() throws Exception {
        String expectedResult = "___item=True___item=Ed.ReadClapSensor()==Ed.CLAP_DETECTED";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/sensor/sound_sensor.xml", false);
    }

    /**
     * Tests forward and backward drive block
     *
     * @throws Exception
     */
    @Test
    public void visitDriveActionTest() throws Exception {
        String expectedResult = "_diffDrive(Ed.FORWARD,33,22)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/drive_1.xml", false);

        expectedResult = "_diffDrive(Ed.BACKWARD,33,22)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/drive_2.xml", false);
    }

    @Test
    public void visitMathOnListFunctTest() throws Exception {
        String expectedResult = "___item=Ed.List(3,[1,2,3])\n" + "___item2=0\n" + "___item2=sum(___item)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/math/math_on_list.xml", false);
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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/task/start.xml", true);
    }

    @Test
    public void visitMathNumPropFunctTest() throws Exception {
        //even
        String expectedResult = "___item=True\n\n___item=(42%2)==0";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/math/num_prop_1.xml", false);

        //odd
        expectedResult = "___item=True\n\n___item=(42%2)==1";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/math/num_prop_2.xml", false);

        //prime
        expectedResult = "___item=True\n\n___item=_isPrime(42)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/math/num_prop_3.xml", false);

        //positive
        expectedResult = "___item=True\n\n___item=42>0";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/math/num_prop_4.xml", false);

        //negative
        expectedResult = "___item=True\n\n___item=42<0";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/math/num_prop_5.xml", false);

        //divisible by
        expectedResult = "___item=True\n\n___item=(42%13)==0";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/math/num_prop_6.xml", false);
    }

    @Test
    public void visitListCreateTest() throws Exception {
        String expectedResult = "___item=Ed.List(4,[5,3,0,4])";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/lists/list_create.xml", false);
    }

    @Test
    public void visitKeysSensorTest() throws Exception {
        String expectedResult = "___item=True\n\n___item=Ed.ReadKeypad()==Ed.KEYPAD_ROUND\n___item=Ed.ReadKeypad()==Ed.KEYPAD_TRIANGLE";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/sensor/keys.xml", false);
    }

    @Test
    public void visitSensorResetTest() throws Exception {
        String expectedResult = "Ed.ReadObstacleDetection()Ed.ReadKeypad()Ed.ReadClapSensor()Ed.ReadRemote()Ed.ReadIRData()Ed.ReadRemote()Ed.ReadIRData()";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/sensor/reset.xml", false);
    }

    @Test
    public void visitInfraredSensorTest() throws Exception {
        String expectedResult =
            "___irvar=True"
                + "___irvar=_obstacleDetection(Ed.OBSTACLE_LEFT)"
                + "___irvar=_obstacleDetection(Ed.OBSTACLE_AHEAD)"
                + "___irvar=_obstacleDetection(Ed.OBSTACLE_RIGHT)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/sensor/infrared.xml", false);
    }

    @Test
    public void visitIRSeekerSensorTest() throws Exception {
        String expectedResult = "___item=0___item=_irSeek(1)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/sensor/irseeker.xml", false);
    }

    @Test
    public void visitLightSensorTest() throws Exception {
        String expectedResult = "___item=0" + "___item=Ed.ReadLeftLightLevel()/10" + "___item=Ed.ReadRightLightLevel()/10" + "___item=Ed.ReadLineTracker()/10";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/sensor/light.xml", false);
    }

    @Test
    public void visitSensorWaitUntilTest() throws Exception {
        String expectedResult =
            "whileTrue:if(Ed.ReadKeypad()==Ed.KEYPAD_TRIANGLE)==True:breakpasswhileTrue:if(Ed.ReadKeypad()==Ed.KEYPAD_ROUND)==True:breakpasswhileTrue:if(_obstacleDetection(Ed.OBSTACLE_LEFT))==True:breakpasswhileTrue:if(_obstacleDetection(Ed.OBSTACLE_RIGHT))==True:breakpasswhileTrue:if(_obstacleDetection(Ed.OBSTACLE_AHEAD))==True:breakpasswhileTrue:if(_irSeek(1))<30:breakpasswhileTrue:if(Ed.ReadLeftLightLevel()/10)<30:breakpasswhileTrue:if(Ed.ReadRightLightLevel()/10)<30:breakpasswhileTrue:if(Ed.ReadLineTracker()/10)<30:breakpasswhileTrue:if(Ed.ReadLineState()==Ed.LINE_ON_BLACK)==True:breakpasswhileTrue:if(Ed.ReadClapSensor()==Ed.CLAP_DETECTED)==True:breakpass";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/sensor/wait.xml", false);
    }

    @Test
    public void visitCurveActionTest() throws Exception {
        String expectedResult = "_diffCurve(Ed.BACKWARD,42,69,17)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/steer.xml", false);
    }

    @Test
    public void visitCurveActionUnlimitedTest() throws Exception {
        String expectedResult = "_diffCurve(Ed.FORWARD,10,30,Ed.DISTANCE_UNLIMITED)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/steer_unlimited.xml", false);
    }

    @Test
    public void visitTurnActionTest() throws Exception {
        String expectedResult = "_diffTurn(Ed.SPIN_RIGHT,42,13)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/turn.xml", false);
    }

    @Test
    public void visitTurnActionUnlimitedTest() throws Exception {
        String expectedResult = "_diffTurn(Ed.SPIN_RIGHT,42,Ed.DISTANCE_UNLIMITED)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/turn_unlimited.xml", false);
    }

    @Test
    public void visitMotorOnTest() throws Exception {
        String expectedResult = "_motorOn(0,23,Ed.DISTANCE_UNLIMITED)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/motor_on.xml", false);
    }

    @Test
    public void visitMotorStopTest() throws Exception {
        String expectedResult = "Ed.DriveRightMotor(Ed.STOP,Ed.SPEED_1,1)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/motor_stop.xml", false);
    }

    @Test
    public void visitMotorDriveStopTest() throws Exception {
        String expectedResult = "Ed.Drive(Ed.STOP,Ed.SPEED_1,1)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/motor_drive_stop.xml", false);
    }

    @Test
    public void visitLengthOfIsEmptyFunctTest() throws Exception {
        String expectedResult = "___item=len(Ed.List(0,[]))";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/lists/length_of_is_empty.xml", false);
    }

    @Test
    public void visitWaitTimeStmtTest() throws Exception {
        String expectedResult = "Ed.TimeWait(234,Ed.TIME_MILLISECONDS)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/task/wait_time_stmt.xml", false);
    }

    @Test
    public void visitListGetIndexTest() throws Exception {
        String expectedResult = "___item=Ed.List(1,[-30])___item2=0___item2=___item[0]";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/lists/get_index.xml", false);
    }

    @Test
    public void visitListSetIndexTest() throws Exception {
        String expectedResult = "___item=Ed.List(2,[3,9])\n\n___item[1]=7";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/lists/set_index.xml", false);
    }

    @Test
    public void visitLightActionTest() throws Exception {
        String expectedResult = "Ed.RightLed(Ed.ON)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/light_action.xml", false);
    }

    @Test
    public void visitLightStatusActionTest() throws Exception {
        String expectedResult = "Ed.RightLed(Ed.OFF)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/light_status_action.xml", false);
    }

    @Test
    public void visitToneActionTest() throws Exception {
        String expectedResult = "Ed.PlayTone(8000000/300,20)Ed.TimeWait(20,Ed.TIME_MILLISECONDS)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/tone_action.xml", false);
    }

    @Test
    public void visitPlayNoteActionTest() throws Exception {
        String expectedResult =
            "Ed.PlayTone(4000000/130,2000)Ed.TimeWait(2000,Ed.TIME_MILLISECONDS)"
                + "Ed.PlayTone(4000000/349,1000)Ed.TimeWait(1000,Ed.TIME_MILLISECONDS)"
                + "Ed.PlayTone(4000000/466,500)Ed.TimeWait(500,Ed.TIME_MILLISECONDS)"
                + "Ed.PlayTone(4000000/466,250)Ed.TimeWait(250,Ed.TIME_MILLISECONDS)"
                + "Ed.PlayTone(4000000/987,125)Ed.TimeWait(125,Ed.TIME_MILLISECONDS)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/play_note_action.xml", false);
    }

    @Test
    public void visitPlayFileActionTest() throws Exception {
        String expectedResult =
            "___soundfile5=Ed.TuneString(55,\"c4d4e4f4g2g2a4a4a4a4g2R2a4a4a4a4g2f4f4f4e2e2g4g4g4g4c1z\")"
                + "Ed.PlayTune(___soundfile5)"
                + "while(Ed.ReadMusicEnd()==Ed.MUSIC_NOT_FINISHED):"
                + "pass";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/actor/play_file_action.xml", false);
    }
}
