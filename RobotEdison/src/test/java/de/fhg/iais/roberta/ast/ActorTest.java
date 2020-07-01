package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ActorTest extends EdisonAstTest {
    private String insertIntoResult(String s) {
        return "BlockAST [project=" + s + "]";
    }

    // Action -> Move

    @Test
    public void TestMotorOn() throws Exception {
        String expected = insertIntoResult("[[Location [x=413, y=189], MotorOnAction [RMOTOR, MotionParam [speed=NumConst [65], duration=null]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/motor_on.xml");
    }

    @Test
    public void TestMotorOnFor() throws Exception {
        String expected =
            insertIntoResult(
                "[[Location [x=389, y=144], MotorOnAction [LMOTOR, MotionParam [speed=NumConst [30], duration=MotorDuration [type=null, value=NumConst [1]]]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/motor_on_for.xml");
    }

    @Test
    public void TestMotorStop() throws Exception {
        String expected = insertIntoResult("[[Location [x=451, y=199], MotorStop [port=LMOTOR]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/motor_stop.xml");
    }

    // Action -> Drive

    @Test
    public void TestMotorDiffOnFor() throws Exception {
        String expected =
            insertIntoResult(
                "[[Location [x=396, y=177], DriveAction [FOREWARD, MotionParam [speed=NumConst [33], duration=MotorDuration [type=DISTANCE, value=NumConst [22]]]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/motordiff_on_for.xml");
    }

    @Test
    public void TestMotorDiffOn() throws Exception {
        String expected = insertIntoResult("[[Location [x=467, y=184], DriveAction [FOREWARD, MotionParam [speed=NumConst [30], duration=null]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/motordiff_on.xml");
    }

    @Test
    public void TestMotorDiffTurnFor() throws Exception {
        String expected =
            insertIntoResult(
                "[[Location [x=435, y=187], TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [30], duration=MotorDuration [type=DEGREE, value=NumConst [20]]]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/motordiff_turn_for.xml");
    }

    @Test
    public void TestMotorDiffTurn() throws Exception {
        String expected = insertIntoResult("[[Location [x=400, y=222], TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [30], duration=null]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/motordiff_turn.xml");
    }

    @Test
    public void TestMotorDiffCurveFor() throws Exception {
        String expected =
            insertIntoResult(
                "[[Location [x=342, y=185], CurveAction [FOREWARD, MotionParam [speed=NumConst [10], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]MotionParam [speed=NumConst [30], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/motordiff_curve_for.xml");
    }

    @Test
    public void TestMotorDiffCurve() throws Exception {
        String expected =
            insertIntoResult(
                "[[Location [x=369, y=233], CurveAction [FOREWARD, MotionParam [speed=NumConst [10], duration=null]MotionParam [speed=NumConst [30], duration=null]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/motordiff_curve.xml");
    }

    // Action -> Sounds

    @Test
    public void TestPlayTone() throws Exception {
        String expected = insertIntoResult("[[Location [x=472, y=173], ToneAction [NumConst [300], EmptyExpr [defVal=NUMBER_INT]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/play_tone.xml");
    }

    @Test
    public void TestPlayNote() throws Exception {
        String expected = insertIntoResult("[[Location [x=370, y=204], PlayNoteAction [ duration=2000, frequency=130.813]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/play_note.xml");
    }

    @Test
    public void TestPlayFile() throws Exception {
        String expected = insertIntoResult("[[Location [x=399, y=134], PlayFileAction [0]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/play_file.xml");
    }

    // Action -> Lights

    @Test
    public void TestLedOn() throws Exception {
        String expected = insertIntoResult("[[Location [x=432, y=211], LightAction [LLED, DEFAULT, DEFAULT, EmptyExpr [defVal=COLOR]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/led_on.xml");
    }

    @Test
    public void TestLedOff() throws Exception {
        String expected = insertIntoResult("[[Location [x=444, y=218], LightStatusAction [LLED, RESET]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/actor/led_off.xml");
    }

}
