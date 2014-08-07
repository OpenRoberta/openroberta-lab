package de.fhg.iais.roberta.codegen.lejos;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;

public abstract class WrappCode {
    protected StringBuilder sb = new StringBuilder();
    protected BrickConfiguration brickConfiguration;
    protected String programName;

    public WrappCode(String programName, BrickConfiguration brickConfiguration) {
        this.brickConfiguration = brickConfiguration;
        this.programName = programName;
    }

    public BrickConfiguration getBrickConfiguration() {
        return this.brickConfiguration;
    }

    public String getProgramName() {
        return this.programName;
    }

    public abstract String generate(GenerateCode generateCode);
}
