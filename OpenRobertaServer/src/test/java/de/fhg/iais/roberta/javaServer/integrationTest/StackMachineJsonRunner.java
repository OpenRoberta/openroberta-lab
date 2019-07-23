package de.fhg.iais.roberta.javaServer.integrationTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.Util1;

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
 * Then the JavaScript found in <code>WedoInterpreter/jsGenerated/runStackMachineJson.js</code> is executed with <code>node</code> (must be on the path!). This
 * runs the interpreter for "StackMachineJson" and compares the results with the results expected. The expected result is found in the source XML (see
 * above!)<br>
 * <br>
 * <i>Note:</i> all sources used for this interpretation are found in project "WedoInterpreter" directory "ts". They are written in TypeScript, transpiled to
 * JavaScript and saved into the directory "WedoInterpreter/jsGenerated". From this directory they are used by "node".
 */
public class StackMachineJsonRunner {
    private static final Logger LOG = LoggerFactory.getLogger(StackMachineJsonRunner.class);

    private static final String STACKMACHINE_PROGRAMS_OUTPUT_DIR = "./target/generatedStackmachinePrograms/";

    private List<String> resultsOfInterpretation;

    public StackMachineJsonRunner() {
        new File(STACKMACHINE_PROGRAMS_OUTPUT_DIR).mkdirs();
    }

    /**
     * run the interpreter for one program
     *
     * @param programName
     * @param programTextInclResultSpec the blockly XML including the result spec; so to say the source program
     * @param stackmachineCode the stackmachine bytecode; so to say the result of the compilation
     * @return true, if the interpretation succeeded; false otherwise
     */
    public boolean runOneProgram(String programName, String programTextInclResultSpec, String stackmachineCode) throws Exception {
        Util1.writeFile(STACKMACHINE_PROGRAMS_OUTPUT_DIR + programName + ".json", stackmachineCode);
        Util1.writeFile(STACKMACHINE_PROGRAMS_OUTPUT_DIR + programName + ".xml", programTextInclResultSpec);
        String nodeCall = "node ../WedoInterpreter/jsGenerated/_main.js" + " " + STACKMACHINE_PROGRAMS_OUTPUT_DIR + " " + programName;
        resultsOfInterpretation = runCommand(nodeCall);
        return showAndEvaluateResult();
    }

    private static List<String> runCommand(String command) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream(), Charset.forName("UTF-8")));
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