package de.fhg.iais.roberta.robotCommunication.ev3;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.ProjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.jaxb.JaxbHelper;
import de.fhg.iais.roberta.syntax.codegen.ev3.Ast2Ev3JavaVisitor;
import de.fhg.iais.roberta.syntax.codegen.ev3.Ast2Ev3PythonVisitor;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.transformer.ev3.Jaxb2Ev3ConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.Assert;

public class Ev3CompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(Ev3CompilerWorkflow.class);
    private final Ev3Communicator brickCommunicator;
    public final String pathToCrosscompilerBaseDir;
    public final String crossCompilerResourcesDir;
    public final String pathToCrossCompilerBuildXMLResource;

    @Inject
    public Ev3CompilerWorkflow(
        Ev3Communicator brickCommunicator,
        @Named("crosscompiler.basedir") String pathToCrosscompilerBaseDir, //
        @Named("robot.crossCompilerResources.dir") String crossCompilerResourcesDir, //
        @Named("crosscompiler.build.xml") String pathToCrossCompilerBuildXMLResource) //
    {
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
    public Key execute(String token, String programName, BlocklyProgramAndConfigTransformer data) {
        String fwName = this.brickCommunicator.getState(token).getFirmwareName();
        boolean doPython = fwName.equals("ev3dev");
        Ev3CompilerWorkflow.LOG.info("compiling for firmware: '" + fwName + "'");

        String sourceCode, ext;
        if ( doPython ) {
            sourceCode = Ast2Ev3PythonVisitor.generate(programName, data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true);
            ext = ".py";
            Ev3CompilerWorkflow.LOG.info("generating python code");
        } else {
            sourceCode = Ast2Ev3JavaVisitor.generate(programName, data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true);
            ext = ".java";
            Ev3CompilerWorkflow.LOG.info("generating java code");
        }

        Ev3CompilerWorkflow.LOG.info("to be compiled:\n{}", sourceCode); // only needed for EXTREME debugging
        try {
            storeGeneratedProgram(token, programName, sourceCode, ext);
        } catch ( Exception e ) {
            Ev3CompilerWorkflow.LOG.error("Storing the generated program into directory " + token + " failed", e);
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_STORE_FAILED;
        }
        if ( !doPython ) {
            Key messageKey = runBuild(token, programName, "generated.main");
            if ( messageKey == null ) {
                Ev3CompilerWorkflow.LOG.info("jar for program {} generated successfully", programName);
            }
            return messageKey;
        } else {
            // maybe copy from /src/ to /target/
            // python -c "import py_compile; py_compile.compile('.../src/...py','.../target/....pyc')"
            return null;
        }
    }

    /**
     * - take the program given<br>
     * - generate the AST<br>
     * - typecheck the AST, execute sanity checks, check a matching brick configuration<br>
     * - generate Java code<br>
     * - and return it
     *
     * @param programName name of the program
     * @param programText source of the program
     * @param configurationText the hardware configuration source that describes characteristic data of the robot
     * @return the generated Java program; null in case of an error
     */
    public String generateJavaProgram(String programName, String programText, String configurationText) {
        BlocklyProgramAndConfigTransformer data = BlocklyProgramAndConfigTransformer.transform(programText, configurationText);
        if ( data.getErrorMessage() != null ) {
            return null;
        }
        String sourceCode = Ast2Ev3JavaVisitor.generate(programName, data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true);
        Ev3CompilerWorkflow.LOG.info("generated Java code:\n{}", sourceCode); // only needed for EXTREME debugging
        return sourceCode;
    }

    /**
     * return the jaxb transformer for a given XML program text.
     *
     * @param blocklyXml the program XML as String
     * @return jaxb the transformer
     * @throws Exception
     */
    public static Jaxb2BlocklyProgramTransformer<Void> generateProgramTransformer(String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2BlocklyProgramTransformer<Void> transformer = new Jaxb2BlocklyProgramTransformer<>();
        transformer.transform(project);
        return transformer;
    }

    /**
     * return the brick configuration for given XML configuration text.
     *
     * @param blocklyXml the configuration XML as String
     * @return brick configuration
     * @throws Exception
     */
    public static Ev3Configuration generateConfiguration(String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2Ev3ConfigurationTransformer transformer = new Jaxb2Ev3ConfigurationTransformer();
        return transformer.transform(project);
    }

    private void storeGeneratedProgram(String token, String programName, String sourceCode, String ext) throws Exception {
        Assert.isTrue(token != null && programName != null && sourceCode != null);
        File sourceFile = new File(this.pathToCrosscompilerBaseDir + token + "/src/" + programName + ext);
        Ev3CompilerWorkflow.LOG.info("stored under: ", sourceFile.getPath());
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
    Key runBuild(String token, String mainFile, String mainPackage) {
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
            return null;
        } catch ( Exception e ) {
            if ( sb.length() > 0 ) {
                Ev3CompilerWorkflow.LOG.error("build exception. Messages from the build script are:\n" + sb.toString(), e);
            } else {
                Ev3CompilerWorkflow.LOG.error("exception when preparing the build", e);
            }
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }

}
