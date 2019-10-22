package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.NaoPythonVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

public final class NaoPythonGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractLanguageVisitor getVisitor(UsedHardwareBean usedHardwareBean, CodeGeneratorSetupBean codeGeneratorSetupBean, Project project) {
        return new NaoPythonVisitor(
            usedHardwareBean,
            codeGeneratorSetupBean,
            project.getConfigurationAst(),
            project.getProgramAst().getTree(),
            project.getLanguage());
    }
}
