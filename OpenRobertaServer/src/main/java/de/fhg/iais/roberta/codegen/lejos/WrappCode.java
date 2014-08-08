package de.fhg.iais.roberta.codegen.lejos;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;

/**
 * top class for all classes used to generate valid code for wrapping generated code from {@link GenerateCode} object.<br>
 * <br>
 * It is expected that all subclass are implementing the method {@link #generate(GenerateCode)} for generation of surrounding code that helps the code generated
 * form {@link GenerateCode} to be executed. <br>
 * <br>
 * To create object of this class use {@link #WrappCode(String, BrickConfiguration)} constructor.
 * 
 * @author kcvejoski
 */
public abstract class WrappCode {
    protected StringBuilder sb = new StringBuilder();
    protected BrickConfiguration brickConfiguration;
    protected String programName;

    /**
     * This constructor creates valid object of the class {@link WrappCode}.<br>
     * <br>
     * Client must provide name of the program (<i>e.g. for JAVA this is used as a class name</i>) and valid brick configuration {@link BrickConfiguration}.
     * 
     * @param programName
     * @param brickConfiguration
     */
    public WrappCode(String programName, BrickConfiguration brickConfiguration) {
        this.brickConfiguration = brickConfiguration;
        this.programName = programName;
    }

    /**
     * @return brick configuration which is used in the code generation process.
     */
    public BrickConfiguration getBrickConfiguration() {
        return this.brickConfiguration;
    }

    /**
     * @return name of the program
     */
    public String getProgramName() {
        return this.programName;
    }

    /**
     * This method is used to generate code from AST and append to the string builder {@link #getSb()}. All subclasses should implement this method. <br>
     * <br>
     * Client calling this method must provide initial indentation.
     * 
     * @param indentation
     */
    /**
     * This method is used to surrounding code that helps the code generated
     * form {@link GenerateCode} to be executed. All subclasses should implement this method. <br>
     * <br>
     * Client calling this method must provide {@link GenerateCode} object which generates the code that should be wrapped.
     * 
     * @param generateCode
     * @return valid code in the desired language ready for compilation.
     */
    public abstract String generate(GenerateCode generateCode);
}
