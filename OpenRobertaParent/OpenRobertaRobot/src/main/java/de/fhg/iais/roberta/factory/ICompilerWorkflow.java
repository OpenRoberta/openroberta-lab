package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.util.Key;

public interface ICompilerWorkflow {

    /**
     * - take the program and configuration (require, that these are without errors)<br>
     * - generate and typecheck the AST, execute sanity checks, check the matching robot configuration<br>
     * - generate source code in the target language for the robot<br>
     *
     * @param token the credential supplied by the user. Needed to provide a unique directory name for crosscompilation
     * @param programName name of the program
     * @param transformer holding program and configuration. Require, that <code>transformer.getErrorMessage() == null</code>
     * @param language locale to be used for messages
     * @return the generated source code; null in case of an error
     */
    String generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer transformer, ILanguage language);

    /**
     * - take the source program<br>
     * - store the code in a token-specific (thus user-specific) target directory<br>
     * - compile the code and generate a library in the target directory<br>
     * <br>
     * <b>Note:</b> the library is prepared for being "uploaded" to the robot, but that is NOT done here. There are different "upload" strategies, among
     * others:<br>
     * - robots get the library after a handshake between robot (e.g. ev3) or USB program (acting for the robot, e.g. nxt) and the server<br>
     * - for other robots code is sent to a download directory of the client computer (e.g. Calliope)
     *
     * @param flagProvider TODO
     * @return a message key in case of an error; null otherwise
     */
    Key compileSourceCode(String token, String programName, String sourceCode, ILanguage language, Object flagProvider);

    /**
     * - take the program given<br>
     * - generate the AST<br>
     * - typecheck the AST, execute sanity checks, check a matching robot configuration<br>
     * - generate source code in target language for the robot<br>
     * - store the code in a token-specific (thus user-specific) target directory<br>
     * - compile the code and generate a library in the target directory<br>
     * <b>Note:</b> the library is prepared for being "uploaded" to the robot, but that is NOT done here. There are different "upload" strategies, among
     * others:<br>
     * - robots get the library after a handshake between robot (e.g. ev3) or USB program (acting for the robot, e.g. nxt) and the server<br>
     * - for other robots code is sent to a download directory of the client computer (e.g. Calliope)<br>
     *
     * @param token the credential the end user (at the terminal) and the brick have both agreed to use
     * @param programName name of the program
     * @param transformer to acces the AST of program and configuration
     * @param language the locale to be used for messages
     * @return a message key in case of an error; null otherwise
     */
    Key generateSourceAndCompile(String token, String programName, BlocklyProgramAndConfigTransformer transformer, ILanguage language);

    /**
     * return the robot configuration for a given XML configuration text.
     *
     * @param blocklyXml the configuration XML as String
     * @return robot configuration
     */
    Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception;

    /**
     * return the compilation result. This is needed for some robots (e.g. arduino). Some implementations return null.
     *
     * @return the compilation result.
     */
    String getCompiledCode();
}
