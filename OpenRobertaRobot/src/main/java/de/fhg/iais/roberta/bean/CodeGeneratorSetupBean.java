package de.fhg.iais.roberta.bean;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.util.HelperMethodGenerator;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.Assert;

public class CodeGeneratorSetupBean {

    private String helperMethodFile = "";
    private final Set<FunctionNames> usedFunctions = new HashSet<>();

    private HelperMethodGenerator helperMethodGenerator;

    private String fileExtension;

    public HelperMethodGenerator getHelperMethodGenerator() {
        return this.helperMethodGenerator;
    }

    public Set<FunctionNames> getUsedFunctions() {
        return this.usedFunctions;
    }

    public static class Builder {
        private final CodeGeneratorSetupBean codeGeneratorBean = new CodeGeneratorSetupBean();

        public Builder setHelperMethodFile(String file) {
            this.codeGeneratorBean.helperMethodFile = file;
            return this;
        }

        public Builder addUsedFunction(FunctionNames usedFunction) {
            this.codeGeneratorBean.usedFunctions.add(usedFunction);
            return this;
        }

        public Builder setFileExtension(String fileExtension) {
            this.codeGeneratorBean.fileExtension = fileExtension;
            return this;
        }

        public CodeGeneratorSetupBean build() {
            JSONObject helperMethods = new JSONObject();
            Util1.loadYAMLRecursive("", helperMethods, this.codeGeneratorBean.helperMethodFile, true);
            Assert.notNull(this.codeGeneratorBean.fileExtension, "File extension has to be set");
            this.codeGeneratorBean.helperMethodGenerator =
                new HelperMethodGenerator(helperMethods, HelperMethodGenerator.getLanguageFromFileExtension(this.codeGeneratorBean.fileExtension));
            return this.codeGeneratorBean;
        }
    }
}
