package de.fhg.iais.roberta.codegen.lejos;

import java.util.ArrayList;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Phrase;

public abstract class GenerateCode {
    protected ArrayList<Phrase> phrases;

    protected StringBuilder sb = new StringBuilder();
    protected BrickConfiguration brickConfiguration;

    public GenerateCode(BrickConfiguration brickConfiguration, ArrayList<Phrase> phrases) {
        this.brickConfiguration = brickConfiguration;
        this.phrases = phrases;
    }

    public void setPhrases(ArrayList<Phrase> phrases) {
        this.phrases = phrases;
    }

    public ArrayList<Phrase> getPhrases() {
        return this.phrases;
    }

    public StringBuilder getSb() {
        return this.sb;
    }

    public BrickConfiguration getBrickConfiguration() {
        return this.brickConfiguration;
    }

    public abstract void generate(int indentation);
}
