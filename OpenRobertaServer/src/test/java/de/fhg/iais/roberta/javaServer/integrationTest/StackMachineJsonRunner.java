package de.fhg.iais.roberta.javaServer.integrationTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * <b>Testing the execution of stack machine code.</b> At the moment exclusively for the WeDo robot. To be extended to the simulation.<br>
 * <br>
 * <i>Usage:</i><br>
 * For a program, put the expected result into the program documentation tab, e.g.<br>
 * <br>
 * <code>
 * ROBOT<br>
 * wedo<br>
 * START-RESULT<br>
 * show "1"<br><br>show "2"<br>
 * END-RESULT<br><br>
 * </code> The string after "show" refers to the string written by the "show text"/"zeige Text"-blockly-block in the program. Empty lines are ignored.<br>
 * Export the program into the directory <code>src/test/resources/crosscompilerTests/robotSpecific/wedo</code>.<br>
 * <br>
 * This class {@linkplain StackMachineJsonRunner} expects, that
 * <ul>
 * <li>the blockly-xml of a given PROGRAM has been translated to byte code in the ORA-format "StackMachineJson". This is done in
 * {@linkplain CompilerWorkflowRobotSpecificIT}
 * <li>the byte code will be stored here into file <code>./target/generatedStackmachinePrograms/PROGRAM.json</code>.
 * <li>the source code will be stored here into file <code>./target/generatedStackmachinePrograms/PROGRAM.xml</code>.
 * </ul>
 * Then the JavaScript found in <code>TypeScriptSources/jsGenerated/runStackMachineJson.js</code> is executed with <code>node</code> (must be on the path!).
 * This runs the interpreter for "StackMachineJson" and compares the results with the results expected. The expected result is found in the source XML (see
 * above!)<br>
 * <br>
 * <i>Note:</i> all sources used for this interpretation are found in project "TypeScriptSources" directory "ts/app/nepostackmachine". They are written in
 * TypeScript, transpiled to JavaScript and saved into the directory "TypeScriptSources/jsGenerated". From this directory they are used by "node".
 */
public class StackMachineJsonRunner {
    private static final Logger LOG = LoggerFactory.getLogger(StackMachineJsonRunner.class);

    public final String generatedStackmachineProgramsDir;
    private List<String> resultsOfInterpretation;

    public StackMachineJsonRunner(String generatedStackmachineProgramsDir) {
        this.generatedStackmachineProgramsDir = generatedStackmachineProgramsDir;
        Assert.isTrue(new File(generatedStackmachineProgramsDir).isDirectory());
    }

    /**
     * run the interpreter
     *
     * @param programName
     * @param programTextInclResultSpec the blockly XML including the result spec; so to say the source program
     * @param stackmachineCode the stackmachine bytecode; so to say the result of the compilation
     * @return true, if the interpretation succeeded; false otherwise
     */
    public boolean run(String programName, String programTextInclResultSpec, String stackmachineCode) throws Exception {
        final String utf8Charset = StandardCharsets.UTF_8.displayName();
        FileUtils.writeStringToFile(new File(generatedStackmachineProgramsDir + programName + ".json"), stackmachineCode, utf8Charset);
        FileUtils.writeStringToFile(new File(generatedStackmachineProgramsDir + programName + ".xml"), programTextInclResultSpec, utf8Charset);
        resultsOfInterpretation = runCommand("node", "../TypeScriptSources/jsGenerated/_main.js", generatedStackmachineProgramsDir, programName);
        return showAndEvaluateResult();
    }

    private static List<String> runCommand(String... command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        int returnCode = process.waitFor();
        LOG.info("node call returned error code " + returnCode);
        if ( returnCode != 0 ) {
            LOG.error("node call returned error code " + returnCode);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("UTF-8")));
        return br.lines().collect(Collectors.toList());
    }

    private boolean showAndEvaluateResult() {
        boolean successForInterpretation = false;
        if ( resultsOfInterpretation.size() == 0 ) {
            LOG.error("the node evaluation didn't return result lines: error");
        } else {
            for ( String line : resultsOfInterpretation ) {
                if ( line.equals("********** result of interpretation: success **********") ) {
                    successForInterpretation = true;
                }
                LOG.info(line);
            }
        }
        return successForInterpretation;
    }
}