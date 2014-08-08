package de.fhg.iais.roberta.codegen.lejos;

import java.util.ArrayList;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * top class for all classes used to generate valid code from {@link Phrase} objects (<i>abstract syntax tree AST</i>).<br>
 * <br>
 * It is expected that all subclass are implementing the method {@link #generate(int)} for generation of code from AST. <br>
 * <br>
 * To create object of this class use {@link #GenerateCode(BrickConfiguration, ArrayList)} constructor.
 * 
 * @author kcvejoski
 */
public abstract class GenerateCode {
    protected ArrayList<Phrase> phrases;

    protected StringBuilder sb = new StringBuilder();
    protected BrickConfiguration brickConfiguration;

    /**
     * This constructor creates valid object of the class {@link GenerateCode}.<br>
     * <br>
     * Client must provide valid brick configuration {@link BrickConfiguration} and valid AST.
     * 
     * @param brickConfiguration
     * @param phrases of the AST
     */
    public GenerateCode(BrickConfiguration brickConfiguration, ArrayList<Phrase> phrases) {
        this.brickConfiguration = brickConfiguration;
        this.phrases = phrases;
    }

    /**
     * @param phrases of valid AST.
     */
    public void setPhrases(ArrayList<Phrase> phrases) {
        this.phrases = phrases;
    }

    /**
     * @return abstract syntax tree
     */
    public ArrayList<Phrase> getPhrases() {
        return this.phrases;
    }

    /**
     * @return string builder with generated code
     */
    public StringBuilder getSb() {
        return this.sb;
    }

    /**
     * @return brick configuration which is used in the code generation process.
     */
    public BrickConfiguration getBrickConfiguration() {
        return this.brickConfiguration;
    }

    /**
     * This method is used to generate code from AST and append to the string builder {@link #getSb()}. All subclasses should implement this method. <br>
     * <br>
     * Client calling this method must provide initial indentation.
     * 
     * @param indentation
     */
    public abstract void generate(int indentation);
}