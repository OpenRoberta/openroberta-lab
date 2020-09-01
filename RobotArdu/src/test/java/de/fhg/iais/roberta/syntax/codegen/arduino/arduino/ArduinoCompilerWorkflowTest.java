package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.compile.ArduinoCompilerWorker;
import static org.junit.Assert.fail;

public class ArduinoCompilerWorkflowTest {
    private static final String ORA_CC_RSC_ENVVAR = ServerProperties.CROSSCOMPILER_RESOURCE_BASE.replace('.', '_');
    private static final Logger LOG = LoggerFactory.getLogger(ArduinoActorTest.class);
    private RobotFactory unoFactory;

    private RobotFactory bob3Factory;
    private RobotFactory botnrollFactory;
    private RobotFactory mbotFactory;
    private RobotFactory senseboxFactory;

    @Ignore
    @Before
    public void init() {
        if ( System.getenv(ORA_CC_RSC_ENVVAR) == null ) {
            LOG.error("the environment variable \"" + ORA_CC_RSC_ENVVAR + "\" must contain the absolute path to the ora-cc-rsc repository - test fails");
            fail();
        }
        PluginProperties properties = new PluginProperties("uno", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util.loadProperties("classpath:/uno.properties"));
        this.unoFactory = new RobotFactory(properties);
        properties = new PluginProperties("nano", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util.loadProperties("classpath:/nano.properties"));
        properties = new PluginProperties("mega", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util.loadProperties("classpath:/mega.properties"));
        properties = new PluginProperties("bob3", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util.loadProperties("classpath:/bob3.properties"));
        this.bob3Factory = new RobotFactory(properties);
        properties = new PluginProperties("botnroll", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util.loadProperties("classpath:/botnroll.properties"));
        this.botnrollFactory = new RobotFactory(properties);
        properties = new PluginProperties("mbot", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util.loadProperties("classpath:/mbot.properties"));
        this.mbotFactory = new RobotFactory(properties);
    }

    @Ignore
    @Test
    public void arduinoCompilerWorkflowTest() throws Exception {
        String configurationXML = Util.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util.readResourceContent("/syntax/actions/action_program.xml");
        Project transformer = UnitTestHelper.setupWithConfigAndProgramXML(this.unoFactory, programXML, configurationXML).build();
        ArduinoCompilerWorker worker = new ArduinoCompilerWorker();
        worker.execute(transformer);
    }

    @Ignore
    @Test
    public void bob3CompilerWorkflowTest() throws Exception {
        String configurationXML = Util.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util.readResourceContent("/syntax/actions/action_program.xml");
        Project transformer = UnitTestHelper.setupWithConfigAndProgramXML(this.bob3Factory, programXML, configurationXML).build();
        ArduinoCompilerWorker worker = new ArduinoCompilerWorker();
        worker.execute(transformer);
    }

    @Ignore
    @Test
    public void mbotCompilerWorkflowTest() throws Exception {
        String configurationXML = Util.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util.readResourceContent("/syntax/actions/action_program.xml");
        Project transformer = UnitTestHelper.setupWithConfigAndProgramXML(this.mbotFactory, programXML, configurationXML).build();
        ArduinoCompilerWorker worker = new ArduinoCompilerWorker();
        worker.execute(transformer);
    }

    @Ignore
    @Test
    public void botnrollCompilerWorkflowTest() throws Exception {
        String configurationXML = Util.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util.readResourceContent("/syntax/actions/action_program.xml");
        Project transformer = UnitTestHelper.setupWithConfigAndProgramXML(this.botnrollFactory, programXML, configurationXML).build();
        ArduinoCompilerWorker worker = new ArduinoCompilerWorker();
        worker.execute(transformer);
    }

    @Ignore
    @Test
    public void senseboxCompilerWorkflowTest() throws Exception {
        String configurationXML = Util.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util.readResourceContent("/syntax/actions/action_program.xml");
        Project transformer = UnitTestHelper.setupWithConfigAndProgramXML(this.senseboxFactory, programXML, configurationXML).build();
        ArduinoCompilerWorker worker = new ArduinoCompilerWorker();
        worker.execute(transformer);
    }
}
