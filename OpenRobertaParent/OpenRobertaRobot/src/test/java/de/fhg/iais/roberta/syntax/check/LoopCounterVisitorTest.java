package de.fhg.iais.roberta.syntax.check;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.hardware.RobotUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;

public class LoopCounterVisitorTest {
    AbstractHelperForXmlTest h = new GenericHelperForXmlTest();

    private class TestUsedHardware extends RobotUsedHardwareCollectorVisitor {
        //TODO create fake for this class
        public TestUsedHardware(ArrayList<ArrayList<Phrase<Void>>> phrases) {
            super(null);
            check(phrases);
        }

        @Override
        public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
            return null;
        }
    }

    @Test
    public void check_noLoops_returnsEmptyMap() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/loop_counter/no_loops.xml");

        TestUsedHardware checkVisitor = new TestUsedHardware(phrases);
        Assert.assertEquals("{}", checkVisitor.getloopsLabelContainer().toString());
    }

    @Test
    public void check_nestedLoopsNoBreakorContinue_returnsMapWithTwoFalseElements() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/loop_counter/nested_loops.xml");

        TestUsedHardware checkVisitor = new TestUsedHardware(phrases);

        Assert.assertEquals("{1=false, 2=false}", checkVisitor.getloopsLabelContainer().toString());
    }

    @Test
    public void check_loopsWithBreakAndContinue_returnsMapWithFiveFalseElements() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/loop_counter/loops_with_break_and_continue.xml");

        TestUsedHardware checkVisitor = new TestUsedHardware(phrases);

        Assert.assertEquals("{1=false, 2=false, 3=false, 4=false, 5=false}", checkVisitor.getloopsLabelContainer().toString());
    }

    @Test
    public void check_loopWithBreakAndContinueInWait_returnsMapWithOneTrueElements() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/loop_counter/loop_with_break_and_continue_inside_wait.xml");

        TestUsedHardware checkVisitor = new TestUsedHardware(phrases);

        Assert.assertEquals("{1=true}", checkVisitor.getloopsLabelContainer().toString());
    }

    @Test
    public void check_loopsWithBreakAndContinueFitstInWaitSecondNot_returnsMapWithTwoElementsFirsTrueSecondFalse() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/loop_counter/two_loop_with_break_and_continue_one_inside_wait_another_not.xml");

        TestUsedHardware checkVisitor = new TestUsedHardware(phrases);

        Assert.assertEquals("{1=true, 2=false}", checkVisitor.getloopsLabelContainer().toString());
    }

    @Test
    public void check_twoNestedloopsFirstWithBreakAndContinueInWaitSecondNot_returnsMapWithTwoElementsFirsTrueSecondFalse() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/loop_counter/two_nested_loops_first_with_break_in_wait_second_not.xml");

        TestUsedHardware checkVisitor = new TestUsedHardware(phrases);

        Assert.assertEquals("{1=true, 2=false}", checkVisitor.getloopsLabelContainer().toString());
    }

    @Test
    public void check_loopWithNestedTwoLoopsInsideWait_returnsMapWithThreeElementsFirsTrueSecondThirdFalse() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/loop_counter/loop_with_nested_two_loops_inside_wait.xml");

        TestUsedHardware checkVisitor = new TestUsedHardware(phrases);

        Assert.assertEquals("{1=true, 2=false, 3=false}", checkVisitor.getloopsLabelContainer().toString());
    }

    @Test
    public void check_loopWithNestedTwoLoopsInsideWaitSecondContainWait_returnsMapWithThreeElementsFirsAndThirdTrueSecondFalse() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/loop_counter/loop_with_nested_two_loops_inside_wait_second_contain_wait.xml");

        TestUsedHardware checkVisitor = new TestUsedHardware(phrases);

        Assert.assertEquals("{1=true, 2=false, 3=true}", checkVisitor.getloopsLabelContainer().toString());
    }

    @Test
    public void check_threeLoopsWithNestedTwoLoopsInsideWaitSecondContainWait_returnsMapWithFiveElementsFirsThirdFourthTrueSecondFifthFalse() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/loop_counter/three_loops_with_nested_two_loops_inside_wait_second_contain_wait.xml");

        TestUsedHardware checkVisitor = new TestUsedHardware(phrases);

        Assert.assertEquals("{1=true, 2=false, 3=true, 4=false, 5=true}", checkVisitor.getloopsLabelContainer().toString());
    }
}
