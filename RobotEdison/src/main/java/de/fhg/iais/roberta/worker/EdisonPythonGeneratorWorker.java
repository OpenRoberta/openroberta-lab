package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.EdisonPythonVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

public final class EdisonPythonGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractLanguageVisitor getVisitor(UsedHardwareBean usedHardwareBean, CodeGeneratorSetupBean codeGeneratorSetupBean, Project project) {
        return new EdisonPythonVisitor(usedHardwareBean, codeGeneratorSetupBean, project.getConfigurationAst(), project.getProgramAst().getTree());
    }
}
