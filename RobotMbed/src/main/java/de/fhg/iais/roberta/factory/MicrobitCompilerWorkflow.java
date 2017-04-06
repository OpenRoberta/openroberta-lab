package de.fhg.iais.roberta.factory;

import java.lang.ProcessBuilder.Redirect;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.MicrobitConfiguration;
import de.fhg.iais.roberta.jaxb.JaxbHelper;
import de.fhg.iais.roberta.robotCommunication.ICompilerWorkflow;
import de.fhg.iais.roberta.syntax.codegen.Ast2PythonMicroBitVisitor;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.Jaxb2MicrobitConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;

public class MicrobitCompilerWorkflow implements ICompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(MicrobitCompilerWorkflow.class);

    public final String robotCompilerResourcesDir;
    public final String robotCompilerDir;

    private String compiledHex = "";

    public MicrobitCompilerWorkflow(String robotCompilerResourcesDir, String robotCompilerDir) {
        this.robotCompilerResourcesDir = robotCompilerResourcesDir;
        this.robotCompilerDir = robotCompilerDir;

    }

    /**
     * - load the program from the database<br>
     * - generate the AST<br>
     * - typecheck the AST, execute sanity checks, check a matching brick
     * configuration<br>
     * - generate Java code<br>
     * - store the code in a token-specific (thus user-specific) directory<br>
     * - compile the code and generate a jar in the token-specific directory
     * (use a ant script, will be replaced later)<br>
     * <b>Note:</b> the jar is prepared for upload, but not uploaded from here.
     * After a handshake with the brick (the brick has to tell, that it is
     * ready) the jar is uploaded to the brick from another thread and then
     * started on the brick
     *
     * @param token
     *        the credential the end user (at the terminal) and the brick
     *        have both agreed to use
     * @param programName
     *        name of the program
     * @param programText
     *        source of the program
     * @param configurationText
     *        the hardware configuration source that describes
     *        characteristic data of the robot
     * @return a message key in case of an error; null otherwise
     */
    @Override
    public Key execute(String token, String programName, BlocklyProgramAndConfigTransformer data) {
        String sourceCode =
            Ast2PythonMicroBitVisitor.generate((MicrobitConfiguration) data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true);

        Key messageKey = runBuild(sourceCode);
        if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
            MicrobitCompilerWorkflow.LOG.info("hex for program {} generated successfully", programName);
        } else {
            MicrobitCompilerWorkflow.LOG.info(messageKey.toString());
        }
        return messageKey;
    }

    /**
     * - take the program given<br>
     * - generate the AST<br>
     * - typecheck the AST, execute sanity checks, check a matching brick
     * configuration<br>
     * - generate source code in the right language for the robot<br>
     * - and return it
     *
     * @param token
     *        the credential the end user (at the terminal) and the brick
     *        have both agreed to use
     * @param programName
     *        name of the program
     * @param programText
     *        source of the program
     * @param configurationText
     *        the hardware configuration source that describes
     *        characteristic data of the robot
     * @return the generated source code; null in case of an error
     */
    @Override
    public String generateSourceCode(IRobotFactory factory, String token, String programName, String programText, String configurationText) {
        BlocklyProgramAndConfigTransformer data = BlocklyProgramAndConfigTransformer.transform(factory, programText, configurationText);
        if ( data.getErrorMessage() != null ) {
            return null;
        }

        return Ast2PythonMicroBitVisitor.generate((MicrobitConfiguration) data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true);
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
    Key runBuild(String sourceCode) {
        final StringBuilder sb = new StringBuilder();

        String scriptName = this.robotCompilerResourcesDir + "/compile.py";

        try {
            ProcessBuilder procBuilder = new ProcessBuilder(new String[] {
                this.robotCompilerDir + "python",
                scriptName,
                sourceCode
            });

            procBuilder.redirectInput(Redirect.INHERIT);
            procBuilder.redirectError(Redirect.INHERIT);
            Process p = procBuilder.start();

            this.compiledHex = IOUtils.toString(p.getInputStream(), "US-ASCII");
            //            FileUtils.writeStringToFile(new File("/home/kcvejoski/Desktop/test.hex"), out); TESTING PURPOSE
            return Key.COMPILERWORKFLOW_SUCCESS;
        } catch ( Exception e ) {
            if ( sb.length() > 0 ) {
                MicrobitCompilerWorkflow.LOG.error("build exception. Messages from the build script are:\n" + sb.toString(), e);
            } else {
                MicrobitCompilerWorkflow.LOG.error("exception when preparing the build", e);
            }
            e.printStackTrace();
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }

    @Override
    public String getCompiledCode() {
        return this.compiledHex;
    }

    /**
     * return the brick configuration for given XML configuration text.
     *
     * @param blocklyXml
     *        the configuration XML as String
     * @return brick configuration
     * @throws Exception
     */
    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2MicrobitConfigurationTransformer transformer = new Jaxb2MicrobitConfigurationTransformer(factory);
        return transformer.transform(project);
    }

}
