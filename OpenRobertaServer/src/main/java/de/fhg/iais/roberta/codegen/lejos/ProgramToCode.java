package de.fhg.iais.roberta.codegen.lejos;

import de.fhg.iais.roberta.helper.Helper;

public class ProgramToCode {
    private final String location;
    private final WrappCode wrappCode;
    private final GenerateCode generateCode;
    private final StringBuilder sb = new StringBuilder();

    public ProgramToCode(String location, WrappCode wrappCode, GenerateCode generateCode) {
        this.location = location;
        this.wrappCode = wrappCode;
        this.generateCode = generateCode;
    }

    public String getLocation() {
        return this.location;
    }

    public WrappCode getWrappCode() {
        return this.wrappCode;
    }

    public GenerateCode getGenerateCode() {
        return this.generateCode;
    }

    public StringBuilder getSb() {
        return this.sb;
    }

    public void generate() throws Exception {
        this.generateCode.setPhrases(Helper.generateAST(this.location).getTree());
        this.sb.append(this.wrappCode.generate(this.generateCode));
    }

}
