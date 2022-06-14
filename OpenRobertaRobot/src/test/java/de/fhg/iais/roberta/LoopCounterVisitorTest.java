package de.fhg.iais.roberta;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.TestLoopCounterWorker;

public class LoopCounterVisitorTest extends AstTest {
    
    @Test
    public void check_noLoops_returnsEmptyMap() throws Exception {
        Project.Builder builder = UnitTestHelper.setupWithProgramXMLWithDefaultConfig(testFactory, Util.readResourceContent("/loop_counter/no_loops.xml"));
        Project project = builder.build();

        TestLoopCounterWorker worker = new TestLoopCounterWorker();
        worker.execute(project);
        UsedHardwareBean bean = project.getWorkerResult(UsedHardwareBean.class);

        Assert.assertEquals("{}", bean.getLoopsLabelContainer().toString());
    }

    @Test
    public void check_nestedLoopsNoBreakorContinue_returnsMapWithTwoFalseElements() throws Exception {
        Project.Builder builder = UnitTestHelper.setupWithProgramXMLWithDefaultConfig(testFactory, Util.readResourceContent("/loop_counter/nested_loops.xml"));
        Project project = builder.build();

        TestLoopCounterWorker worker = new TestLoopCounterWorker();
        worker.execute(project);
        UsedHardwareBean bean = project.getWorkerResult(UsedHardwareBean.class);

        Assert.assertEquals("{1=false, 2=false}", bean.getLoopsLabelContainer().toString());
    }

    @Test
    public void check_loopsWithBreakAndContinue_returnsMapWithFiveFalseElements() throws Exception {
        Project.Builder builder = UnitTestHelper.setupWithProgramXMLWithDefaultConfig(testFactory, Util.readResourceContent("/loop_counter/loops_with_break_and_continue.xml"));
        Project project = builder.build();

        TestLoopCounterWorker worker = new TestLoopCounterWorker();
        worker.execute(project);
        UsedHardwareBean bean = project.getWorkerResult(UsedHardwareBean.class);

        Assert.assertEquals("{1=false, 2=false, 3=false, 4=false, 5=false}", bean.getLoopsLabelContainer().toString());
    }

    @Test
    public void check_loopWithBreakAndContinueInWait_returnsMapWithOneTrueElements() throws Exception {
        Project.Builder builder =
            UnitTestHelper.setupWithProgramXMLWithDefaultConfig(testFactory, Util.readResourceContent("/loop_counter/loop_with_break_and_continue_inside_wait.xml"));
        Project project = builder.build();

        TestLoopCounterWorker worker = new TestLoopCounterWorker();
        worker.execute(project);
        UsedHardwareBean bean = project.getWorkerResult(UsedHardwareBean.class);

        Assert.assertEquals("{1=true}", bean.getLoopsLabelContainer().toString());
    }

    @Test
    public void check_loopsWithBreakAndContinueFitstInWaitSecondNot_returnsMapWithTwoElementsFirsTrueSecondFalse() throws Exception {
        Project.Builder builder =
            UnitTestHelper.setupWithProgramXMLWithDefaultConfig(testFactory, Util.readResourceContent("/loop_counter/two_loop_with_break_and_continue_one_inside_wait_another_not.xml"));
        Project project = builder.build();

        TestLoopCounterWorker worker = new TestLoopCounterWorker();
        worker.execute(project);
        UsedHardwareBean bean = project.getWorkerResult(UsedHardwareBean.class);

        Assert.assertEquals("{1=true, 2=false}", bean.getLoopsLabelContainer().toString());
    }

    @Test
    public void check_twoNestedloopsFirstWithBreakAndContinueInWaitSecondNot_returnsMapWithTwoElementsFirsTrueSecondFalse() throws Exception {
        Project.Builder builder =
            UnitTestHelper.setupWithProgramXMLWithDefaultConfig(testFactory, Util.readResourceContent("/loop_counter/two_nested_loops_first_with_break_in_wait_second_not.xml"));
        Project project = builder.build();

        TestLoopCounterWorker worker = new TestLoopCounterWorker();
        worker.execute(project);
        UsedHardwareBean bean = project.getWorkerResult(UsedHardwareBean.class);

        Assert.assertEquals("{1=true, 2=false}", bean.getLoopsLabelContainer().toString());
    }

    @Test
    public void check_loopWithNestedTwoLoopsInsideWait_returnsMapWithThreeElementsFirsTrueSecondThirdFalse() throws Exception {
        Project.Builder builder =
            UnitTestHelper.setupWithProgramXMLWithDefaultConfig(testFactory, Util.readResourceContent("/loop_counter/loop_with_nested_two_loops_inside_wait.xml"));
        Project project = builder.build();

        TestLoopCounterWorker worker = new TestLoopCounterWorker();
        worker.execute(project);
        UsedHardwareBean bean = project.getWorkerResult(UsedHardwareBean.class);

        Assert.assertEquals("{1=true, 2=false, 3=false}", bean.getLoopsLabelContainer().toString());
    }

    @Test
    public void check_loopWithNestedTwoLoopsInsideWaitSecondContainWait_returnsMapWithThreeElementsFirsAndThirdTrueSecondFalse() throws Exception {
        Project.Builder builder =
            UnitTestHelper.setupWithProgramXMLWithDefaultConfig(testFactory, Util.readResourceContent("/loop_counter/loop_with_nested_two_loops_inside_wait_second_contain_wait.xml"));
        Project project = builder.build();

        TestLoopCounterWorker worker = new TestLoopCounterWorker();
        worker.execute(project);
        UsedHardwareBean bean = project.getWorkerResult(UsedHardwareBean.class);

        Assert.assertEquals("{1=true, 2=false, 3=true}", bean.getLoopsLabelContainer().toString());
    }

    @Test
    public void check_threeLoopsWithNestedTwoLoopsInsideWaitSecondContainWait_returnsMapWithFiveElementsFirsThirdFourthTrueSecondFifthFalse() throws Exception {
        Project.Builder builder =
            UnitTestHelper.setupWithProgramXMLWithDefaultConfig(testFactory, Util.readResourceContent("/loop_counter/three_loops_with_nested_two_loops_inside_wait_second_contain_wait.xml"));
        Project project = builder.build();

        TestLoopCounterWorker worker = new TestLoopCounterWorker();
        worker.execute(project);
        UsedHardwareBean bean = project.getWorkerResult(UsedHardwareBean.class);

        Assert.assertEquals("{1=true, 2=false, 3=true, 4=false, 5=true}", bean.getLoopsLabelContainer().toString());
    }

}
