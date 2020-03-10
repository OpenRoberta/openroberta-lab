package de.fhg.iais.roberta.components;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAst;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import static de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAst.block2OldConfiguration;

/**
 * This class stores the AST representation of the program and the configuration as well as everything needed for executing workflows
 * it contains the result of code generation and compilation for the robots that are auto connected. This is the main data storage in the system.
 */
public final class Project {

    private static final Logger LOG = LoggerFactory.getLogger(Project.class);

    private String token;
    private String robot;
    private String programName;
    private String compiledProgramPath;
    private String SSID;
    private String password;
    private ILanguage language;
    private RobotCommunicator robotCommunicator;
    private IRobotFactory robotFactory;
    private boolean withWrapping = true;

    private ProgramAst<Void> program = null;
    private ConfigurationAst configuration = null;

    private final ClassToInstanceMap<IProjectBean> workerResults = MutableClassToInstanceMap.create();

    private StringBuilder sourceCodeBuilder = new StringBuilder();
    private final StringBuilder indentationBuilder = new StringBuilder();

    private String compiledHex = "";

    private Key result = Key.COMPILERWORKFLOW_PROJECT_BUILD_SUCCESS;
    private final Map<String, String> resultParams = new HashMap<>();
    private int errorCounter = 0;

    private Project() {
    }

    public String getToken() {
        return this.token;
    }

    public String getRobot() {
        return this.robot;
    }

    public String getProgramName() {
        return this.programName;
    }

    public String getCompiledProgramPath() {
        return this.compiledProgramPath;
    }

    public String getSourceCodeFileExtension() {
        return this.robotFactory.getSourceCodeFileExtension();
    }

    public String getBinaryFileExtension() {
        return this.robotFactory.getBinaryFileExtension();
    }

    public String getSSID() {
        return this.SSID;
    }

    public String getPassword() {
        return this.password;
    }

    public ILanguage getLanguage() {
        return this.language;
    }

    public RobotCommunicator getRobotCommunicator() {
        return this.robotCommunicator;
    }

    public IRobotFactory getRobotFactory() {
        return this.robotFactory;
    }

    public boolean isWithWrapping() {
        return this.withWrapping;
    }

    /**
     * @return the programTransformer
     */
    public ProgramAst<Void> getProgramAst() {
        return this.program;
    }

    /**
     * @return the robot configuration
     */
    public ConfigurationAst getConfigurationAst() {
        return this.configuration;
    }

    public <T extends IProjectBean> T getWorkerResult(Class<T> beanClass) {
        IProjectBean bean = this.workerResults.get(beanClass);
        Assert.notNull(bean, "No worker result bean with " + beanClass.getSimpleName() + " available!");
        return beanClass.cast(bean);
    }

    public void addWorkerResult(IProjectBean bean) {
        IProjectBean existingBean = this.workerResults.get(bean.getClass());
        Assert.isNull(existingBean, "A worker result bean with " + bean.getClass().getSimpleName() + " already exists!");
        this.workerResults.put(bean.getClass(), bean);
    }

    public void appendWorkerResult(IProjectBean bean) {
        IProjectBean existingBean = this.workerResults.get(bean.getClass());
        if (existingBean == null) {
            this.addWorkerResult(bean);
        } else {
            existingBean.merge(bean);
        }
    }

    public StringBuilder getSourceCode() {
        return this.sourceCodeBuilder;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCodeBuilder = new StringBuilder(sourceCode);
    }

    public StringBuilder getIndentation() {
        return this.indentationBuilder;
    }

    /**
     * @return this will actually return either Intel Hex or base 64 encoded binary
     */
    public String getCompiledHex() {
        return this.compiledHex;
    }

    public void setCompiledHex(String compiledHex) {
        this.compiledHex = compiledHex;
    }

    public Key getResult() {
        return this.result;
    }

    public void setResult(Key result) {
        this.result = result;
    }

    public boolean hasSucceeded() {
        Assert.notNull(this.result);
        return this.result.isSuccess();
    }

    public Map<String, String> getResultParams() {
        return Collections.unmodifiableMap(this.resultParams);
    }

    public void addResultParam(String key, String value) {
        this.resultParams.put(key, value);
    }

    public void addToErrorCounter(int nErrors) {
        this.errorCounter += nErrors;
    }

    public int getErrorCounter() {
        return this.errorCounter;
    }

    public String getAnnotatedProgramAsXml() {
        String programXML = "";
        try {
            programXML = jaxbToXml(astToJaxb(this.program));
        } catch ( JAXBException e ) {
            throw new DbcException("Transformation of program AST into blockset and into XML failed.", e);
        }
        return programXML;
    }

    public String getAnnotatedConfigurationAsXml() {
        String configurationXML = "";
        try {
            configurationXML = jaxbToXml(this.configuration.generateBlockSet());
        } catch ( JAXBException e ) {
            throw new DbcException("Transformation of configuration AST into blockset and into XML failed.", e);
        }
        return configurationXML;
    }

