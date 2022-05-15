package de.fhg.iais.roberta.syntax.check;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.AstTest;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.lang.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EvalExpr;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorAndCollectorWorker;

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

    public class TestLoopCounterWorker extends AbstractValidatorAndCollectorWorker {

        @Override
        protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project p, ClassToInstanceMap<IProjectBean.IBuilder<?>> bb) {
            return new TestLoopCounterVisitor(p.getConfigurationAst(), bb);
        }
    }

    public class TestLoopCounterVisitor extends CommonNepoValidatorAndCollectorVisitor {
        protected TestLoopCounterVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) //
        {
            super(robotConfiguration, beanBuilders);
        }

        @Override
        public Void visitActionExpr(ActionExpr<Void> actionExpr) {
            return super.visitActionExpr(actionExpr);
        }

        @Override
        public Void visitActionStmt(ActionStmt<Void> actionStmt) {
            return super.visitActionStmt(actionStmt);
        }

        @Override
        public Void visitActivityTask(ActivityTask<Void> activityTask) {
            return super.visitActivityTask(activityTask);
        }

        @Override
        public Void visitEvalExpr(EvalExpr<Void> evalExpr) {
            return super.visitEvalExpr(evalExpr);
        }

        @Override
        public Void visitExprStmt(ExprStmt<Void> exprStmt) {
            return super.visitExprStmt(exprStmt);
        }

        @Override
        public Void visitFunctionExpr(FunctionExpr<Void> functionExpr) {
            return super.visitFunctionExpr(functionExpr);
        }

        @Override
        public Void visitFunctionStmt(FunctionStmt<Void> functionStmt) {
            return super.visitFunctionStmt(functionStmt);
        }

        @Override
        public Void visitLocation(Location<Void> location) {
            return super.visitLocation(location);
        }

        @Override
        public Void visitMethodExpr(MethodExpr<Void> methodExpr) {
            return super.visitMethodExpr(methodExpr);
        }

        @Override
        public Void visitSensorExpr(SensorExpr<Void> sensorExpr) {
            return super.visitSensorExpr(sensorExpr);
        }

        @Override
        public Void visitSensorStmt(SensorStmt<Void> sensorStmt) {
            return super.visitSensorStmt(sensorStmt);
        }

        @Override
        public Void visitShadowExpr(ShadowExpr<Void> shadowExpr) {
            return super.visitShadowExpr(shadowExpr);
        }

        @Override
        public Void visitStartActivityTask(StartActivityTask<Void> startActivityTask) {
            return super.visitStartActivityTask(startActivityTask);
        }

        @Override
        public Void visitStmtExpr(StmtExpr<Void> stmtExpr) {
            return super.visitStmtExpr(stmtExpr);
        }

        @Override
        public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
            return null;
        }
    }
}
