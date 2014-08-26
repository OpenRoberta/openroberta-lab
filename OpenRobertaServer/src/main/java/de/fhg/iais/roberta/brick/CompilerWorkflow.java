package de.fhg.iais.roberta.brick;

import de.fhg.iais.roberta.persistence.connector.SessionWrapper;

public class CompilerWorkflow {

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
     * @param projectName to retrieve the program code
     * @param programName to retrieve the program code
     * @param configurationName the hardware configuration that is expected to have been used when assembling the brick
     * @return a message in case of an error; null otherwise
     */
    public static String execute(SessionWrapper session, String token, String projectName, String programName, String configurationName) {

        return null;
    }

}
