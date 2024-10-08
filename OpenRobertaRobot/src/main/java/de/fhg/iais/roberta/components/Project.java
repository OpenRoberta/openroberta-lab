package de.fhg.iais.roberta.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAst;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

/**
 * This class stores the AST representation of the program and the configuration as well as everything needed for executing workflows it contains the result of
 * code generation and compilation for the robots that are auto connected. This is the main data storage in the system.
 */
public final class Project {

    private static final Logger LOG = LoggerFactory.getLogger(Project.class);
    private final Map<String, JSONObject> confAnnotationList = new HashMap<String, JSONObject>();
    private final ClassToInstanceMap<IProjectBean> workerResults = MutableClassToInstanceMap.create();
    private final Map<String, String> resultParams = new HashMap<>();
    private String token;
    private String robot;
    private String programName;
    private String compiledProgramPath;
    private String SSID;
    private String password;
    private ILanguage language;
    private RobotCommunicator robotCommunicator;
    private RobotFactory robotFactory;
    private boolean withWrapping = true;
    private boolean isNativeEditorCode = false;
    private ProgramAst program = null;
    private ConfigurationAst configuration = null;
    private StringBuilder sourceCodeBuilder = new StringBuilder();
    private StringBuilder indentationBuilder = null;
    private String compiledHex = "";
    private Key result = Key.COMPILERWORKFLOW_PROJECT_BUILD_SUCCESS;
    private int errorCounter = 0;
    private JSONObject configurationJSON;
    private String programAsBlocklyXML = null;
    private String programAsTextly = null;
    private String configurationAsBlocklyXML = null;
    private List<String> errorAndWarningMessages = null;

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

    public RobotFactory getRobotFactory() {
        return this.robotFactory;
    }

    public boolean isWithWrapping() {
        return this.withWrapping;
    }

    public boolean isNativeEditorCode() {
        return this.isNativeEditorCode;
    }

    public ProgramAst getProgramAst() {
        return this.program;
    }

    public void setProgramAst(ProgramAst program) {
        this.program = program;
    }

    public ConfigurationAst getConfigurationAst() {
        return this.configuration;
    }

    public void setConfigurationAst(ConfigurationAst configuration) {
        this.configuration = configuration;
    }

    public Map<String, JSONObject> getConfAnnotationList() {
        return this.confAnnotationList;
    }

    public void addToConfAnnotationList(String blockId, NepoInfo nepoInfo) {
        this.confAnnotationList.put(blockId, nepoInfo.getAsJson());
    }

    public <T extends IProjectBean> T getWorkerResult(Class<T> beanClass) {
        IProjectBean bean = this.workerResults.get(beanClass);
        // TODO: remove this hack after visitor rewrite!
        if ( bean == null && beanClass.isAssignableFrom(NNBean.class) ) {
            bean = new NNBean.Builder().build();
        } else {
            Assert.notNull(bean, "No worker result bean with " + beanClass.getSimpleName() + " available!");
        }
        return beanClass.cast(bean);
    }

    public void addWorkerResult(IProjectBean bean) {
        IProjectBean existingBean = this.workerResults.get(bean.getClass());
        Assert.isNull(existingBean, "A worker result bean with " + bean.getClass().getSimpleName() + " already exists!");
        this.workerResults.put(bean.getClass(), bean);
    }

    public void appendWorkerResult(IProjectBean bean) {
        IProjectBean existingBean = this.workerResults.get(bean.getClass());
        if ( existingBean == null ) {
            this.addWorkerResult(bean);
        } else {
            existingBean.merge(bean);
        }
    }

    /**
     * very dangerous, this allows to pass the source code builder between worker. It is assumed, that only ONE worker
     * will write. This is not true anymore. The RegenerateNepoWorker sets own source builder, but compensates this
     * dangerous desing
     * @return the UNIQUE builder for the whole worker chain
     */
    public StringBuilder getSourceCodeBuilder() {
        return this.sourceCodeBuilder;
    }

