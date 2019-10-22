package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.Bob3Factory;
import de.fhg.iais.roberta.factory.BotnrollFactory;
import de.fhg.iais.roberta.factory.MbotFactory;
import de.fhg.iais.roberta.factory.SenseboxFactory;
import de.fhg.iais.roberta.factory.UnoFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.compile.ArduinoCompilerWorker;

public class ArduinoCompilerWorkflowTest {
    private static final String ORA_CC_RSC_ENVVAR = ServerProperties.CROSSCOMPILER_RESOURCE_BASE.replace('.', '_');
    private static final Logger LOG = LoggerFactory.getLogger(ArduinoActorTest.class);
    private UnoFactory unoFactory;

    private Bob3Factory bob3Factory;
    private BotnrollFactory botnrollFactory;
    private MbotFactory mbotFactory;
    private SenseboxFactory senseboxFactory;

    @Ignore
    @Before
    public void init() {
        if ( System.getenv(ORA_CC_RSC_ENVVAR) == null ) {
            LOG.error("the environment variable \"" + ORA_CC_RSC_ENVVAR + "\" must contain the absolute path to the ora-cc-rsc repository - test fails");
            fail();
        }
        PluginProperties properties = new PluginProperties("uno", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/uno.properties"));
        this.unoFactory = new UnoFactory(properties);
        properties = new PluginProperties("nano", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/nano.properties"));
        properties = new PluginProperties("mega", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/mega.properties"));
        properties = new PluginProperties("bob3", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/bob3.properties"));
        this.bob3Factory = new Bob3Factory(properties);
        properties = new PluginProperties("botnroll", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/botnroll.properties"));
        this.botnrollFactory = new BotnrollFactory(properties);
        properties = new PluginProperties("mbot", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/mbot.properties"));
        this.mbotFactory = new MbotFactory(properties);
    }

    @Ignore
    @Test
    public void arduinoCompilerWorkflowTest() throws Exception {
        String configurationXML = Util1.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util1.readResourceContent("/syntax/actions/action_program.xml");
        Project transformer = UnitTestHelper.setupWithConfigurationAndProgramXML(this.unoFactory, programXML, configurationXML).build();
        ArduinoCompilerWorker worker = new ArduinoCompilerWorker();
        worker.execute(transformer);
    }

    @Ignore
    @Test
    public void bob3CompilerWorkflowTest() throws Exception {
        String configurationXML = Util1.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util1.readResourceContent("/syntax/actions/action_program.xml");
        Project transformer = UnitTestHelper.setupWithConfigurationAndProgramXML(this.bob3Factory, programXML, configurationXML).build();
        ArduinoCompilerWorker worker = new ArduinoCompilerWorker();
        worker.execute(transformer);
    }

    @Ignore
    @Test
    public void mbotCompilerWorkflowTest() throws Exception {
        String configurationXML = Util1.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util1.readResourceContent("/syntax/actions/action_program.xml");
        Project transformer = UnitTestHelper.setupWithConfigurationAndProgramXML(this.mbotFactory, programXML, configurationXML).build();
        ArduinoCompilerWorker worker = new ArduinoCompilerWorker();
        worker.execute(transformer);
    }

    @Ignore
    @Test
    public void botnrollCompilerWorkflowTest() throws Exception {
        String configurationXML = Util1.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util1.readResourceContent("/syntax/actions/action_program.xml");
        Project transformer = UnitTestHelper.setupWithConfigurationAndProgramXML(this.botnrollFactory, programXML, configurationXML).build();
        ArduinoCompilerWorker worker = new ArduinoCompilerWorker();
        worker.execute(transformer);
    }

    @Ignore
    @Test
    public void senseboxCompilerWorkflowTest() throws Exception {
        String configurationXML = Util1.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util1.readResourceContent("/syntax/actions/action_program.xml");
        Project transformer = UnitTestHelper.setupWithConfigurationAndProgramXML(this.senseboxFactory, programXML, configurationXML).build();
        ArduinoCompilerWorker worker = new ArduinoCompilerWorker();
        worker.execute(transformer);
    }
}
