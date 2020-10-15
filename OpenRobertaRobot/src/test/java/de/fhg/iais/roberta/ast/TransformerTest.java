package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.bean.NewUsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.ITransformerVisitor;
import de.fhg.iais.roberta.worker.AbstractTransformerWorker;

/**
 * These tests do not cover all the possibilities of block usage and combination, just some more complex generic programs that may provide a challenge to
 * transform blocks in.
 */
public class TransformerTest extends AstTest {

    @Test
    public void executeTransformer_ShouldReturnTransformedSensActAST_WhenGivenSensActAST() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=671,y=5],MainTask[],"
                + "DebugAction[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]]," // simple sensor usage
                + "MotorOnAction[MotorPort,MotionParam[speed=SensorExpr[TemperatureSensor[TempPort,TempMode,TempSlot]],duration=null]]]]]"; // simple actor + sensor in motionparam usage

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/ast/transform/old_sensor_actor.xml"))
                .setConfigurationAst(new ConfigurationAst.Builder().setXmlVersion("2.0").build())
                .build();

        new TestTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedGetSampleAST_WhenGivenGetSampleAST() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=671,y=5],MainTask[],"
                + "WaitStmt[(repeat[WAIT,SensorExpr[GetSampleSensor[TemperatureSensor[GetSamplePort,TEMPERATURE,GetSampleSlot]]]])]]]]";

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/ast/transform/old_get_sample.xml"))
                .setConfigurationAst(new ConfigurationAst.Builder().setXmlVersion("2.0").build())
                .build();

        new TestTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedDisabledAST_WhenGivenDisabledAST() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=671,y=5],MainTask[],"
                + "DebugAction[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]]]," // disabled block
                + "[Location[x=739,y=83],KeysSensor[KeysPort,KeysMode,KeysSlot]]]]"; // unused block

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/ast/transform/old_disabled.xml"))
                .setConfigurationAst(new ConfigurationAst.Builder().setXmlVersion("2.0").build())
                .build();

        new TestTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedControlAST_WhenGivenControlAST() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=671,y=5],MainTask[],"
                + "(repeat[UNTIL,Unary[NOT,SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]]]"
                + "(repeat[FOREVER,BoolConst[true]]DebugAction[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]])"
                + "(repeat[TIMES,Var[k0],NumConst[0],SensorExpr[TemperatureSensor[TempPort,TempMode,TempSlot]],NumConst[1]]"
                + "(repeat[FOR,Var[i],SensorExpr[TemperatureSensor[TempPort,TempMode,TempSlot]],SensorExpr[TemperatureSensor[TempPort,TempMode,TempSlot]],SensorExpr[TemperatureSensor[TempPort,TempMode,TempSlot]]]DebugAction[SensorExpr[TemperatureSensor[TempPort,TempMode,TempSlot]]]))),"
                + "ifSensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]],"
                + "thenifSensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]],"
                + "thenDebugAction[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]],"
                + "elseifSensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]],"
                + "thenDebugAction[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]],"
                + "elseDebugAction[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]],"
                + "WaitStmt[(repeat[WAIT,SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]])]]]]";

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/ast/transform/old_control.xml"))
                .setConfigurationAst(new ConfigurationAst.Builder().setXmlVersion("2.0").build())
                .build();

        new TestTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedFunctionsAST_WhenGivenFunctionsAST() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=671,y=5],MainTask[],"
                + "MethodCall[f1,,,VOID]],"
                + "[Location[x=169,y=122],MethodVoid[f1,,DebugAction[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]]DebugAction[MethodExpr[MethodCall[f2,,,BOOLEAN]]]]],"
                + "[Location[x=167,y=225],MethodReturn[f2,,MethodStmt[MethodIfReturn[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]],BOOLEAN,SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]]]DebugAction[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]],BOOLEAN,SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]]],"
                + "[Location[x=166,y=355],MethodVoid[unused,,DebugAction[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]]]]]]"; // unused function

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/ast/transform/old_functions.xml"))
                .setConfigurationAst(new ConfigurationAst.Builder().setXmlVersion("2.0").build())
                .build();

        new TestTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
    }

    @Test
    public void executeTransformer_ShouldReturnSameAST_WhenGivenNewerVersionAST() {
        String expectedProgramAst = "BlockAST[project=[[Location[x=671,y=5],MainTask[]," + "DebugAction[SensorExpr[KeysSensor[A,PRESSED,EMPTY_SLOT]]]]]]"; // simple sensor usage
        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/ast/transform/old_newer_version.xml"))
                .setConfigurationAst(new ConfigurationAst.Builder().setXmlVersion("3.0").build())
                .build();

        new TestTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        Assert.assertEquals("3.0", project.getProgramAst().getXmlVersion());
        Assert.assertEquals("3.0", project.getConfigurationAst().getXmlVersion());
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedAST_WhenGivenOlderVersionAST() {
        String expectedProgramAst = "BlockAST[project=[[Location[x=671,y=5],MainTask[]," + "DebugAction[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]]]]]"; // simple sensor usage
        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/ast/transform/old_older_version.xml"))
                .setConfigurationAst(new ConfigurationAst.Builder().setXmlVersion("1.0").build())
                .build();

        new TestTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        Assert.assertEquals("4.0", project.getProgramAst().getXmlVersion());
        Assert.assertEquals("4.0", project.getConfigurationAst().getXmlVersion());
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedAST_WhenGivenEmptyVersionAST() {
        String expectedProgramAst = "BlockAST[project=[[Location[x=671,y=5],MainTask[]," + "DebugAction[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]]]]]"; // simple sensor usage
        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/ast/transform/old_empty_version.xml"))
                .setConfigurationAst(new ConfigurationAst.Builder().setXmlVersion("").build())
                .build();

        new TestTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        Assert.assertEquals("4.0", project.getProgramAst().getXmlVersion());
        Assert.assertEquals("4.0", project.getConfigurationAst().getXmlVersion());
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedAST_WhenGivenNoVersionAST() {
        String expectedProgramAst = "BlockAST[project=[[Location[x=671,y=5],MainTask[]," + "DebugAction[SensorExpr[KeysSensor[KeysPort,KeysMode,KeysSlot]]]]]]"; // simple sensor usage
        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/ast/transform/old_no_version.xml"))
                .setConfigurationAst(new ConfigurationAst.Builder().setXmlVersion(null).build())
                .build();

        new TestTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        Assert.assertEquals("4.0", project.getProgramAst().getXmlVersion());
        Assert.assertEquals("4.0", project.getConfigurationAst().getXmlVersion());
    }

    @Test
    public void executeTransformer_ShouldReturnSameProgramAndConf_WhenNothingNeedsToBeTransformed() {
        String expectedProgramAst = "BlockAST[project=[[Location[x=512,y=50],MainTask[],(repeat[FOREVER_ARDU,BoolConst[true]]AktionStmt[PinWriteValueSensor[StringConst[Hallo]]])]]]"; // simple sensor usage
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent [componentType=ANALOG_INPUT, isActor=true, userDefinedName=A, portName=A, componentProperties={INPUT=3}]",
                "ConfigurationComponent [componentType=SERVOMOTOR, isActor=true, userDefinedName=S, portName=S, componentProperties={PULSE=1}]",
                "ConfigurationComponent [componentType=LED, isActor=true, userDefinedName=L, portName=L, componentProperties={INPUT=LED_BUILTIN}]"
            };
        Project project =
            UnitTestHelper
                .setupWithExportXML(testFactoryNewConf, Util.readResourceContent("/ast/transform/old_nothingneeded.xml"))
                .build();
        new TestTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
        Assert.assertEquals("3.0", project.getProgramAst().getXmlVersion());
        Assert.assertEquals("3.0", project.getConfigurationAst().getXmlVersion());
    }

    private static class TestTransformerVisitor implements ITransformerVisitor<Void> {

        private final BlocklyDropdownFactory blocklyDropdownFactory;

        private TestTransformerVisitor(BlocklyDropdownFactory blocklyDropdownFactory) {
            this.blocklyDropdownFactory = blocklyDropdownFactory;
        }

        @Override
        public BlocklyDropdownFactory getBlocklyDropdownFactory() {
            return this.blocklyDropdownFactory;
        }

        @Override
        public Phrase<Void> visitKeysSensor(KeysSensor<Phrase<Void>> keysSensor) {
            return KeysSensor
                .make(
                    new SensorMetaDataBean("KeysPort", "KeysMode", "KeysSlot", keysSensor.isPortInMutation()),
                    keysSensor.getProperty(),
                    keysSensor.getComment());
        }

        @Override
        public Phrase<Void> visitTemperatureSensor(TemperatureSensor<Phrase<Void>> temperatureSensor) {
            return TemperatureSensor
                .make(
                    new SensorMetaDataBean("TempPort", "TempMode", "TempSlot", temperatureSensor.isPortInMutation()),
                    temperatureSensor.getProperty(),
                    temperatureSensor.getComment());
        }

        @Override
        public Phrase<Void> visitMotorOnAction(MotorOnAction<Phrase<Void>> motorOnAction) {
            return MotorOnAction.make("MotorPort", modifyMotionParam(motorOnAction.getParam()), motorOnAction.getProperty(), motorOnAction.getComment());
        }

        @Override
        public Phrase<Void> visitGetSampleSensor(GetSampleSensor<Phrase<Void>> sensorGetSample) {
            return GetSampleSensor
                .make(
                    "TEMPERATURE_VALUE",
                    "GetSamplePort",
                    "GetSampleSlot",
                    sensorGetSample.isPortInMutation(),
                    sensorGetSample.getProperty(),
                    sensorGetSample.getComment(),
                    this.blocklyDropdownFactory);
        }
    }

    private static class TestTransformerWorker extends AbstractTransformerWorker {

        TestTransformerWorker() {
            super("0.0", "2.0", "4.0");
        }

        @Override
        protected ITransformerVisitor<Void> getVisitor(Project project, NewUsedHardwareBean.Builder builder, ConfigurationAst configuration) {
            return new TestTransformerVisitor(project.getRobotFactory().getBlocklyDropdownFactory());
        }
    }
}
