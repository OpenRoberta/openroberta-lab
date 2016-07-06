package de.fhg.iais.roberta.factory;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.ProjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.jaxb.JaxbHelper;
import de.fhg.iais.roberta.robotCommunication.ICompilerWorkflow;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.syntax.codegen.Ast2Ev3JavaVisitor;
import de.fhg.iais.roberta.syntax.codegen.Ast2Ev3PythonVisitor;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.ev3.Jaxb2Ev3ConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.Assert;

public class Ev3CompilerWorkflow implements ICompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(Ev3CompilerWorkflow.class);
    private final RobotCommunicator brickCommunicator;
    public final String pathToCrosscompilerBaseDir;
    public final String crossCompilerResourcesDir;
    public final String pathToCrossCompilerBuildXMLResource;

    private enum Language {
        JAVA( ".java" ), PYTHON( ".py" );

        private String extension;

        Language(String ext) {
            this.extension = ext;
        }

        String getExtension() {
            return this.extension;
        }

        public static Language fromCommunicationData(RobotCommunicationData state) {
            if ( state == null ) {
                return JAVA;
            }
            String fwName = state.getFirmwareName();
            return (fwName != null && fwName.equals("ev3dev")) ? PYTHON : JAVA;
        }
    }

    @Inject
    public Ev3CompilerWorkflow(
        RobotCommunicator brickCommunicator,
        String pathToCrosscompilerBaseDir,
        String crossCompilerResourcesDir,
        String pathToCrossCompilerBuildXMLResource) {
        this.brickCommunicator = brickCommunicator;
        this.pathToCrosscompilerBaseDir = pathToCrosscompilerBaseDir;
        this.crossCompilerResourcesDir = crossCompilerResourcesDir;
        this.pathToCrossCompilerBuildXMLResource = pathToCrossCompilerBuildXMLResource;
    }

    /**
     * - load the program from the database<br>
     * - generate the AST<br>
     * - typecheck the AST, execute sanity checks, check a matching brick configuration<br>
     * - generate Java code<br>
     * - store the code in a token-specific (thus user-specific) directory<br>
     * - compile the code and generate a jar in the token-specific directory (use a ant script, will be replaced later)<br>
     * <b>Note:</b> the jar is prepared for upload, but not uploaded from here. After a handshake with the brick (the brick has to tell, that it is ready) the
     * jar is uploaded to the brick from another thread and then started on the brick
     *
     * @param token the credential the end user (at the terminal) and the brick have both agreed to use
     * @param programName name of the program
     * @param programText source of the program
     * @param configurationText the hardware configuration source that describes characteristic data of the robot
     * @return a message key in case of an error; null otherwise
     */
    @Override
    public Key execute(String token, String programName, BlocklyProgramAndConfigTransformer data) {
        Language lang = Language.fromCommunicationData(this.brickCommunicator.getState(token));
        String sourceCode = generateProgram(lang, programName, data);

        //Ev3CompilerWorkflow.LOG.info("generated code:\n{}", sourceCode); // only needed for EXTREME debugging
        try {
            storeGeneratedProgram(token, programName, sourceCode, lang.getExtension());
        } catch ( Exception e ) {
            Ev3CompilerWorkflow.LOG.error("Storing the generated program into directory " + token + " failed", e);
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_STORE_FAILED;
        }
        switch ( lang ) {
            case JAVA:
                Key messageKey = runBuild(token, programName, "generated.main");
                if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
                    Ev3CompilerWorkflow.LOG.info("jar for program {} generated successfully", programName);
                } else {
                    Ev3CompilerWorkflow.LOG.info(messageKey.toString());
                }
                return messageKey;
            case PYTHON:
                // maybe copy from /src/ to /target/
                // python -c "import py_compile; py_compile.compile('.../src/...py','.../target/....pyc')"
                return Key.COMPILERWORKFLOW_SUCCESS;
        }
        return null;
    }

    /**
     * - take the program given<br>
     * - generate the AST<br>
     * - typecheck the AST, execute sanity checks, check a matching brick configuration<br>
     * - generate source code in the right language for the robot<br>
     * - and return it
     *
     * @param token the credential the end user (at the terminal) and the brick have both agreed to use
     * @param programName name of the program
     * @param programText source of the program
     * @param configurationText the hardware configuration source that describes characteristic data of the robot
     * @return the generated source code; null in case of an error
     */
    @Override
    public String generateSourceCode(IRobotFactory factory, String token, String programName, String programText, String configurationText) {
        BlocklyProgramAndConfigTransformer data = BlocklyProgramAndConfigTransformer.transform(factory, programText, configurationText);
        if ( data.getErrorMessage() != null ) {
            return null;
        }
        Language lang = Language.fromCommunicationData(this.brickCommunicator.getState(token));
        return generateProgram(lang, programName, data);
    }

    private String generateProgram(Language lang, String programName, BlocklyProgramAndConfigTransformer data) {
        String sourceCode = "";
        switch ( lang ) {
            case JAVA:
                sourceCode = Ast2Ev3JavaVisitor.generate(programName, data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true);
                break;
            case PYTHON:
                sourceCode = Ast2Ev3PythonVisitor.generate(programName, data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true);
                break;
        }
        Ev3CompilerWorkflow.LOG.info("generating {} code", lang.toString().toLowerCase());
        return sourceCode;
    }

    private void storeGeneratedProgram(String token, String programName, String sourceCode, String ext) throws Exception {
        Assert.isTrue(token != null && programName != null && sourceCode != null);
        File sourceFile = new File(this.pathToCrosscompilerBaseDir + token + "/src/" + programName + ext);
        Ev3CompilerWorkflow.LOG.info("stored under: " + sourceFile.getPath());
        FileUtils.writeStringToFile(sourceFile, sourceCode, StandardCharsets.UTF_8.displayName());
    }

    /**
     * 1. Make target folder (if not exists).<br>
     * 2. Clean target folder (everything inside).<br>
     * 3. Compile .java files to .class.<br>
     * 4. Make jar from class files and add META-INF entries.<br>
     *
     * @param token
     * @param mainFile
     * @param mainPackage
     */
    public Key runBuild(String token, String mainFile, String mainPackage) {
        final StringBuilder sb = new StringBuilder();
        try {
            File buildFile = new File(this.pathToCrossCompilerBuildXMLResource);
            org.apache.tools.ant.Project project = new org.apache.tools.ant.Project();

            project.init();
            project.setProperty("user.projects.dir", this.pathToCrosscompilerBaseDir);
            project.setProperty("crosscompiler.resources.dir", this.crossCompilerResourcesDir);
            project.setProperty("token.dir", token);
            project.setProperty("main.name", mainFile);
            project.setProperty("main.package", mainPackage);

            ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
            projectHelper.parse(project, buildFile);

            project.addBuildListener(new BuildListener() {
                @Override
                public void taskStarted(BuildEvent event) {
                }

                @Override
                public void taskFinished(BuildEvent event) {
                }

                @Override
                public void targetStarted(BuildEvent event) {
                    sb.append("targetStart: ").append(event.getTarget().getName()).append("\n");
                }

                @Override
                public void targetFinished(BuildEvent event) {
                    sb.append("targetEnd:   ").append(event.getTarget().getName()).append("\n");
                }

                @Override
                public void messageLogged(BuildEvent event) {
                    sb.append(event.getMessage()).append("\n");
                }

                @Override
                public void buildStarted(BuildEvent event) {
                }

                @Override
                public void buildFinished(BuildEvent event) {
                }
            });
            project.executeTarget(project.getDefaultTarget());
            // LOG.info("build ok. Messages from build script are:\n" + sb.toString());
            return Key.COMPILERWORKFLOW_SUCCESS;
        } catch ( Exception e ) {
            if ( sb.length() > 0 ) {
                Ev3CompilerWorkflow.LOG.error("build exception. Messages from the build script are:\n" + sb.toString(), e);
            } else {
                Ev3CompilerWorkflow.LOG.error("exception when preparing the build", e);
            }
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }

    /**
     * return the brick configuration for given XML configuration text.
     *
     * @param blocklyXml the configuration XML as String
     * @return brick configuration
     * @throws Exception
     */
    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2Ev3ConfigurationTransformer transformer = new Jaxb2Ev3ConfigurationTransformer(factory);
        return transformer.transform(project);
    }

}
