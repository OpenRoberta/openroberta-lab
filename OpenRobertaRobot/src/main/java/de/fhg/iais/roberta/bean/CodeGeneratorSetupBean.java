package de.fhg.iais.roberta.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import de.fhg.iais.roberta.util.HelperMethodGenerator;
import de.fhg.iais.roberta.util.HelperMethodGenerator.Language;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * Container for code generation related information and functionality. Creates the HelperMethodGenerator for use in the code generation visitors.
 * TODO should include more general information like global variables etc.
 */
public class CodeGeneratorSetupBean implements IProjectBean {

    private HelperMethodGenerator helperMethodGenerator;
    private final Set<Enum<?>> usedMethods = new HashSet<>(); // All needed helper methods as a Set
    private NNBean nnBean;

    public HelperMethodGenerator getHelperMethodGenerator() {
        return this.helperMethodGenerator;
    }

    public Set<Enum<?>> getUsedMethods() {
        return Collections.unmodifiableSet(this.usedMethods);
    }

    public NNBean getNNBean() {
        return this.nnBean;
    }

    public static class Builder implements IBuilder<CodeGeneratorSetupBean> {
        private final CodeGeneratorSetupBean codeGeneratorBean = new CodeGeneratorSetupBean();

        private String helperMethodFile = null;
        private String fileExtension = null;
        private final Collection<Class<? extends Enum<?>>> additionalEnums = new ArrayList<>();

        public Builder setHelperMethodFile(String file) {
            this.helperMethodFile = file;
            return this;
        }

        public Builder addUsedMethods(Collection<Enum<?>> usedMethods) {
            this.codeGeneratorBean.usedMethods.addAll(usedMethods);
            return this;
        }

        public Builder setFileExtension(String fileExtension) {
            this.fileExtension = fileExtension;
            return this;
        }

        public Builder addAdditionalEnums(Collection<Class<? extends Enum<?>>> additionalEnums) {
            this.additionalEnums.addAll(additionalEnums);
            return this;
        }

        public Builder setNNBean(NNBean nnBean) {
            codeGeneratorBean.nnBean = nnBean;
            return this;
        }

        /**
         * Builds a bean from the provided information. Creates a {@link HelperMethodGenerator} from the provided helper method file and file extension.
         *
         * @return the finished bean
         */
        @Override
        public CodeGeneratorSetupBean build() {
            Assert.notNull(this.helperMethodFile, "Helper method file has to be set");
            Assert.notNull(this.fileExtension, "File extension has to be set");

            JSONObject helperMethods = new JSONObject();
            Util.loadYAMLRecursive("", helperMethods, this.helperMethodFile, true);
            this.codeGeneratorBean.helperMethodGenerator = new HelperMethodGenerator(helperMethods, getLanguageFromFileExtension(this.fileExtension));
            for ( Class<? extends Enum<?>> additionalEnum : this.additionalEnums ) {
                this.codeGeneratorBean.helperMethodGenerator.addAdditionalEnum(additionalEnum);
            }
            return this.codeGeneratorBean;
        }
    }

    private static Language getLanguageFromFileExtension(String ext) {
        switch ( ext ) {
            case "java":
                return Language.JAVA;
            case "py":
                return Language.PYTHON;
            case "cpp":
            case "ino":
            case "nxc":
                return Language.C;
            case "json":
                return Language.JSON;
            case "aesl":
                return Language.ASEBA;
            default:
                throw new DbcException("File extension not implemented!");
        }
    }
}