    private static String jaxbToXml(BlockSet blockSet) throws JAXBException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        final Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);
        final StringWriter writer = new StringWriter();
        m.marshal(blockSet, writer);
        return writer.toString();
    }

    private static BlockSet astToJaxb(ProgramAst<Void> program) {
        Assert.notNull(program);
        ArrayList<ArrayList<Phrase<Void>>> astProgram = program.getTree();
        final BlockSet blockSet = new BlockSet();
        blockSet.setDescription(program.getDescription());
        blockSet.setRobottype(program.getRobotType());
        blockSet.setTags(program.getTags());
        blockSet.setXmlversion(program.getXmlVersion());

        for ( final ArrayList<Phrase<Void>> tree : astProgram ) {
            final Instance instance = new Instance();
            blockSet.getInstance().add(instance);
            for ( final Phrase<Void> phrase : tree ) {
                if ( phrase.getKind().hasName("LOCATION") ) {
                    instance.setX(((Location<Void>) phrase).getX());
                    instance.setY(((Location<Void>) phrase).getY());
                }
                instance.getBlock().add(phrase.astToBlock());
            }
        }
        return blockSet;
    }

    public static class Builder {
        private final Project project = new Project();

        private String configurationXml;
        private String programXml;
        private String programNativeSource;
        private String compiledProgramPath;

        public Builder setToken(String token) {
            this.project.token = token;
            return this;
        }

        public Builder setRobot(String robot) {
            this.project.robot = robot;
            return this;
        }

        public Builder setCompiledProgramPath(String compiledProgramPath) {
            this.compiledProgramPath = compiledProgramPath;
            return this;
        }

        public Builder setProgramName(String programName) {
            this.project.programName = programName;
            return this;
        }

        public Builder setSSID(String sSID) {
            this.project.SSID = sSID;
            return this;
        }

        public Builder setPassword(String password) {
            this.project.password = password;
            return this;
        }

        public Builder setLanguage(ILanguage language) {
            this.project.language = language;
            return this;
        }

        public Builder setRobotCommunicator(RobotCommunicator robotCommunicator) {
            this.project.robotCommunicator = robotCommunicator;
            return this;
        }

        public Builder setFactory(IRobotFactory factory) {
            this.project.robotFactory = factory;
            return this;
        }

        public Builder setWithWrapping(boolean withWrapping) {
            this.project.withWrapping = withWrapping;
            return this;
        }

        public Builder setProgramXml(String programXml) {
            this.programXml = programXml;
            return this;
        }

        public Builder setConfigurationXml(String configurationXml) {
            this.configurationXml = configurationXml;
            return this;
        }

        public Builder setConfigurationAst(ConfigurationAst configurationAst) {
            this.project.configuration = configurationAst;
            return this;
        }

        public Builder setProgramNativeSource(String programNativeSource) {
            this.programNativeSource = programNativeSource;
            return this;
        }

        public Project build() {
            if ( this.compiledProgramPath != null ) { // Used to flash the system with a precompiled program
                this.project.compiledProgramPath = this.compiledProgramPath;
                int index = this.compiledProgramPath.lastIndexOf('/');
                this.project.programName = this.compiledProgramPath.substring(index + 1);
            } else if ( this.programNativeSource != null ) { // Used to run native code directly
                Assert.isNull(this.programXml, "Program XML should not be set when using native compile");
                Assert.isNull(this.configurationXml, "Configuration XML should not be set when using native compile");
                this.project.setSourceCode(this.programNativeSource);
            } else { // STANDARD CASE - Used to follow the default generation, compilation, run from blockly            
                if ( this.project.configuration == null ) {
                    transformConfiguration();
                }
                if ( this.project.program == null ) {
                    transformProgram();
                }
            }
            return this.project;
        }

        /**
         * Transforms program XML into AST.
         */
        private void transformProgram() {
            if ( (this.programXml == null) || this.programXml.trim().isEmpty() ) {
                this.project.result = Key.COMPILERWORKFLOW_ERROR_PROGRAM_NOT_FOUND;
            } else {
                try {
                    Jaxb2ProgramAst<Void> programTransformer = JaxbHelper.generateProgramTransformer(this.project.robotFactory, this.programXml);
                    this.project.program = programTransformer.getData();
                } catch ( Exception e ) {
                    LOG.error("Transformer failed", e);
                    this.project.result = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
                }
            }
        }

        /**
         * Transforms configuration XML into AST.
         */
        private void transformConfiguration() {
            if ( (this.configurationXml == null) || this.configurationXml.trim().isEmpty() ) {
                this.project.result = Key.COMPILERWORKFLOW_ERROR_CONFIGURATION_NOT_FOUND;
            } else {
                try {
                    final BlockSet blockSet = JaxbHelper.xml2BlockSet(this.configurationXml);
                    if ( this.project.robotFactory.getConfigurationType().equals("new") ) {
                        this.project.configuration = transformConfiguration(blockSet);
                    } else {
                        this.project.configuration =
                            transformOldConfiguration(
                                blockSet,
                                this.project.robotFactory.getTopBlockOfOldConfiguration(),
                                this.project.robotFactory.getSensorPrefix());
                    }
                    this.project.configuration.setRobotName(this.project.getRobot()); // TODO remove dependencies on robot name to remove this
                } catch ( Exception e ) {
                    LOG.error("Generation of the configuration failed", e);
                    this.project.result = Key.COMPILERWORKFLOW_ERROR_CONFIGURATION_TRANSFORM_FAILED;
                }
            }
        }

        private ConfigurationAst transformConfiguration(BlockSet blockSet) {
            List<Instance> instances = blockSet.getInstance();
            List<List<Block>> blocks = new ArrayList<>();
            for ( int i = 0; i < instances.size(); i++ ) {
                blocks.add(instances.get(i).getBlock());
            }
            return Jaxb2ConfigurationAst.blocks2NewConfiguration(blocks, this.project.robotFactory.getBlocklyDropdownFactory());
        }

        private ConfigurationAst transformOldConfiguration(BlockSet blockSet, String topBlockName, String sensorsPrefix) {
            Block startingBlock = Jaxb2ConfigurationAst.getTopBlock(blockSet, topBlockName);
            return block2OldConfiguration(startingBlock, this.project.robotFactory.getBlocklyDropdownFactory(), sensorsPrefix);
        }
    }

}