    /**
     * very dangerous, this allows to pass the source code builder between worker. It is assumed, that only ONE worker
     * will write. This is not true anymore. The RegenerateNepoWorker sets own source builder, but compensates this
     * dangerous desing
     * @return the UNIQUE builder for the whole worker chain
     */
    public StringBuilder getIndentationBuilder() {
        return this.indentationBuilder;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCodeBuilder = new StringBuilder(sourceCode);
    }

    public void setIndentationBuilder(StringBuilder indentationBuilder) {
        this.indentationBuilder = indentationBuilder;
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

    public void addToErrorCounter(int nErrors, List<String> errorAndWarningMessages) {
        this.errorCounter += nErrors;
        if ( errorAndWarningMessages != null ) {
            if ( this.errorAndWarningMessages == null ) {
                this.errorAndWarningMessages = new ArrayList<>(errorAndWarningMessages);
            } else {
                this.errorAndWarningMessages.addAll(errorAndWarningMessages);
            }
        }
    }

    public int getErrorCounter() {
        return this.errorCounter;
    }

    public List<String> getErrorAndWarningMessages() {
        return errorAndWarningMessages;
    }

    public String getProgramAsBlocklyXML() {
        Assert.notNull(programAsBlocklyXML, "Transformation of program AST into NEPO not executed by the worker chain (blockly).");
        return programAsBlocklyXML;
    }

    public String getProgramAsTextly() {
        Assert.notNull(programAsTextly, "Transformation of program AST into NEPO not executed by the worker chain (textly).");
        return programAsTextly;
    }

    public void setProgramAsBlocklyXML(String programAsBlocklyXML) {
        Assert.isNull(this.programAsBlocklyXML);
        this.programAsBlocklyXML = programAsBlocklyXML;
    }

    public void setProgramAsTextly(String programAsTextly) {
        Assert.isNull(this.programAsTextly);
        this.programAsTextly = programAsTextly;
    }

    public String getConfigurationAsBlocklyXML() {
        Assert.notNull(configurationAsBlocklyXML, "Transformation of configuration AST into NEPO not executed by the worker chain.");
        return configurationAsBlocklyXML;
    }

    public void setConfigurationAsBlocklyXML(String configurationAsBlocklyXML) {
        Assert.isNull(this.configurationAsBlocklyXML);
        this.configurationAsBlocklyXML = configurationAsBlocklyXML;
    }

    public JSONObject getConfigurationJSON() {
        return this.configurationJSON;
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

        public Builder setFactory(RobotFactory factory) {
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
            this.project.configurationJSON = new JSONObject();
            try {
                this.project.configurationJSON.put("TRACKWIDTH", configurationAst.getTrackWidth());
                this.project.configurationJSON.put("WHEELDIAMETER", configurationAst.getWheelDiameter());
                this.project.configurationJSON.put("SENSORS", configurationAst2JSON(configurationAst.getSensors()));
                this.project.configurationJSON.put("ACTUATORS", configurationAst2JSON(configurationAst.getActors()));
            } catch ( JSONException e ) {
                throw new DbcException("exception when generating the configuration ", e);
            }
            return this;
        }

        private static JSONObject configurationAst2JSON(Collection<ConfigurationComponent> configurationAst) {
            JSONObject compJSON = new JSONObject();
            for ( ConfigurationComponent component : configurationAst ) {
                JSONObject propJSON = new JSONObject();
                propJSON.put("TYPE", component.componentType);
                for ( String prop : component.getComponentProperties().keySet() ) {
                    propJSON.put(prop, component.getComponentProperties().get(prop));
                }
                if ( component.hasSubComponents() ) {
                    LinkedHashMap<String, List<ConfigurationComponent>> subComponentLists = component.getSubComponents();
                    for ( String subComponentList : subComponentLists.keySet() ) {
                        JSONObject sub = new JSONObject();
                        List<ConfigurationComponent> subList = subComponentLists.get(subComponentList);
                        for ( ConfigurationComponent subComp : subList ) {
                            JSONObject subPropJSON = new JSONObject();
                            subPropJSON.put("TYPE", subComp.componentType);
                            for ( String prop : subComp.getComponentProperties().keySet() ) {
                                subPropJSON.put(prop, subComp.getComponentProperties().get(prop));
                            }
                            sub.put(subComp.userDefinedPortName, subPropJSON);
                        }
                        propJSON.put("SUBCOMPONENTS", sub);
                    }
                }
                compJSON.put(component.userDefinedPortName, propJSON);
            }
            return compJSON;
        }

        public Builder setProgramAst(ProgramAst programAst) {
            this.project.program = programAst;
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
                this.project.setSourceCode(this.programNativeSource);
                this.project.isNativeEditorCode = true;
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
            if ( this.programXml == null || this.programXml.trim().isEmpty() ) {
                this.project.result = Key.COMPILERWORKFLOW_ERROR_PROGRAM_NOT_FOUND;
            } else {
                try {
                    BlockSet blockSet = JaxbHelper.xml2BlockSet(this.programXml);
                    // Assume any program without or an empty xmlVersion is 2.0
                    String xmlversion = blockSet.getXmlversion();
                    if ( xmlversion == null || xmlversion.isEmpty() ) {
                        blockSet.setXmlversion("2.0");
                    }
                    Jaxb2ProgramAst transformer = new Jaxb2ProgramAst(this.project.robotFactory);
                    this.project.program = transformer.blocks2ast(blockSet);
                } catch ( JAXBException e ) {
                    LOG.error("Transformer failed", e);
                    this.project.result = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
                }
            }
        }

        /**
         * Transforms configuration XML into AST.
         */
        private void transformConfiguration() {
            if ( this.configurationXml == null || this.configurationXml.trim().isEmpty() ) {
                this.project.result = Key.COMPILERWORKFLOW_ERROR_CONFIGURATION_NOT_FOUND;
            } else {
                try {
                    BlockSet blockSet = JaxbHelper.xml2BlockSet(this.configurationXml);
                    // Assume any program without or an empty xmlVersion is 2.0
                    String xmlversion = blockSet.getXmlversion();
                    if ( xmlversion == null || xmlversion.isEmpty() ) {
                        blockSet.setXmlversion("2.0");
                    }
                    String topBlock = this.project.robotFactory.optTopBlockOfOldConfiguration();
                    ConfigurationAst confAst;
                    if ( this.project.robotFactory.getConfigurationType().equals("new") ) { // new configuration
                        // if the program has a new configuration but still has the old top block in the XML, the new default configuration should be loaded
                        if ( topBlock != null ) {
                            if ( this.configurationXml.contains(topBlock) ) {
                                blockSet = JaxbHelper.xml2BlockSet(this.project.robotFactory.getConfigurationDefault());
                            }
                        }
                        confAst = Jaxb2ConfigurationAst.blocks2NewConfig(blockSet, this.project.robotFactory.getBlocklyDropdownFactory());
                    } else { // old configuration
                        String sensorPrefix = this.project.robotFactory.optSensorPrefix();
                        if ( topBlock == null || sensorPrefix == null ) {
                            throw new DbcException("A top block and a sensor prefix are required for an old configuration!");
                        }
                        confAst =
                            Jaxb2ConfigurationAst.blocks2OldConfig(blockSet, this.project.robotFactory.getBlocklyDropdownFactory(), topBlock, sensorPrefix);
                    }
                    setConfigurationAst(confAst);
                    this.project.configuration.setRobotName(this.project.getRobot()); // TODO remove dependencies on robot name to remove this
                } catch ( JAXBException e ) {
                    LOG.error("Generation of the configuration failed", e);
                    this.project.result = Key.COMPILERWORKFLOW_ERROR_CONFIGURATION_TRANSFORM_FAILED;
                }
            }
        }
    }

}
