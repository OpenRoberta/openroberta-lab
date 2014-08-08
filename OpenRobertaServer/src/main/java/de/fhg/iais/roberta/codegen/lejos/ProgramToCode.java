package de.fhg.iais.roberta.codegen.lejos;

import de.fhg.iais.roberta.helper.Helper;

/**
 * This class is used to generate valid code in desired programming language ready for compilation and execution from a valid XML stored program.<br>
 * <br>
 * To create object from this class use {@link ProgramToCode} constructor.
 * 
 * @author kcvejoski
 */
public class ProgramToCode {
    private final String location;
    private final WrappCode wrappCode;
    private final GenerateCode generateCode;
    private final StringBuilder sb = new StringBuilder();

    /**
     * This constructor create valid object from the {@linkplain ProgramToCode} class.<br>
     * <br>
     * Client must provide location where the program is stored, object that wraps the code and object that generates code from AST in desired programming
     * language.
     * 
     * @param location where the program is stored,
     * @param wrappCode
     * @param generateCode
     */
    public ProgramToCode(String location, WrappCode wrappCode, GenerateCode generateCode) {
        this.location = location;
        this.wrappCode = wrappCode;
        this.generateCode = generateCode;
    }

    /**
     * @return location of where the program is stored.
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @return object which contains the wrapped code.
     */
    public WrappCode getWrappCode() {
        return this.wrappCode;
    }

    /**
     * @return object which contains code for the program created.
     */
    public GenerateCode getGenerateCode() {
        return this.generateCode;
    }

    /**
     * @return string builder which contains whole program.
     */
    public StringBuilder getSb() {
        return this.sb;
    }

    /**
     * Generates valid code from a XML stored program.
     * 
     * @throws Exception
     */
    public void generate() throws Exception {
        this.generateCode.setPhrases(Helper.generateAST(this.location).getTree());
        this.sb.append(this.wrappCode.generate(this.generateCode));
    }

}
