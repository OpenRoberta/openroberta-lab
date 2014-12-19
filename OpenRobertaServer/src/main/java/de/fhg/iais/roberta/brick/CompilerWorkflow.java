package de.fhg.iais.roberta.brick;

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

import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;
import de.fhg.iais.roberta.ast.transformer.JaxbBrickConfigTransformer;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.brickconfiguration.BrickConfiguration;
import de.fhg.iais.roberta.brickconfiguration.ev3.EV3BrickConfiguration;
import de.fhg.iais.roberta.codegen.lejos.AstToLejosJavaVisitor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.jaxb.JaxbHelper;
import de.fhg.iais.roberta.persistence.util.DbSession;

public class CompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(CompilerWorkflow.class);
    public final String pathToCrosscompilerBaseDir;
    public final String crossCompilerResourcesDir;
    public final String pathToCrossCompilerBuildXMLResource;

    @Inject
    public CompilerWorkflow(@Named("crosscompiler.basedir") String pathToCrosscompilerBaseDir, //
        @Named("robot.crossCompilerResources.dir") String crossCompilerResourcesDir, //
        @Named("crosscompiler.build.xml") String pathToCrossCompilerBuildXMLResource) //
    {
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
     * @param session to retrieve the the program from the database
     * @param token the credential the end user (at the terminal) and the brick have both agreed to use
     * @param
     * @param projectName to retrieve the program code
     * @param programName to retrieve the program code
     * @param configurationName the hardware configuration that is expected to have been used when assembling the brick
     * @return a message in case of an error; null otherwise
     */
    public String execute(DbSession session, String token, String programName, String programText, String configurationText) {
        if ( programText == null || programText.trim().equals("") ) {
            return "program not found or program has no blocks";
        }
        if ( configurationText == null || configurationText.trim().equals("") ) {
            return "configuration not found or configuration has no blocks";
        }

        JaxbBlocklyProgramTransformer<Void> programTransformer;
        try {
            programTransformer = generateProgramTransformer(programText);

        } catch ( Exception e ) {
            LOG.error("Transformer failed", e);
            return "program could not be transformed (message: " + e.getMessage() + ")";
        }
        EV3BrickConfiguration brickConfiguration;
        try {
            brickConfiguration = (EV3BrickConfiguration) generateConfiguration(configurationText);
        } catch ( Exception e ) {
            LOG.error("Generation of the configuration failed", e);
            return "configurationj could not be transformed (message: " + e.getMessage() + ")";
        }

        String javaCode = AstToLejosJavaVisitor.generate(programName, brickConfiguration, programTransformer.getTree(), true);

        LOG.info("to be compiled:\n{}", javaCode); // only needed for EXTREME debugging
        try {
            storeGeneratedProgram(token, programName, javaCode);
        } catch ( Exception e ) {
            return "generated java code could not be stored (message: + " + e.getMessage() + ")";
        }
        LOG.debug("generated program stored in directory/token {}", token);
        runBuild(token, programName, "generated.main");
        LOG.info("brick jar for program {} generated successfully", programName);
        return null;
    }

    /**
     * return the jaxb transformer for a given XML program text.
     *
     * @param blocklyXml the program XML as String
     * @return jaxb the transformer
     * @throws Exception
     */
    private JaxbBlocklyProgramTransformer<Void> generateProgramTransformer(String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        JaxbBlocklyProgramTransformer<Void> transformer = new JaxbBlocklyProgramTransformer<>();
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
    private BrickConfiguration generateConfiguration(String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        JaxbBrickConfigTransformer transformer = new JaxbBrickConfigTransformer();
        return transformer.transform(project);
    }

    private void storeGeneratedProgram(String token, String programName, String javaCode) throws Exception {
        Assert.isTrue(token != null && programName != null && javaCode != null);
        File javaFile = new File(this.pathToCrosscompilerBaseDir + token + "/src/" + programName + ".java");
        FileUtils.writeStringToFile(javaFile, javaCode, StandardCharsets.UTF_8.displayName());
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
    void runBuild(String token, String mainFile, String mainPackage) {
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
                    sb.append(event.getTarget().getName()).append("\n");
                }

                @Override
                public void targetFinished(BuildEvent event) {
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
        } catch ( Exception e ) {
            if ( sb.length() > 0 ) {
                LOG.error("build exception. Stacktrace is suppressed. Messages from build script are:\n" + sb.toString());
            } else {
                LOG.error("exception when preparing the build", e);
            }
            throw new DbcException("build exception", e);
        }
    }

}
