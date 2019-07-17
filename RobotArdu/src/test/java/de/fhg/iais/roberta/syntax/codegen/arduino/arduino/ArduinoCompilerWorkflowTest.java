package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.codegen.ArduinoCompilerWorkflow;
import de.fhg.iais.roberta.codegen.Bob3CompilerWorkflow;
import de.fhg.iais.roberta.codegen.BotnrollCompilerWorkflow;
import de.fhg.iais.roberta.codegen.MbotCompilerWorkflow;
import de.fhg.iais.roberta.codegen.SenseboxCompilerWorkflow;
import de.fhg.iais.roberta.factory.Bob3Factory;
import de.fhg.iais.roberta.factory.BotnrollFactory;
import de.fhg.iais.roberta.factory.MbotFactory;
import de.fhg.iais.roberta.factory.MegaFactory;
import de.fhg.iais.roberta.factory.NanoFactory;
import de.fhg.iais.roberta.factory.SenseboxFactory;
import de.fhg.iais.roberta.factory.UnoFactory;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util1;

public class ArduinoCompilerWorkflowTest {
    private static final String ORA_CC_RSC_ENVVAR = ServerProperties.CROSSCOMPILER_RESOURCE_BASE.replace('.', '_');
    private static final Logger LOG = LoggerFactory.getLogger(ArduinoActorTest.class);
    private UnoFactory unoFactory;
    private NanoFactory nanoFactory;
    private MegaFactory megaFactory;
    private Bob3Factory bob3Factory;
    private BotnrollFactory botnrollFactory;
    private MbotFactory mbotFactory;
    private SenseboxFactory senseboxFactory;
    private ArduinoCompilerWorkflow arduinoCompilerWorkflow;
    private Bob3CompilerWorkflow bob3CompilerWorkflow;
    private MbotCompilerWorkflow mbotCompilerWorkflow;
    private BotnrollCompilerWorkflow botnrollCompilerWorkflow;
    private SenseboxCompilerWorkflow senseboxCompilerWorkflow;

    @Ignore
    @Before
    public void init() {
        if ( System.getenv(ORA_CC_RSC_ENVVAR) == null ) {
            LOG.error("the environment variable \"" + ORA_CC_RSC_ENVVAR + "\" must contain the absolute path to the ora-cc-rsc repository - test fails");
            fail();
        }
        PluginProperties properties = new PluginProperties("uno", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/uno.properties"));
        this.unoFactory = new UnoFactory(properties);
        this.arduinoCompilerWorkflow = new ArduinoCompilerWorkflow(properties);
        properties = new PluginProperties("nano", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/nano.properties"));
        this.nanoFactory = new NanoFactory(properties);
        properties = new PluginProperties("mega", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/mega.properties"));
        this.megaFactory = new MegaFactory(properties);
        properties = new PluginProperties("bob3", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/bob3.properties"));
        this.bob3Factory = new Bob3Factory(properties);
        this.bob3CompilerWorkflow = new Bob3CompilerWorkflow(properties);
        properties = new PluginProperties("botnroll", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/botnroll.properties"));
        this.botnrollFactory = new BotnrollFactory(properties);
        this.botnrollCompilerWorkflow = new BotnrollCompilerWorkflow(properties);
        properties = new PluginProperties("mbot", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/mbot.properties"));
        this.mbotFactory = new MbotFactory(properties);
        this.mbotCompilerWorkflow = new MbotCompilerWorkflow(properties);
    }

    @Ignore
    @Test
    public void arduinoCompilerWorkflowTest() throws Exception {
        String configurationXML = Util1.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util1.readResourceContent("/syntax/actions/action_program.xml");
        BlocklyProgramAndConfigTransformer transformer = BlocklyProgramAndConfigTransformer.transform(this.unoFactory, programXML, configurationXML);
        this.arduinoCompilerWorkflow.generateSourceAndCompile("ABCDEFGH", "action", transformer, null);
    }

    @Ignore
    @Test
    public void bob3CompilerWorkflowTest() throws Exception {
        String configurationXML = Util1.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util1.readResourceContent("/syntax/actions/action_program.xml");
        BlocklyProgramAndConfigTransformer transformer = BlocklyProgramAndConfigTransformer.transform(this.bob3Factory, programXML, configurationXML);
        this.bob3CompilerWorkflow.generateSourceAndCompile("ABCDEFGH", "action", transformer, null);
    }

    @Ignore
    @Test
    public void mbotCompilerWorkflowTest() throws Exception {
        String configurationXML = Util1.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util1.readResourceContent("/syntax/actions/action_program.xml");
        BlocklyProgramAndConfigTransformer transformer = BlocklyProgramAndConfigTransformer.transform(this.mbotFactory, programXML, configurationXML);
        this.mbotCompilerWorkflow.generateSourceAndCompile("ABCDEFGH", "action", transformer, null);
    }

    @Ignore
    @Test
    public void botnrollCompilerWorkflowTest() throws Exception {
        String configurationXML = Util1.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util1.readResourceContent("/syntax/actions/action_program.xml");
        BlocklyProgramAndConfigTransformer transformer = BlocklyProgramAndConfigTransformer.transform(this.botnrollFactory, programXML, configurationXML);
        this.botnrollCompilerWorkflow.generateSourceAndCompile("ABCDEFGH", "action", transformer, null);
    }

    @Ignore
    @Test
    public void senseboxCompilerWorkflowTest() throws Exception {
        String configurationXML = Util1.readResourceContent("/syntax/actions/action_configuration.xml");
        String programXML = Util1.readResourceContent("/syntax/actions/action_program.xml");
        BlocklyProgramAndConfigTransformer transformer = BlocklyProgramAndConfigTransformer.transform(this.senseboxFactory, programXML, configurationXML);
        this.senseboxCompilerWorkflow.generateSourceAndCompile("ABCDEFGH", "action", transformer, null);
    }
}
