package de.fhg.iais.roberta.worker.codegen;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.Bob3CppVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;
import de.fhg.iais.roberta.worker.AbstractLanguageGeneratorWorker;

public class Bob3CxxGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractLanguageVisitor getVisitor(UsedHardwareBean usedHardwareBean, CodeGeneratorSetupBean codeGeneratorSetupBean, Project project) {
        return new Bob3CppVisitor(usedHardwareBean, codeGeneratorSetupBean, project.getProgramAst().getTree());
    }
}
